package com.kadioglumf.scheduler.service.scheduled.impl;

import com.kadioglumf.scheduler.adepter.TestServiceAdapter;
import com.kadioglumf.scheduler.service.scheduled.ScheduledJobExecutorService;
import com.kadioglumf.scheduler.exceptions.SchedulerServiceException;
import com.kadioglumf.scheduler.model.scheduler.ApplicationServiceType;
import com.kadioglumf.scheduler.model.scheduler.ScheduledJobDetailModel;
import com.kadioglumf.scheduler.model.scheduler.ScheduledJobDetailsStatusType;
import com.kadioglumf.scheduler.model.scheduler.ScheduledJobModel;
import com.kadioglumf.scheduler.payload.response.error.ErrorType;
import com.kadioglumf.scheduler.repository.ScheduledDetailsRepository;
import com.kadioglumf.scheduler.repository.ScheduledRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ScheduledJobExecutorServiceImpl implements ScheduledJobExecutorService {

    private final ScheduledDetailsRepository scheduledDetailsRepository;
    private final ScheduledRepository scheduledRepository;
    private final TaskScheduler taskScheduler;
    private final TestServiceAdapter testServiceAdapter;

    private Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();


    @Override
    @PostConstruct
    public void initialize() {
        scheduledRepository.findAll().forEach(this::addSchedulerJobToTaskScheduler);
    }

    @Override
    public void process(ScheduledJobModel scheduledJob) {
        ScheduledJobDetailModel scheduledJobDetail =  new ScheduledJobDetailModel();
        scheduledJobDetail.setScheduledJob(scheduledJob);
        scheduledJobDetail.setStatus(ScheduledJobDetailsStatusType.INITIAL);
        long start = System.currentTimeMillis();

        try {
            if (ApplicationServiceType.TEST_SERVICE.equals(scheduledJob.getApplicationServiceType())) {
                log.info("test scheduled started...");
                testServiceAdapter.process(scheduledJob.getPath(), scheduledJob.getHttpMethod());
            }
            // other services
            scheduledJobDetail.setStatus(ScheduledJobDetailsStatusType.SUCCESSFUL);
        }
        catch (Exception ex) {
            scheduledJobDetail.setStatus(ScheduledJobDetailsStatusType.ERROR);
            scheduledJobDetail.setErrorMessage(ex.getMessage());
        }

        long end = System.currentTimeMillis();
        scheduledJobDetail.setExecutionStartDate(new Date(start));
        scheduledJobDetail.setExecutionTime(end - start);
        scheduledDetailsRepository.save(scheduledJobDetail);
    }

    @Override
    public void addSchedulerJobToTaskScheduler(ScheduledJobModel scheduledJobModel) {
        ScheduledFuture<?> scheduledFuture = taskScheduler
                .schedule(() -> process(scheduledJobModel),
                        new CronTrigger(scheduledJobModel.getCronExpression()));

        scheduledTasks.put(scheduledJobModel.getName(), scheduledFuture);
    }

    @Override
    public void removeSchedulerJobToTaskScheduler(String scheduledJobName) {
        removeSchedulerJobsWithJobNameToTaskScheduler(scheduledJobName);
    }

    @Override
    public void removeAllSchedulerJobsToTaskScheduler() {
        Set<String> keySet = scheduledTasks.keySet();
        keySet.forEach(this::removeSchedulerJobsWithJobNameToTaskScheduler);
    }

    private void removeSchedulerJobsWithJobNameToTaskScheduler(String jobName) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(jobName);
        scheduledFuture.cancel(true);
        if (!scheduledFuture.isCancelled()) {
            throw new SchedulerServiceException(ErrorType.SCHEDULER_JOB_CANCEL_ERROR,
                    String.format("Scheduled job with name: %s could not cancel : ", jobName));
        }
    }
}
