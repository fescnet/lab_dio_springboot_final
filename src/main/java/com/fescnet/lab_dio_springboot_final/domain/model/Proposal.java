package com.fescnet.lab_dio_springboot_final.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "tb_proposals")
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start date is mandatory")
    @Future(message = "Start date must be in the future")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is mandatory")
    @Future(message = "End date must be in the future")
    @Column(nullable = false)
    private LocalDate endDate;

    @NotNull(message = "value proposed is mandatory")
    @Positive(message = "Proposed value must be positive")
    @Column(precision = 13, scale = 2, nullable = false)
    private BigDecimal valueProposed;

    @NotNull(message = "A proposal must have a property")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @NotNull(message = "A proposal must have a tenant")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id", nullable = false)
    private User tenant;

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be before end date");
            }
            long monthsBetween = startDate.until(endDate).toTotalMonths();
            if (monthsBetween < 12 || monthsBetween > 30) {
                throw new IllegalArgumentException("Contract duration must be between 12 and 30 months");
            }
        }
    }
}
