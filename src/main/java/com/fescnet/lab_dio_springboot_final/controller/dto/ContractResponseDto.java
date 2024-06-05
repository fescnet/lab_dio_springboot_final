package com.fescnet.lab_dio_springboot_final.controller.dto;

import com.fescnet.lab_dio_springboot_final.domain.model.Contract;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO that represents a Contract response
 */
@Getter @Setter
public class ContractResponseDto extends ContractRequestDto {

    private UserResponseDto owner;
    private UserResponseDto tenant;
    private PropertyResponseDto property;
    private ProposalResponseDto proposal;

    private Long propertyId;
    private Long ownerId;
    private Long tenantId;
    private Long proposalId;

    protected LocalDate startDate;
    protected LocalDate endDate;
    protected BigDecimal rentalValue;

    public ContractResponseDto(Contract contract) {
        super();
        this.id = contract.getId();
        this.startDate = contract.getStartDate();
        this.endDate = contract.getEndDate();
        this.rentalValue = contract.getRentalValue();
        this.propertyId = contract.getProperty().getId();
        this.ownerId = contract.getOwner().getId();
        this.tenantId = contract.getTenant().getId();
        this.owner = new UserResponseDto(contract.getOwner());
        this.tenant = new UserResponseDto(contract.getTenant());
        this.property = new PropertyResponseDto((contract.getProperty()));
        this.proposal = new ProposalResponseDto(contract.getProposal());
    }
}
