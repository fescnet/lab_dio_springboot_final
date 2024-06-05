package com.fescnet.lab_dio_springboot_final.controller.dto;

import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import com.fescnet.lab_dio_springboot_final.domain.model.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Represents the request data used to create or update properties
 */
@Getter @Setter
public class PropertyRequestDto{

    protected Long id;
    protected String street;
    protected String number;
    protected String neighborhood;
    protected String city;
    protected String state;
    protected String zipCode;
    protected String landmark;
    protected BigDecimal rentalValue;
    // It's easier for the client to send only the ownerId instead of an entity
    protected Long ownerId;

    public PropertyRequestDto(){}

    public Property toModel() {
        Property property = new Property();
        property.setId(this.id);
        property.setStreet(this.street);
        property.setNumber(this.number);
        property.setNeighborhood(this.neighborhood);
        property.setCity(this.city);
        property.setState(this.state);
        property.setZipCode(this.zipCode);
        property.setLandmark(this.landmark);
        property.setRentalValue(this.rentalValue);

        // the owner id is sent inside a new User object
        User owner = new User();
        owner.setId(this.getOwnerId());
        property.setOwner(owner);

        return property;
    }
}