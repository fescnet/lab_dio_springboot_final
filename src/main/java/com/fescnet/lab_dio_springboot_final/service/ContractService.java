package com.fescnet.lab_dio_springboot_final.service;

import com.fescnet.lab_dio_springboot_final.domain.model.Contract;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContractService {

    Contract findById(Long id);
    Contract create(Contract entity);
    Contract create(Long proposalId);
    public Optional<Contract> findActiveContractByPropertyId(@Param("propertyId") Long propertyId);
}
