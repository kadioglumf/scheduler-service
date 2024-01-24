package com.kadioglumf.scheduler.service.scheduled;

import com.kadioglumf.scheduler.payload.request.CreateScheduledJobRequest;
import com.kadioglumf.scheduler.payload.request.UpdateScheduledJobRequest;
import com.kadioglumf.scheduler.payload.request.search.SearchRequest;
import com.kadioglumf.scheduler.payload.response.ScheduledJobResponse;
import org.springframework.data.domain.Page;

public interface ScheduledService {

    void create(CreateScheduledJobRequest request);

    void update(UpdateScheduledJobRequest request);

    void delete(Long id);

    Page<ScheduledJobResponse> getScheduledJobs(SearchRequest request);

    void restartAllJobs();
}
