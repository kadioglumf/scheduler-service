package com.kadioglumf.scheduler.service.scheduled;

import com.kadioglumf.scheduler.model.scheduler.ScheduledJobModel;

public interface ScheduledJobExecutorService {

    void initialize();

    void process(ScheduledJobModel scheduledJobModel);

    void addSchedulerJobToTaskScheduler(ScheduledJobModel scheduledJobModel);

    void removeSchedulerJobToTaskScheduler(String scheduledJobName);

    void removeAllSchedulerJobsToTaskScheduler();
}
