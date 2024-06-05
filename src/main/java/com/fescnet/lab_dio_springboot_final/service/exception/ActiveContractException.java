package com.fescnet.lab_dio_springboot_final.service.exception;

import java.io.Serial;

public class ActiveContractException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ActiveContractException(String message) {
        super(message);
    }
}
