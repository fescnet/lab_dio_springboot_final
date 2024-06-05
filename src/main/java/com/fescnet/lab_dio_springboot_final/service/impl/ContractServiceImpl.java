package com.fescnet.lab_dio_springboot_final.service.impl;

import com.fescnet.lab_dio_springboot_final.domain.model.Contract;
import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import com.fescnet.lab_dio_springboot_final.domain.model.Proposal;
import com.fescnet.lab_dio_springboot_final.domain.repository.ContractRepository;
import com.fescnet.lab_dio_springboot_final.domain.repository.ProposalRepository;
import com.fescnet.lab_dio_springboot_final.service.ContractService;
import com.fescnet.lab_dio_springboot_final.service.ProposalService;
import com.fescnet.lab_dio_springboot_final.service.exception.BusinessException;
import com.fescnet.lab_dio_springboot_final.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ProposalRepository proposalRepository;

    public ContractServiceImpl(ContractRepository contractRepository, ProposalRepository proposalRepository) {
        this.contractRepository = contractRepository;
        this.proposalRepository = proposalRepository;
    }

    @Transactional(readOnly = true)
    public Contract findById(Long id) {
        return this.contractRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Contract create(Contract contractToCreate) {
        contractToCreate.setId(null); // avoid updating a record using this method
        return this.contractRepository.save(contractToCreate);
    }

    @Override
    @Transactional
    public Contract create(Long proposalId) {
        Proposal proposal = proposalRepository.findById(proposalId).orElseThrow(NotFoundException::new);
        Property property = proposal.getProperty();

        // Business rule: properties must not have more than one active contract
        Optional<Contract> contractOptional = contractRepository.findActiveContractByPropertyId(property.getId());
        if(contractOptional.isPresent()){ throw new BusinessException("The property already have an active rental contract"); }

        Contract contract = new Contract();
        contract.setRentalValue(proposal.getValueProposed());
        contract.setProposal(proposal);
        contract.setOwner(property.getOwner());
        contract.setProperty(property);
        contract.setTenant(proposal.getTenant());
        contract.setEndDate(proposal.getEndDate());
        contract.setStartDate(proposal.getStartDate());

        return contractRepository.save(contract);
    }


    @Override
    public Optional<Contract> findActiveContractByPropertyId(Long propertyId) {
        return contractRepository.findActiveContractByPropertyId(propertyId);
    }
}

