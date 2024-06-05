package com.fescnet.lab_dio_springboot_final.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "tb_users")
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Column(nullable = false)
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 40, max = 255, message = "Password must be stored as a hash code")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Phone is mandatory")
    @Pattern(regexp = "^\\+?[0-9. ()-]{11,18}$", message = "Phone number is invalid.  It must have at least 11 digits.")
    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Property> properties;

    // I used CascadeType.ALL just for simplicity.
    // It would not be acceptable in a real world application, where you must keep the records for future usage.
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
    private List<Contract> contracts;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
    private List<Proposal> proposals;
}
