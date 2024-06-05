package com.fescnet.lab_dio_springboot_final.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Login response
 */
@Getter @Setter
public class LoginResponseDto {
    private String email;
    private String token;
}
