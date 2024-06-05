package com.fescnet.lab_dio_springboot_final.domain.repository;

import com.fescnet.lab_dio_springboot_final.domain.model.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
}
