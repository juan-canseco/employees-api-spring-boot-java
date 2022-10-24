package com.example.employee.api.employeeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
