package com.kadioglumf.scheduler.payload.request;

import com.kadioglumf.scheduler.model.scheduler.ApplicationServiceType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateScheduledJobRequest {
    @NotBlank
    private String name;

    @NotBlank
    @ApiModelProperty(name = "cronExpression", dataType = "String", value = "cronExpression", example = "0 0/1 * * * *")
    private String cronExpression;

    @NotBlank
    private String path;

    @NotNull
    private HttpMethod httpMethod;

    @NotNull
    private ApplicationServiceType applicationServiceType;
}
