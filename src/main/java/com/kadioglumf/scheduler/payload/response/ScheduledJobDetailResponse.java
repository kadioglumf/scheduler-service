package com.kadioglumf.scheduler.payload.response;

import com.kadioglumf.scheduler.model.scheduler.ScheduledJobDetailsStatusType;
import lombok.Data;

import java.util.Date;

@Data
public class ScheduledJobDetailResponse {
    private Long id;
    private ScheduledJobDetailsStatusType status;
    private long executionTime;
    private Date executionStartDate;
    private String errorMessage;
}
