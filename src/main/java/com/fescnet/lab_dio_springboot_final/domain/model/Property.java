package com.fescnet.lab_dio_springboot_final.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity(name = "tb_properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Street is mandatory")
    @Column(nullable = false)
    private String street;

    @NotBlank(message = "Number is mandatory")
    @Column(nullable = false)
    private String number;

    @NotBlank(message = "Neighborhood is mandatory")
    @Column(nullable = false)
    private String neighborhood;

    @NotBlank(message = "City is mandatory")
    @Column(nullable = false)
    private String city;

    @NotBlank(message = "State is mandatory")
    @Size(min = 2, max = 2, message = "State must be a 2-letter abbreviation")
    @Column(nullable = false)
    private String state;

    @NotBlank(message = "Zip Code is mandatory")
    @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "Zip Code must be in the format 12345-678")
    @Column(nullable = false)
    private String zipCode;

    private String landmark;

    @NotNull(message = "Rental value is mandatory")
    @Column(nullable = false)
    private BigDecimal rentalValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Contract> contracts;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Proposal> proposals;
}
