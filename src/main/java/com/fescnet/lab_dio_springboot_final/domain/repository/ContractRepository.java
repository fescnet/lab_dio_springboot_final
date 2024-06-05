package com.fescnet.lab_dio_springboot_final.domain.repository;

import com.fescnet.lab_dio_springboot_final.domain.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
        @Query("SELECT c FROM tb_contracts c WHERE c.property.id = :propertyId AND c.endDate >= CURRENT_DATE")
        Optional<Contract> findActiveContractByPropertyId(@Param("propertyId") Long propertyId);
}
