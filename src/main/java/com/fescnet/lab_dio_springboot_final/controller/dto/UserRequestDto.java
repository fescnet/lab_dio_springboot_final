package com.fescnet.lab_dio_springboot_final.controller.dto;

import com.fescnet.lab_dio_springboot_final.domain.model.User;
import lombok.Getter;
import lombok.Setter;

/**
 * This DTO represents the request data needed to create an user on the API
 * The request expects a password property among other fields
 */
@Getter @Setter
public class UserRequestDto extends UserResponseDto {

    private String password;

    public User toModel() {
        User model = new User();
        model.setId(this.getId());
        model.setName(this.getName());
        model.setEmail(this.getEmail());
        model.setPhone(this.getPhone());
        model.setPassword(this.getPassword());
        return model;
    }
}

