package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

//ADDED
public interface ShipService {
    List<Ship> findAll(Specification<Ship> specification, Pageable pageable);
    Optional<Ship> findById(Long id);
    Long count(Specification<Ship> specification);
    Ship save(Ship ship);
    void deleteById(Long id);
}
