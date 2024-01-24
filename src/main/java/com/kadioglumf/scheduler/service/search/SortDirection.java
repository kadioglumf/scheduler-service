package com.kadioglumf.scheduler.service.search;

import com.kadioglumf.scheduler.payload.request.search.SortRequest;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public enum SortDirection {

    ASC {
        public <T> Order build(Root<T> root, CriteriaBuilder cb, SortRequest request, SearchExpressionType searchExpressionType) {
            return cb.asc(searchExpressionType.getExpression(root, request.getKey()));
        }
    },
    DESC {
        public <T> Order build(Root<T> root, CriteriaBuilder cb, SortRequest request, SearchExpressionType searchExpressionType) {
            return cb.desc(searchExpressionType.getExpression(root, request.getKey()));
        }
    };

    public abstract <T> Order build(Root<T> root, CriteriaBuilder cb, SortRequest request, SearchExpressionType searchExpressionType);

}

