package com.fescnet.lab_dio_springboot_final.controller;

import com.fescnet.lab_dio_springboot_final.controller.dto.LoginRequestDto;
import com.fescnet.lab_dio_springboot_final.controller.dto.LoginResponseDto;
import com.fescnet.lab_dio_springboot_final.domain.model.User;
import com.fescnet.lab_dio_springboot_final.domain.repository.UserRepository;
import com.fescnet.lab_dio_springboot_final.security.JWTCreator;
import com.fescnet.lab_dio_springboot_final.security.SecurityConfig;
import com.fescnet.lab_dio_springboot_final.service.exception.UserNotAllowedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication Controller", description = "RESTful API for authenticating users (client role).")
public class AuthController {
    private final PasswordEncoder encoder;
    private final SecurityConfig securityConfig;
    private final UserRepository repository;

    public AuthController(PasswordEncoder encoder, SecurityConfig securityConfig, UserRepository userRepository){
        this.encoder = encoder;
        this.securityConfig = securityConfig;
        this.repository = userRepository;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate an user", description = "Authenticates an user and returns a JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "403", description = "Invalid user credentials")
    })
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto){
        User user = repository.findByEmail(loginRequestDto.getEmail());
        if(user != null) {
            boolean passwordOk = encoder.matches(loginRequestDto.getPassword(), user.getPassword());
            if (!passwordOk) {
                throw new RuntimeException("Invalid password.");
            }
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setEmail(user.getEmail());
            loginResponseDto.setToken(JWTCreator.create(securityConfig.getKey(), user.getId(), securityConfig.getExpiration(), securityConfig.getPrefix()));
            return loginResponseDto;
        }else {
            throw new UserNotAllowedException("Login error");
        }
    }
}
