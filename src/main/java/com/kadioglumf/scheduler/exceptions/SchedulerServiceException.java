package com.kadioglumf.scheduler.exceptions;

import com.kadioglumf.scheduler.payload.response.error.BaseErrorResponse;
import com.kadioglumf.scheduler.payload.response.error.ErrorType;

public class SchedulerServiceException extends RuntimeException {
    private final BaseErrorResponse errorResponse;

    public SchedulerServiceException(BaseErrorResponse errorResponse) {
        super(errorResponse.getErrorMessage());
        this.errorResponse = errorResponse;
    }

    public SchedulerServiceException(ErrorType errorType) {
        super(errorType.getDescription());
        this.errorResponse = new BaseErrorResponse(errorType);
    }

    public SchedulerServiceException(int statusCode, ErrorType errorType) {
        super(errorType.getDescription());
        this.errorResponse = new BaseErrorResponse(statusCode, errorType);
    }

    public SchedulerServiceException(int statusCode, ErrorType errorType, String errorMessage) {
        super(errorMessage);
        this.errorResponse = new BaseErrorResponse(statusCode, errorType, errorMessage);
    }

    public SchedulerServiceException(ErrorType errorType, String errorMessage) {
        super(errorMessage);
        this.errorResponse = new BaseErrorResponse(errorType, errorMessage);
    }

    public BaseErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
