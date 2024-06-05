package com.fescnet.lab_dio_springboot_final.controller.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

/**
 * Error REST responses follow this pattern
 */
@Getter
@Setter
public class ExceptionResponseDto {
    private String message;

    private static final String DEFAULT_MESSAGE = "Unexpected exception";

    public ExceptionResponseDto(String message) {
        this.message = message == null ? DEFAULT_MESSAGE : message;
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"message\": \"" + DEFAULT_MESSAGE + "\"}";
        }
    }
}
