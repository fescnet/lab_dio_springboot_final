package com.fescnet.lab_dio_springboot_final.service.exception;

import java.io.Serial;

public class InvalidDataException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidDataException(String message) {
        super(message);
    }
}
