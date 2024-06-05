package com.fescnet.lab_dio_springboot_final.controller.exception;

import com.fescnet.lab_dio_springboot_final.controller.dto.ExceptionResponseDto;
import com.fescnet.lab_dio_springboot_final.service.exception.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponseDto handleBusinessException(BusinessException ex) {
        return new ExceptionResponseDto(ex.getMessage());
    }

    @ExceptionHandler(UserNotAllowedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponseDto handleUserNotAllowedException(UserNotAllowedException ex) {
        return new ExceptionResponseDto(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponseDto handleNoContentException(NotFoundException ex) {
        return new ExceptionResponseDto("Resource Id not found.");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseDto handleConstraintViolationException(ConstraintViolationException ex) {
        String a = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).reduce("", (message1, message2) -> message1.concat(message2).concat("; "));
        return new ExceptionResponseDto(a);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseDto handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "Data integrity violation: " + ex.getMostSpecificCause().getMessage();
        return new ExceptionResponseDto("Email address already exists. Please use a different email.");
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseDto handleInvalidDataException(InvalidDataException ex) {
        return new ExceptionResponseDto(ex.getMessage());
    }

    @ExceptionHandler(ActiveContractException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseDto handleActiveContractException(ActiveContractException ex) {
        return new ExceptionResponseDto(ex.getMessage());
    }

    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponseDto handleTransactionSystemException(TransactionSystemException ex) {
        return new ExceptionResponseDto("Invalid data provided, check the API docs for more details");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponseDto handleUnexpectedException(Throwable ex) {
        String message = "Unexpected server error.";
        return new ExceptionResponseDto(message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponseDto handleException(Exception ex) {
        LOGGER.info(ex.getClass().getName());
        return new ExceptionResponseDto(ex.getMessage());
    }


}
