package com.example.meal2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpiredCredentialsException extends RuntimeException {
    public ExpiredCredentialsException(String message) {super(message);}
    public ExpiredCredentialsException(String message, Throwable cause) {super(message, cause);}
}
