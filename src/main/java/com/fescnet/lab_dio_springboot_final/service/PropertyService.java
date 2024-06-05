package com.fescnet.lab_dio_springboot_final.service;

import com.fescnet.lab_dio_springboot_final.domain.model.Property;

import java.util.List;

public interface PropertyService {

    Property findById(Long id);
    Property create(Property entity);
    Property update(Long id, Property entity);
    void delete(Long id);

    public List<Property> findPropertiesWithoutActiveContracts();
}
