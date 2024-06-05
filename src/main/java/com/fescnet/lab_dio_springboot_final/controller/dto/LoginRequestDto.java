package com.fescnet.lab_dio_springboot_final.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO used in login endpoint
 */
@Getter @Setter
public class LoginRequestDto {
    private String email;
    private String password;
}
