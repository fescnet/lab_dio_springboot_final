package com.fescnet.lab_dio_springboot_final.domain.repository;

import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    @Query("SELECT p FROM tb_properties p WHERE p.id NOT IN (SELECT c.property.id FROM tb_contracts c WHERE c.endDate >= CURRENT_DATE)")
    List<Property> findAllWithoutActiveContracts();
}
