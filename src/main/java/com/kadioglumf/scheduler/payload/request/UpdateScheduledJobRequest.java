package com.kadioglumf.scheduler.payload.request;

import com.kadioglumf.scheduler.model.scheduler.ApplicationServiceType;
import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateScheduledJobRequest {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String cronExpression;

    @NotBlank
    private String path;

    @NotNull
    private HttpMethod httpMethod;

    @NotNull
    private ApplicationServiceType applicationServiceType;
}
