package com.fescnet.lab_dio_springboot_final.controller.dto;

import com.fescnet.lab_dio_springboot_final.domain.model.Proposal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProposalResponseDto extends ProposalRequestDto {

    private PropertyResponseDto property;
    private UserResponseDto tenant;

    public ProposalResponseDto(Proposal proposal) {
        super();
        this.id = proposal.getId();
        this.startDate = proposal.getStartDate();
        this.endDate = proposal.getEndDate();
        this.valueProposed = proposal.getValueProposed();
        this.propertyId = proposal.getProperty().getId();
        this.tenantId = proposal.getTenant().getId();

        // Include nested DTOs for detailed information
        this.property = new PropertyResponseDto(proposal.getProperty());
        this.tenant = new UserResponseDto(proposal.getTenant());
    }
}
