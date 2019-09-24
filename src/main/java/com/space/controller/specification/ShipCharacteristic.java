package com.space.controller.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class ShipCharacteristic implements Specification<Ship> {
    private SearchCriteria criteria;

    public ShipCharacteristic(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if (criteria.getOperation().equalsIgnoreCase(">=")) {
            if (root.get(criteria.getKey()).getJavaType() == Date.class) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), toDateValue(criteria.getValue()));
            } else {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            }
        }
        else if (criteria.getOperation().equalsIgnoreCase("<=")) {
            if (root.get(criteria.getKey()).getJavaType() == Date.class) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), toDateValue(criteria.getValue()));
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            }
        }
        else if (criteria.getOperation().equalsIgnoreCase("=")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else if (root.get(criteria.getKey()).getJavaType() == Date.class) {
                return criteriaBuilder.equal(root.get(criteria.getKey()), toDateValue(criteria.getValue()));
            } else {
                return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }

        return null;

    }

    private Date toDateValue(Object value) throws IllegalArgumentException {

        Class clazz = value.getClass();

        if (clazz == Date.class) {
            return (Date) value;
        } else if (clazz == Integer.class || clazz == Long.class) {
            return new Date((Long) value);
        } else {
            throw new IllegalArgumentException("Cannot convert value to Date type");
        }

    }
}
