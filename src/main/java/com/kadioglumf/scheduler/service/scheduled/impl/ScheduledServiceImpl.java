package com.kadioglumf.scheduler.service.scheduled.impl;

import com.kadioglumf.scheduler.service.scheduled.ScheduledJobExecutorService;
import com.kadioglumf.scheduler.service.scheduled.ScheduledService;
import com.kadioglumf.scheduler.service.search.SearchExpressionType;
import com.kadioglumf.scheduler.exceptions.SchedulerServiceException;
import com.kadioglumf.scheduler.mapper.ScheduledMapper;
import com.kadioglumf.scheduler.model.scheduler.ScheduledJobModel;
import com.kadioglumf.scheduler.payload.request.CreateScheduledJobRequest;
import com.kadioglumf.scheduler.payload.request.UpdateScheduledJobRequest;
import com.kadioglumf.scheduler.payload.request.search.SearchRequest;
import com.kadioglumf.scheduler.payload.response.ScheduledJobResponse;
import com.kadioglumf.scheduler.payload.response.error.ErrorType;
import com.kadioglumf.scheduler.repository.ScheduledRepository;
import com.kadioglumf.scheduler.service.search.SearchSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ScheduledServiceImpl implements ScheduledService {

    private final ScheduledRepository scheduledRepository;
    private final ScheduledMapper scheduledMapper;
    private final ScheduledJobExecutorService scheduledJobExecutorService;


    @Override
    public void create(CreateScheduledJobRequest request) {
        if (scheduledRepository.existsByName(request.getName())) {
            throw new SchedulerServiceException(ErrorType.SCHEDULER_JOB_ALREADY_EXIST_ERROR);
        }
        checkCronExpression(request.getCronExpression());
        ScheduledJobModel model = scheduledMapper.toScheduledJobModel(request);
        ScheduledJobModel savedScheduledJob = scheduledRepository.save(model);
        scheduledJobExecutorService.addSchedulerJobToTaskScheduler(savedScheduledJob);
    }

    @Override
    public void update(UpdateScheduledJobRequest request) {
        checkCronExpression(request.getCronExpression());
        ScheduledJobModel scheduledJob = getScheduledJobModelById(request.getId());
        if (!scheduledJob.getName().equals(request.getName())
                && scheduledRepository.existsByName(request.getName()))
        {
            throw new SchedulerServiceException(ErrorType.SCHEDULER_JOB_ALREADY_EXIST_ERROR);
        }
        String oldScheduledJobName = scheduledJob.getName();

        scheduledJob.setName(request.getName());
        scheduledJob.setCronExpression(request.getCronExpression());
        scheduledJob.setPath(request.getPath());
        scheduledJob.setApplicationServiceType(request.getApplicationServiceType());
        scheduledJob.setPath(request.getPath());

        ScheduledJobModel savedScheduledJob = scheduledRepository.save(scheduledJob);

        scheduledJobExecutorService.removeSchedulerJobToTaskScheduler(oldScheduledJobName);
        scheduledJobExecutorService.addSchedulerJobToTaskScheduler(savedScheduledJob);
    }

    @Override
    public void delete(Long id) {
        ScheduledJobModel scheduledJob = getScheduledJobModelById(id);
        scheduledJobExecutorService.removeSchedulerJobToTaskScheduler(scheduledJob.getName());
        scheduledRepository.delete(scheduledJob);
    }

    @Override
    public Page<ScheduledJobResponse> getScheduledJobs(SearchRequest request) {
        SearchSpecification<ScheduledJobModel> specification = new SearchSpecification<>(request, SearchExpressionType.GET_SCHEDULED_JOBS);
        Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
        Page<ScheduledJobModel> scheduledJobModels = scheduledRepository.findAll(specification, pageable);
        return scheduledMapper.toScheduledJobResponse(scheduledJobModels);
    }

    @Override
    public void restartAllJobs() {
        scheduledJobExecutorService.removeAllSchedulerJobsToTaskScheduler();
        scheduledJobExecutorService.initialize();
    }

    private ScheduledJobModel getScheduledJobModelById(Long scheduledJobId) {
        Optional<ScheduledJobModel> scheduledJobModel = scheduledRepository.findById(scheduledJobId);
        if (scheduledJobModel.isEmpty()) {
            throw new SchedulerServiceException(ErrorType.SCHEDULER_JOB_NOT_FOUND_ERROR);
        }
        return scheduledJobModel.get();
    }

    private void checkCronExpression(String cron) {
        if (!CronSequenceGenerator.isValidExpression(cron)) {
            throw new SchedulerServiceException(ErrorType.CRON_VALUE_NOT_VALID_ERROR, "Cron Expression not valid : " + cron);
        }
    }
}
