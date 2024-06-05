package com.fescnet.lab_dio_springboot_final.service.impl;

import com.fescnet.lab_dio_springboot_final.domain.model.Contract;
import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import com.fescnet.lab_dio_springboot_final.domain.model.Proposal;
import com.fescnet.lab_dio_springboot_final.domain.repository.ProposalRepository;
import com.fescnet.lab_dio_springboot_final.service.ContractService;
import com.fescnet.lab_dio_springboot_final.service.PropertyService;
import com.fescnet.lab_dio_springboot_final.service.ProposalService;
import com.fescnet.lab_dio_springboot_final.service.exception.BusinessException;
import com.fescnet.lab_dio_springboot_final.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProposalServiceImpl implements ProposalService {

    private final ProposalRepository proposalRepository;
    private final PropertyService propertyService;
    private final ContractService contractService;

    @Autowired
    public ProposalServiceImpl(ProposalRepository proposalRepository, PropertyService propertyService, ContractService contractService) {
        this.proposalRepository = proposalRepository;
        this.propertyService = propertyService;
        this.contractService = contractService;
    }

    @Transactional
    @Override
    public Proposal create(Proposal proposal) {
        validateProposal(proposal);
        // Business rule: A user can create proposals for properties they don't own
        if(userIsTheOwner(proposal.getTenant().getId(), proposal.getProperty().getId())){
            throw new BusinessException("Users cannot make proposals for their own properties");
        }

        // Business rule: It's not possible to create proposals for properties with an active contract
        Optional<Contract> contractOptional =  contractService.findActiveContractByPropertyId(proposal.getProperty().getId());
        if(contractOptional.isPresent()){ throw new BusinessException("It's not possible to create a proposal because the property has an active contract"); }

        return this.proposalRepository.save(proposal);
    }

    @Transactional(readOnly = true)
    public Proposal findById(Long id) {
        return this.proposalRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public boolean userIsTheOwner(Long userId, Long propertyId){
        Property property = propertyService.findById(propertyId);
        return property.getOwner().getId().equals(userId);
    }

    private void validateProposal(Proposal proposal) {
        // Ensure property is not null and does not have an active contract
        if (proposal.getProperty() == null) {
            throw new BusinessException("Property must be provided for the proposal");
        }
        // Add more validation logic as needed
    }
}
