package com.kadioglumf.scheduler.service.search;

import com.kadioglumf.scheduler.payload.request.search.ConditionRequest;
import com.kadioglumf.scheduler.payload.request.search.FilterRequest;
import com.kadioglumf.scheduler.payload.request.search.Operator;
import com.kadioglumf.scheduler.payload.response.error.ErrorType;
import com.kadioglumf.scheduler.exceptions.SchedulerServiceException;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public enum FilterType {

    EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request,
                                   Predicate predicate, ConditionRequest condition,
                                   SearchExpressionType searchExpressionType) {

            Object value = request.getFieldType().parse(validateValueAndReturn(condition.getValue()).toString());
            Expression<?> key = searchExpressionType.getExpression(root, request.getKey());
            value = convertToEnumIfEnum(value, key);
            if (predicate == null) {
                return cb.equal(key, value);
            }
            if (Operator.OR.equals(request.getOperator())) {
                return cb.or(cb.equal(key, value), predicate);
            }
            return cb.and(cb.equal(key, value), predicate);
        }
    },

    NOT_EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request,
                                   Predicate predicate, ConditionRequest condition,
                                   SearchExpressionType searchExpressionType) {

            Object value = request.getFieldType().parse(validateValueAndReturn(condition.getValue()).toString());
            Expression<?> key = searchExpressionType.getExpression(root, request.getKey());
            value = convertToEnumIfEnum(value, key);
            if (predicate == null) {
                return cb.notEqual(key, value);
            }
            if (Operator.OR.equals(request.getOperator())) {
                return cb.or(cb.notEqual(key, value), predicate);
            }
            return cb.and(cb.notEqual(key, value), predicate);
        }
    },

    CONTAINS {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request,
                                   Predicate predicate, ConditionRequest condition,
                                   SearchExpressionType searchExpressionType) {

            if (FieldType.STRING.equals(request.getFieldType())) {
                String value = validateValueAndReturn(condition.getValue()).toString().toUpperCase();
                Expression<String> key = (Expression<String>) searchExpressionType.getExpression(root, request.getKey());
                if (predicate == null) {
                    return cb.like(cb.upper(key), "%" + value + "%");
                }
                if (Operator.OR.equals(request.getOperator())) {
                    return cb.or(cb.like(cb.upper(key), "%" + value + "%"), predicate);
                }
                return cb.and(cb.like(cb.upper(key), "%" + value + "%"), predicate);
            }
            return null;
        }
    },

    ENDS_WITH {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request,
                                   Predicate predicate, ConditionRequest condition,
                                   SearchExpressionType searchExpressionType) {

            if (FieldType.STRING.equals(request.getFieldType())) {
                String value = validateValueAndReturn(condition.getValue()).toString().toUpperCase();
                Expression<String> key = (Expression<String>) searchExpressionType.getExpression(root, request.getKey());
                if (predicate == null) {
                    return cb.like(cb.upper(key), "%" + value);
                }
                if (Operator.OR.equals(request.getOperator())) {
                    return cb.or(cb.like(cb.upper(key), "%" + value), predicate);
                }
                return cb.and(cb.like(cb.upper(key), "%" + value), predicate);
            }
            return null;
        }
    },

    STARTS_WITH {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request,
                                   Predicate predicate, ConditionRequest condition,
                                   SearchExpressionType searchExpressionType) {

            if (FieldType.STRING.equals(request.getFieldType())) {
                String value = validateValueAndReturn(condition.getValue()).toString().toUpperCase();
                Expression<String> key = (Expression<String>) searchExpressionType.getExpression(root, request.getKey());
                if (predicate == null) {
                    return cb.like(cb.upper(key), value + "%");
                }
                if (Operator.OR.equals(request.getOperator())) {
                    return cb.or(cb.like(cb.upper(key), value + "%"), predicate);
                }
                return cb.and(cb.like(cb.upper(key),value + "%"), predicate);
            }
            return null;
        }
    },

    IN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request,
                                   Predicate predicate, ConditionRequest condition,
                                   SearchExpressionType searchExpressionType) {

            validateValueAndReturn(condition.getValues());
            List<Object> values = condition.getValues();
            Expression<String> key = (Expression<String>) searchExpressionType.getExpression(root, request.getKey());
            values = convertToEnumIfEnum(values, key);
            CriteriaBuilder.In<Object> inClause = cb.in(key);
            for (Object value : values) {
                inClause.value(value);
            }
            if (predicate == null) {
                return inClause;
            }
            if (Operator.OR.equals(request.getOperator())) {
                return cb.or(inClause, predicate);
            }
            return cb.and(inClause, predicate);
        }
    },

    GRATER_THAN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request,
                                   Predicate predicate, ConditionRequest condition,
                                   SearchExpressionType searchExpressionType) {

            Object value = request.getFieldType().parse(validateValueAndReturn(condition.getValue()).toString());

            if (FieldType.STRING.equals(request.getFieldType())) {
                Expression<String> key = (Expression<String>) searchExpressionType.getExpression(root, request.getKey());
                if (predicate == null) {
                    return cb.greaterThan(cb.upper(key), value.toString());
                }
                if (Operator.OR.equals(request.getOperator())) {
                    return cb.or(cb.greaterThan(cb.upper(key), value.toString()), predicate);
                }
                return cb.and(cb.greaterThan(cb.upper(key), value.toString()), predicate);
            }
            else if (FieldType.DATE.equals(request.getFieldType())) {
                if (value instanceof LocalDateTime) {
                    LocalDateTime startDate = (LocalDateTime) value;
                    Expression<LocalDateTime> key = (Expression<LocalDateTime>) searchExpressionType.getExpression(root, request.getKey());

                    if (predicate == null) {
                        return cb.and(cb.greaterThan(key, startDate));
                    }
                    if (Operator.OR.equals(request.getOperator())) {
                        return cb.or(cb.and(cb.greaterThan(key, startDate), predicate));
                    }
                    return cb.and(cb.and(cb.greaterThan(key, startDate), predicate));
                }
                else if (value instanceof LocalDate) {
                    LocalDate startDate = (LocalDate) value;
                    Expression<LocalDate> key = (Expression<LocalDate>) searchExpressionType.getExpression(root, request.getKey());

                    if (predicate == null) {
                        return cb.and(cb.greaterThan(key, startDate));
                    }
                    if (Operator.OR.equals(request.getOperator())) {
                        return cb.or(cb.and(cb.greaterThan(key, startDate), predicate));
                    }
                    return cb.and(cb.and(cb.greaterThan(key, startDate), predicate));
                }
            }
            else if (FieldType.NUMBER.equals(request.getFieldType())) {
                Number numberValue = (Number) value;
                Expression<Number> key = (Expression<Number>) searchExpressionType.getExpression(root, request.getKey());
                if (predicate == null) {
                    return cb.gt(key, numberValue);
                }
                if (Operator.OR.equals(request.getOperator())) {
                    return cb.or(cb.gt(key, numberValue), predicate);
                }
                return cb.and(cb.gt(key, numberValue), predicate);
            }
            return null;
        }
    },

    GRATER_THAN_OR_EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request,
                                   Predicate predicate, ConditionRequest condition,
                                   SearchExpressionType searchExpressionType) {

            Object value = request.getFieldType().parse(validateValueAndReturn(condition.getValue()).toString());

            if (FieldType.STRING.equals(request.getFieldType())) {
                Expression<String> key = (Expression<String>) searchExpressionType.getExpression(root, request.getKey());
                if (predicate == null) {
                    return cb.greaterThanOrEqualTo(cb.upper(key), value.toString());
                }
                if (Operator.OR.equals(request.getOperator())) {
                    return cb.or(cb.greaterThanOrEqualTo(cb.upper(key), value.toString()), predicate);
                }
                return cb.and(cb.greaterThanOrEqualTo(cb.upper(key), value.toString()), predicate);
            }
            else if (FieldType.DATE.equals(request.getFieldType())) {
                if (value instanceof LocalDateTime) {
                    LocalDateTime startDate = (LocalDateTime) value;
                    Expression<LocalDateTime> key = (Expression<LocalDateTime>) searchExpressionType.getExpression(root, request.getKey());
                    if (predicate == null) {
                        return cb.and(cb.greaterThanOrEqualTo(key, startDate));
                    }
                    if (Operator.OR.equals(request.getOperator())) {
                        return cb.or(cb.and(cb.greaterThanOrEqualTo(key, startDate), predicate));
                    }
                    return cb.and(cb.and(cb.greaterThanOrEqualTo(key, startDate), predicate));
                }
                else if (value instanceof LocalDate) {
                    LocalDate startDate = (LocalDate) value;
                    Expression<LocalDate> key = (Expression<LocalDate>) searchExpressionType.getExpression(root, request.getKey());
                    if (predicate == null) {
                        return cb.and(cb.greaterThanOrEqualTo(key, startDate));
                    }
                    if (Operator.OR.equals(request.getOperator())) {
                        return cb.or(cb.and(cb.greaterThanOrEqualTo(key, startDate), predicate));
                    }
                    return cb.and(cb.and(cb.greaterThanOrEqualTo(key, startDate), predicate));
                }

            }
            else if (FieldType.NUMBER.equals(request.getFieldType())) {
                Number numberValue = (Number) value;
                Expression<Number> key = (Expression<Number>) searchExpressionType.getExpression(root, request.getKey());
                if (predicate == null) {
                    return cb.ge(key, numberValue);
                }
                if (Operator.OR.equals(request.getOperator())) {
                    return cb.or(cb.ge(key, numberValue), predicate);
                }
                return cb.and(cb.ge(key, numberValue), predicate);
            }
            return null;
        }
    },

    LESS_THAN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request,
                                   Predicate predicate, ConditionRequest condition,
                                   SearchExpressionType searchExpressionType) {

            Object value = request.getFieldType().parse(validateValueAndReturn(condition.getValue()).toString());

            if (FieldType.STRING.equals(request.getFieldType())) {
                Expression<String> key = (Expression<String>) searchExpressionType.getExpression(root, request.getKey());
                if (predicate == null) {
                    return cb.lessThan(cb.upper(key), value.toString());
                }
                if (Operator.OR.equals(request.getOperator())) {
                    return cb.or(cb.lessThan(cb.upper(key), value.toString()), predicate);
                }
                return cb.and(cb.lessThan(cb.upper(key), value.toString()), predicate);
            }
            else if (FieldType.DATE.equals(request.getFieldType())) {
                if (value instanceof LocalDateTime) {
                    LocalDateTime startDate = (LocalDateTime) value;
                    Expression<LocalDateTime> key = (Expression<LocalDateTime>) searchExpressionType.getExpression(root, request.getKey());
                    if (predicate == null) {
                        return cb.and(cb.lessThan(key, startDate));
                    }
                    if (Operator.OR.equals(request.getOperator())) {
                        return cb.or(cb.and(cb.lessThan(key, startDate), predicate));
                    }
                    return cb.and(cb.and(cb.lessThan(key, startDate), predicate));
                }
                else if (value instanceof LocalDate) {
                    LocalDate startDate = (LocalDate) value;
                    Expression<LocalDate> key = (Expression<LocalDate>) searchExpressionType.getExpression(root, request.getKey());
                    if (predicate == null) {
                        return cb.and(cb.lessThan(key, startDate));
                    }
                    if (Operator.OR.equals(request.getOperator())) {
                        return cb.or(cb.and(cb.lessThan(key, startDate), predicate));
                    }
                    return cb.and(cb.and(cb.lessThan(key, startDate), predicate));
                }
            }
            else if (FieldType.NUMBER.equals(request.getFieldType())) {
                Number numberValue = (Number) value;
                Expression<Number> key = (Expression<Number>) searchExpressionType.getExpression(root, request.getKey());
                if (predicate == null) {
                    return cb.lt(key, numberValue);
                }
                if (Operator.OR.equals(request.getOperator())) {
                    return cb.or(cb.lt(key, numberValue), predicate);
                }
                return cb.and(cb.lt(key, numberValue), predicate);
            }
            return null;
        }
    },

    LESS_THAN_OR_EQUAL {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request,
                                   Predicate predicate, ConditionRequest condition,
                                   SearchExpressionType searchExpressionType) {

            Object value = request.getFieldType().parse(validateValueAndReturn(condition.getValue()).toString());

            if (FieldType.STRING.equals(request.getFieldType())) {
                Expression<String> key = (Expression<String>) searchExpressionType.getExpression(root, request.getKey());
                if (predicate == null) {
                    return cb.lessThanOrEqualTo(cb.upper(key), value.toString());
                }
                if (Operator.OR.equals(request.getOperator())) {
                    return cb.or(cb.lessThanOrEqualTo(cb.upper(key), value.toString()), predicate);
                }
                return cb.and(cb.lessThanOrEqualTo(cb.upper(key), value.toString()), predicate);
            }
            else if (FieldType.DATE.equals(request.getFieldType())) {
                if (value instanceof LocalDateTime) {
                    LocalDateTime startDate = (LocalDateTime) value;
                    Expression<LocalDateTime> key = (Expression<LocalDateTime>) searchExpressionType.getExpression(root, request.getKey());
                    if (predicate == null) {
                        return cb.and(cb.lessThanOrEqualTo(key, startDate));
                    }
                    if (Operator.OR.equals(request.getOperator())) {
                        return cb.or(cb.and(cb.lessThanOrEqualTo(key, startDate), predicate));
                    }
                    return cb.and(cb.and(cb.lessThanOrEqualTo(key, startDate), predicate));
                }
                else if (value instanceof LocalDate) {
                    LocalDate startDate = (LocalDate) value;
                    Expression<LocalDate> key = (Expression<LocalDate>) searchExpressionType.getExpression(root, request.getKey());
                    if (predicate == null) {
                        return cb.and(cb.lessThanOrEqualTo(key, startDate));
                    }
                    if (Operator.OR.equals(request.getOperator())) {
                        return cb.or(cb.and(cb.lessThanOrEqualTo(key, startDate), predicate));
                    }
                    return cb.and(cb.and(cb.lessThanOrEqualTo(key, startDate), predicate));
                }
            }
            else if (FieldType.NUMBER.equals(request.getFieldType())) {
                Number numberValue = (Number) value;
                Expression<Number> key = (Expression<Number>) searchExpressionType.getExpression(root, request.getKey());
                if (predicate == null) {
                    return cb.le(key, numberValue);
                }
                if (Operator.OR.equals(request.getOperator())) {
                    return cb.or(cb.le(key, numberValue), predicate);
                }
                return cb.and(cb.le(key, numberValue), predicate);
            }
            return null;
        }
    },


    BETWEEN {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request,
                                   Predicate predicate, ConditionRequest condition,
                                   SearchExpressionType searchExpressionType) {

            Object value = request.getFieldType().parse(validateValueAndReturn(condition.getValue()).toString());
            Object valueTo = request.getFieldType().parse(validateValueAndReturn(condition.getValueTo()).toString());

            if (FieldType.DATE.equals(request.getFieldType())) {
                if (value instanceof LocalDateTime) {
                    LocalDateTime startDate = (LocalDateTime) value;
                    LocalDateTime endDate = (LocalDateTime) valueTo;
                    Expression<LocalDateTime> key = (Expression<LocalDateTime>) searchExpressionType.getExpression(root, request.getKey());
                    if (predicate == null) {
                        return cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate));
                    }
                    if (Operator.OR.equals(request.getOperator())) {
                        return cb.or(cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate), predicate));
                    }
                    return cb.and(cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate), predicate));
                }
                else if (value instanceof LocalDate) {
                    LocalDate startDate = (LocalDate) value;
                    LocalDate endDate = (LocalDate) valueTo;
                    Expression<LocalDate> key = (Expression<LocalDate>) searchExpressionType.getExpression(root, request.getKey());
                    if (predicate == null) {
                        return cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate));
                    }
                    if (Operator.OR.equals(request.getOperator())) {
                        return cb.or(cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate), predicate));
                    }
                    return cb.and(cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate), predicate));
                }

            }
            else if (FieldType.NUMBER.equals(request.getFieldType())) {
                Number start = (Number) value;
                Number end = (Number) valueTo;
                Expression<Number> key = (Expression<Number>) searchExpressionType.getExpression(root, request.getKey());
                if (predicate == null) {
                    return cb.and(cb.ge(key, start), cb.le(key, end));
                }
                if (Operator.OR.equals(request.getOperator())) {
                    return cb.or(cb.and(cb.ge(key, start), cb.le(key, end)), predicate);
                }
                return cb.and(cb.and(cb.ge(key, start), cb.le(key, end)), predicate);
            }
            return null;
        }
    };

    public Object validateValueAndReturn(Object obj) {
        if (obj == null) {
            throw new SchedulerServiceException(ErrorType.FILTER_PARAMETER_CANNOT_NULL_ERROR);
        }
        return obj;
    }

    public Object convertToEnumIfEnum(Object value, Expression<?> expression) {
        if (expression.getJavaType().isEnum()) {

            for (Object obj : expression.getJavaType().getEnumConstants()) {

                for (Field field : expression.getJavaType().getFields()) {
                    if (obj.toString().equals(field.getName())) {
                        if (field.getAnnotations() != null && field.getAnnotations().length > 0 && field.getAnnotations()[0] instanceof JsonProperty) {
                            JsonProperty jsonProperty = (JsonProperty) field.getAnnotations()[0];
                            if (value.equals(jsonProperty.value())) {
                                return obj;
                            }
                        }
                        else if (value.equals(obj.toString())) {
                            return obj;
                        }
                    }
                }
            }
        }

        return value;
    }

    public List<Object> convertToEnumIfEnum(List<Object> values, Expression<?> expression) {
        List<Object> enums = new ArrayList<>();

        for (Object value : values) {
            if (expression.getJavaType().getEnumConstants() != null) {
                for (Object key : expression.getJavaType().getEnumConstants()) {
                    if (value.equals(key.toString())) {
                        enums.add(key);
                    }
                }
            }
        }
        if (!CollectionUtils.isEmpty(enums)) {
            return enums;
        }

        return values;
    }

    public abstract <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request,
                                        Predicate predicate, ConditionRequest condition,
                                        SearchExpressionType searchExpressionType);
}
