package com.fescnet.lab_dio_springboot_final.util;

import com.fescnet.lab_dio_springboot_final.domain.model.Contract;
import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import com.fescnet.lab_dio_springboot_final.domain.model.Proposal;
import com.fescnet.lab_dio_springboot_final.domain.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PojoProvider {

    public static User createValidUser(){
        User user = new User();
        user.setName("Fernando");
        user.setEmail("test@gmail.com");
        user.setPassword("123456");
        user.setPhone("+5521988849409");
        return user;
    }

    public static Property createValidProperty(User owner){
        Property property = new Property();
        property.setStreet("Av. Paulista");
        property.setNumber("143");
        property.setNeighborhood("Brooklyn");
        property.setLandmark("Casa Blanca Hotel");
        property.setZipCode("11205-050");
        property.setCity("São Paulo");
        property.setState("SP");
        property.setRentalValue(BigDecimal.valueOf(3000));
		property.setOwner(owner);
        return property;
    }

    public static LocalDate today(){
        return LocalDate.now();
    }

    public static LocalDate tomorow(){
        return LocalDate.now().plusDays(1);
    }

    public static LocalDate moreThanOneYearFromToday(){
        return LocalDate.now().plusDays(400);
    }

    public static LocalDate moreThanThreeYearsFromToday(){
        return LocalDate.now().plusDays(1200);
    }

    public static LocalDate twentyDaysFromToday(){
        return LocalDate.now().plusDays(20);
    }

    public static Proposal createValidProposal(User tenant, Property property){
        Proposal proposal = new Proposal();
        proposal.setValueProposed(BigDecimal.valueOf(3000));
        proposal.setStartDate(tomorow());
        proposal.setEndDate(moreThanOneYearFromToday());
        proposal.setProperty(property);
        proposal.setTenant(tenant);
        return proposal;
    }

    public static Contract createValidContract(User tenant, Property property, User owner, Proposal proposal){
        Contract contract = new Contract();
        contract.setStartDate(tomorow());
        contract.setEndDate(moreThanOneYearFromToday());
        contract.setProperty(property);
        contract.setTenant(tenant);
        contract.setOwner(owner);
        contract.setProposal(proposal);
        contract.setRentalValue(BigDecimal.valueOf(3000));
        return contract;
    }
}
