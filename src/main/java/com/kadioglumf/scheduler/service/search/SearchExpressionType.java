package com.kadioglumf.scheduler.service.search;

import com.kadioglumf.scheduler.exceptions.SchedulerServiceException;
import com.kadioglumf.scheduler.payload.response.error.ErrorType;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.List;

public enum SearchExpressionType {

    GET_SCHEDULED_JOBS {

        @Override
        public <T> Expression<?> getExpression(Root<T> root, String key) {
            checkSearchKey(key, ExpressionKeyConstants.getScheduledJobKeysKeys());

            switch (key) {
                case ExpressionKeyConstants.CREATION_DATE:
                case ExpressionKeyConstants.ID:
                case ExpressionKeyConstants.NAME:
                case ExpressionKeyConstants.CRON_EXPRESSION:
                case ExpressionKeyConstants.PATH:
                case ExpressionKeyConstants.HTTP_METHOD:
                case ExpressionKeyConstants.APPLICATION_SERVICE_TYPE:
                case ExpressionKeyConstants.EXECUTION_TIME:
                case ExpressionKeyConstants.EXECUTION_START_DATE:
                    return root.get(key);
                case ExpressionKeyConstants.STATUS:
                    return root.get(ExpressionKeyConstants.SCHEDULED_JOB_DETAILS).get(key);
                default:
                    throw new SchedulerServiceException(ErrorType.FILTER_PARAMETER_BLACKLIST_ERROR, "You cannot search with: " + key);
            }
        }
    };


    public abstract <T> Expression<?> getExpression(Root<T> root, String key);


    public void checkSearchKey(String key, List<String> whiteListKeys) {
        if (CollectionUtils.isEmpty(whiteListKeys)) {
            throw new SchedulerServiceException(ErrorType.FORBIDDEN_ERROR);
        }
        if (!whiteListKeys.contains(key)) {
            throw new SchedulerServiceException(ErrorType.FILTER_PARAMETER_BLACKLIST_ERROR, "You cannot search with: " + key);
        }
    }
}
