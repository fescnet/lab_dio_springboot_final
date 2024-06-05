package com.fescnet.lab_dio_springboot_final.controller.dto;

import com.fescnet.lab_dio_springboot_final.domain.model.User;
import lombok.Getter;
import lombok.Setter;

/**
 * This DTO represents a user
 * For security reasons, the password is not transmitted back to the client
 */
@Getter @Setter
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String phone;

    protected UserResponseDto(){}

    public UserResponseDto(User model) {
        this.id = model.getId();
        this.name = model.getName();
        this.email = model.getEmail();
        this.phone = model.getPhone();
    }
}
