package com.kadioglumf.scheduler.mapper;

import com.kadioglumf.scheduler.payload.request.CreateScheduledJobRequest;
import com.kadioglumf.scheduler.payload.response.ScheduledJobResponse;
import com.kadioglumf.scheduler.model.scheduler.ScheduledJobModel;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ScheduledMapper {

    ScheduledJobModel toScheduledJobModel(CreateScheduledJobRequest request);


    default Page<ScheduledJobResponse> toScheduledJobResponse(Page<ScheduledJobModel> model) {
        return model.map(this::toScheduledJobResponse);
    }

    ScheduledJobResponse toScheduledJobResponse(ScheduledJobModel model);
}
