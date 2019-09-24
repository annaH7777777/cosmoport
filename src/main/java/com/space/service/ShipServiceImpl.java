package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//ADDED
@Repository
@Transactional
@Service("shipService")
public class ShipServiceImpl implements ShipService {
    private ShipRepository shipRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Ship> findAll(Specification<Ship> specification, Pageable pageable) {

        List<Ship> ships = new ArrayList<>();
        for (Ship ship : shipRepository.findAll(specification, pageable)) {
            ships.add(ship);
        }

        return ships;

    }

    @Override
    public Optional<Ship> findById(Long id) {
        return shipRepository.findById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public Long count(Specification<Ship> specification) {
        return shipRepository.count(specification);
    }

    @Override
    public Ship save(Ship ship) {
        ship.calculateRating();
        return shipRepository.save(ship);
    }

    @Override
    public void deleteById(Long id) {
        shipRepository.deleteById(id);
    }

    @Autowired
    public void setShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

}
