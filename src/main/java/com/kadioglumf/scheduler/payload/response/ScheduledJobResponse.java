package com.kadioglumf.scheduler.payload.response;

import com.kadioglumf.scheduler.model.scheduler.ApplicationServiceType;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.Date;

@Data
public class ScheduledJobResponse {
    private Long id;
    private String name;
    private String cronExpression;
    private String path;
    private HttpMethod httpMethod;
    private Date creationDate;
    private ApplicationServiceType applicationServiceType;
}
