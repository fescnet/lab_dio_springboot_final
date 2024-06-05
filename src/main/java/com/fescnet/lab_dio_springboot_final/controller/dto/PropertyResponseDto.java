package com.fescnet.lab_dio_springboot_final.controller.dto;

import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import lombok.Getter;
import lombok.Setter;

/**
 * represents a property response object
 */
@Getter @Setter
public class PropertyResponseDto extends PropertyRequestDto {

    private UserResponseDto owner;

    public PropertyResponseDto(Property property) {
        super();
        this.id = property.getId();
        this.street = property.getStreet();
        this.number = property.getNumber();
        this.neighborhood = property.getNeighborhood();
        this.city = property.getCity();
        this.state = property.getState();
        this.zipCode = property.getZipCode();
        this.landmark = property.getLandmark();
        this.rentalValue = property.getRentalValue();
        this.ownerId = property.getOwner().getId();
        // I do not need to worry about null variables because the field is mandatory
        this.owner = new UserResponseDto(property.getOwner());
    }
}