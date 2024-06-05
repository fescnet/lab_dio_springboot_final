package com.fescnet.lab_dio_springboot_final.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/")
@Tag(name = "Welcome Controller", description = "RESTful API for health checks.")
public class WelcomeController {

    @GetMapping
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("<h1>Welcome to Quinto Andar API.</h1>You can access the API documentation here: <a href=\"http://localhost:8080/swagger-ui.html\">http://localhost:8080/swagger-ui.html</a>");
    }
}
