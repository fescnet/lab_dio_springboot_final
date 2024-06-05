package com.fescnet.lab_dio_springboot_final.controller.dto;

import com.fescnet.lab_dio_springboot_final.domain.model.Property;
import com.fescnet.lab_dio_springboot_final.domain.model.Proposal;
import com.fescnet.lab_dio_springboot_final.domain.model.User;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This DTO represents the expected data in create proposals endpoint
 */
@Getter
@Setter
public class ProposalRequestDto {

    protected Long id;

    @NotNull(message = "Start date is mandatory")
    @Future(message = "Start date must be in the future")
    protected LocalDate startDate;

    @NotNull(message = "End date is mandatory")
    @Future(message = "End date must be in the future")
    protected LocalDate endDate;

    @Positive(message = "Proposed value must be positive")
    protected BigDecimal valueProposed;

    @NotNull(message = "Property ID is mandatory")
    protected Long propertyId;

    @NotNull(message = "Tenant ID is mandatory")
    protected Long tenantId;

    public ProposalRequestDto() {}

    public Proposal toModel() {
        Proposal proposal = new Proposal();
        proposal.setId(this.id);
        proposal.setStartDate(this.startDate);
        proposal.setEndDate(this.endDate);
        proposal.setValueProposed(this.valueProposed);

        Property property = new Property();
        property.setId(this.propertyId);
        proposal.setProperty(property);

        User tenant = new User();
        tenant.setId(this.tenantId);
        proposal.setTenant(tenant);

        return proposal;
    }
}
