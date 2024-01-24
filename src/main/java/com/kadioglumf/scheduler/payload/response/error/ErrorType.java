package com.kadioglumf.scheduler.payload.response.error;

public enum ErrorType {

    //microservice error types
    TEST_SERVICE_ERROR(1001, "Unknown test service exception", 500),


    UNKNOWN_SCHEDULER_SERVICE_ERROR(12001, "Unknown scheduler service exception", 500),
    FILTER_PARAMETER_BLACKLIST_ERROR(12002, "Blacklist filter parameter", 400),
    FILTER_PARAMETER_CANNOT_NULL_ERROR(12003, "Filter parameter cannot be null.", 400),
    FILTER_TYPE_NOT_SUITABLE_ERROR(12004, "Cannot search with this filter type and field type.", 400),
    SEARCH_UNKNOWN_ERROR(12005, "Unknow search error", 500),
    DATE_FORMAT_NOT_FOUND_ERROR(12006, "Date format not found.", 400),
    LOCAL_DATE_PARSE_ERROR(12007, "LocalDate parse exception.", 400),
    NUMBER_PARSE_ERROR(12008, "Number parse error", 500),
    FORBIDDEN_ERROR(12009, "You cannot reach this resource!", 403),
    SCHEDULER_JOB_CANCEL_ERROR(12010, "Scheduled job cancel error!", 403),
    SCHEDULER_JOB_NOT_FOUND_ERROR(12011, "Scheduled job not found error!", 403),
    REQUEST_OBJECT_NOT_VALID_ERROR(12012, "Request object not valid.", 400),
    CRON_VALUE_NOT_VALID_ERROR(12013, "Cron value not valid.", 400),
    SCHEDULER_JOB_ALREADY_EXIST_ERROR(12013, "Scheduler job already exist.", 400),





    //External service error types


    ;


    private final int code;
    private final String description;
    private final int httpStatusCode;

    ErrorType(int code, String description, int httpStatusCode) {
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }

}
