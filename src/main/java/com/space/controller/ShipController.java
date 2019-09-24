package com.space.controller;

import com.space.controller.specification.ShipCharacteristicBuilder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

//ADDED
@Controller
@RequestMapping(value = "/rest/ships")
public class ShipController {
    @Autowired
    ShipService shipService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Ship> findAll(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "order", required = false) ShipOrder order,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize
    ) {

        ShipCharacteristicBuilder builder = new ShipCharacteristicBuilder();
        builder.and("name", "=", name)
                .and("planet", "=", planet)
                .and("shipType", "=", shipType)
                .and("isUsed", "=", isUsed)

                .and("prodDate", ">=", after)
                .and("prodDate", "<=", before)

                .and("speed", ">=", minSpeed)
                .and("speed", "<=", maxSpeed)

                .and("crewSize", ">=", minCrewSize)
                .and("crewSize", "<=", maxCrewSize)

                .and("rating", ">=", minRating)
                .and("rating", "<=", maxRating)
        ;

        Specification<Ship> spec = builder.build();
        Sort sort = Sort.unsorted();
        if (order != null) {
            sort = Sort.by(order.getFieldName());
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return shipService.findAll(spec, pageable);

    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseBody
    public Long getShipsCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating
    ) {

        ShipCharacteristicBuilder builder = new ShipCharacteristicBuilder();
        builder.and("name", "=", name)
                .and("planet", "=", planet)
                .and("shipType", "=", shipType)
                .and("isUsed", "=", isUsed)

                .and("prodDate", ">=", after)
                .and("prodDate", "<=", before)

                .and("speed", ">=", minSpeed)
                .and("speed", "<=", maxSpeed)

                .and("crewSize", ">=", minCrewSize)
                .and("crewSize", "<=", maxCrewSize)

                .and("rating", ">=", minRating)
                .and("rating", "<=", maxRating)
        ;
        Specification<Ship> spec = builder.build();

        return shipService.count(spec);

    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) throws ParseException {

        if (!ship.checkFields(false)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (ship.getUsed() == null) {
            ship.setUsed(false);
        }

        return new ResponseEntity(shipService.save(ship), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Ship> findById(@PathVariable Long id) {

        if (id <= 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Optional<Ship> shipOptional = shipService.findById(id);
        if (!shipOptional.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(shipOptional.get(), HttpStatus.OK);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<Ship> updateShip(@PathVariable Long id, @RequestBody Ship ship) throws ParseException {

        if (id <= 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (!ship.checkFields(true)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Optional<Ship> shipOptional = shipService.findById(id);
        if (!shipOptional.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Ship currentShip = shipOptional.get();
        if (ship.copyFieldsTo(currentShip)) {
            currentShip = shipService.save(currentShip);
        }

        return new ResponseEntity(currentShip, HttpStatus.OK);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteShip(@PathVariable Long id) {

        if (id <= 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Optional<Ship> shipOptional = shipService.findById(id);
        if (!shipOptional.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        shipService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
