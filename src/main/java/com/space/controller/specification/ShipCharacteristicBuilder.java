package com.space.controller.specification;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShipCharacteristicBuilder {
    private final List<SearchCriteria> parameters;

    public ShipCharacteristicBuilder() {
        parameters = new ArrayList<>();
    }

    public ShipCharacteristicBuilder with(String key, String operation, Object value, boolean orPredicate) {
        if (value == null) {
            return this;
        }
        parameters.add(new SearchCriteria(key, operation, value, orPredicate));
        return this;
    }

    public ShipCharacteristicBuilder and(String key, String operation, Object value) {
        return with(key, operation, value, false);
    }

    public ShipCharacteristicBuilder or(String key, String operation, Object value) {
        return with(key, operation, value, true);
    }

    public Specification<Ship> build() {
        if (parameters.size() == 0) {
            return null;
        }

        List<Specification> specs = parameters.stream().map(ShipCharacteristic::new).collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < parameters.size(); i++) {
            result = parameters.get(i).isOrPredicate()
                    ? Specification.where(result).or(specs.get(i))
                    : Specification.where(result).and(specs.get(i));
        }

        return result;
    }

}
