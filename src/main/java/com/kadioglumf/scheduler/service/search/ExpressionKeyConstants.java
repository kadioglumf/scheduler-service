package com.kadioglumf.scheduler.service.search;

import java.util.Arrays;
import java.util.List;

public class ExpressionKeyConstants {

    public static final String CREATION_DATE = "creationDate";
    public static final String STATUS = "status";
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String CRON_EXPRESSION = "cronExpression";
    public static final String PATH = "path";
    public static final String HTTP_METHOD = "httpMethod";
    public static final String APPLICATION_SERVICE_TYPE = "applicationServiceType";
    public static final String EXECUTION_TIME = "executionTime";
    public static final String EXECUTION_START_DATE = "executionStartDate";
    public static final String SCHEDULED_JOB_DETAILS = "scheduledJobDetails";




    public static List<String> getScheduledJobKeysKeys() {
        return Arrays.asList(CREATION_DATE, ID, STATUS, NAME, CRON_EXPRESSION,
                PATH, HTTP_METHOD, APPLICATION_SERVICE_TYPE,
                EXECUTION_TIME, EXECUTION_START_DATE);
    }
}
