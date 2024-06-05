package com.fescnet.lab_dio_springboot_final.service.impl;

import com.fescnet.lab_dio_springboot_final.domain.model.Contract;
import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import com.fescnet.lab_dio_springboot_final.domain.model.User;
import com.fescnet.lab_dio_springboot_final.domain.repository.PropertyRepository;
import com.fescnet.lab_dio_springboot_final.service.ContractService;
import com.fescnet.lab_dio_springboot_final.service.PropertyService;
import com.fescnet.lab_dio_springboot_final.service.UserService;
import com.fescnet.lab_dio_springboot_final.service.exception.ActiveContractException;
import com.fescnet.lab_dio_springboot_final.service.exception.BusinessException;
import com.fescnet.lab_dio_springboot_final.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private  final UserService userService;
    private  final ContractService contractService;

    public PropertyServiceImpl(PropertyRepository propertyRepository, UserService userService, ContractService contractService) {
        this.propertyRepository = propertyRepository;
        this.userService = userService;
        this.contractService = contractService;
    }

    @Transactional(readOnly = true)
    public Property findById(Long id) {
        return this.propertyRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Property create(Property propertyToCreate) {
        propertyToCreate.setId(null); // avoid updating a record using this method
        Property property = this.propertyRepository.save(propertyToCreate);
        property.setOwner(userService.findById(property.getOwner().getId()));
        return property;
    }

    @Transactional
    public Property update(Long id, Property propertyToUpdate) {
        Property dbProperty = this.findById(id);
        if (!dbProperty.getId().equals(propertyToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same.");
        }

        Long newOwnerID = propertyToUpdate.getOwner().getId();
        Long currentOwnerId = dbProperty.getId();

        if(!newOwnerID.equals(currentOwnerId)){
            throwIfPropertyHasActiveContract(id);
        }

        User owner = userService.findById(propertyToUpdate.getOwner().getId());
        dbProperty.setStreet(propertyToUpdate.getStreet());
        dbProperty.setNumber(propertyToUpdate.getNumber());
        dbProperty.setNumber(propertyToUpdate.getNumber());
        dbProperty.setCity(propertyToUpdate.getCity());
        dbProperty.setState(propertyToUpdate.getState());
        dbProperty.setZipCode(propertyToUpdate.getZipCode());
        dbProperty.setLandmark(propertyToUpdate.getLandmark());
        dbProperty.setRentalValue(propertyToUpdate.getRentalValue());
        dbProperty.setOwner(owner);
        return this.propertyRepository.save(dbProperty);
    }

    private void throwIfPropertyHasActiveContract(Long propertyId){
        Optional<Contract> contractOptional = contractService.findActiveContractByPropertyId(propertyId);
        if(contractOptional.isPresent()){
            throw new ActiveContractException("You can not change the ownership because you have an active rental contract");
        }
    }

    @Transactional
    public void delete(Long id) {
        Property dbProperty = this.findById(id);
        Optional<Contract> contractOptional = contractService.findActiveContractByPropertyId(id);
        if(contractOptional.isPresent()){ throw new BusinessException("Property with active contracts cannot be deleted"); }
        this.propertyRepository.delete(dbProperty);
    }

    @Override
    public List<Property> findPropertiesWithoutActiveContracts() {
        return propertyRepository.findAllWithoutActiveContracts();
    }
}

