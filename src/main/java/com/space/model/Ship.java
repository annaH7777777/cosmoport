package com.space.model;

import org.hibernate.annotations.Type;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

//ADDED
@Entity
@Table(name = "ship")
public class Ship implements Serializable {
    //MIN_PROD_DATE 00:00 01.01.2800
    private static final Date MIN_PROD_DATE = new Date(26192239200000L);

    //MAX_PROD_DATE 00:00 01.01.3019
    private static final Date MAX_PROD_DATE = new Date(33103202400000L);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "planet")
    private String planet;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "shipType", length = 9)
    private ShipType shipType;

    @Column(name = "prodDate")
    private Date prodDate;

    @Column(name = "isUsed")
    private Boolean isUsed;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "crewSize")
    private Integer crewSize;

    @Column(name = "rating")
    private Double rating;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = (double) Math.round(speed * 100) / 100;;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public boolean checkFields(boolean ignoreNull) {

        if (!ignoreNull &&
                (name == null || planet == null || shipType == null || prodDate == null || speed == null || crewSize == null)) {
            return false;
        }

        if (name != null && (name.isEmpty() || name.length() > 50)) {
            return false;
        }

        if (planet != null && (planet.isEmpty() || planet.length() > 50)) {
            return false;
        }

        if (prodDate != null && (prodDate.before(MIN_PROD_DATE) || prodDate.after(MAX_PROD_DATE))) {
            return false;
        }

        if (speed != null && (speed < 0.01 || speed > 0.99)) {
            return false;
        }

        if (crewSize != null && (crewSize < 0 || crewSize > 9999)) {
            return false;
        }

        return true;

    }

    public void calculateRating() {

        this.rating = 80 * speed * (isUsed ? 0.5 : 1) / (MAX_PROD_DATE.getYear() - prodDate.getYear() + 1);
        this.rating = (double) Math.round(this.rating * 100) / 100;

    }

    public boolean copyFieldsTo(Ship ship) {

        int countOfFields = 0;

        if (name != null) {
            ship.setName(name);
            countOfFields++;
        }
        if (planet != null) {
            ship.setPlanet(planet);
            countOfFields++;
        }
        if (shipType != null) {
            ship.setShipType(shipType);
            countOfFields++;
        }
        if (prodDate != null) {
            ship.setProdDate(prodDate);
            countOfFields++;
        }
        if (speed != null) {
            ship.setSpeed(speed);
            countOfFields++;
        }
        if (crewSize != null) {
            ship.setCrewSize(crewSize);
            countOfFields++;
        }

        return countOfFields > 0;

    }
}
