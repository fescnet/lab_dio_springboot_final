package com.fescnet.lab_dio_springboot_final.service.exception;

import java.io.Serial;

public class UserNotAllowedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserNotAllowedException(String message) {
        super(message);
    }
}
