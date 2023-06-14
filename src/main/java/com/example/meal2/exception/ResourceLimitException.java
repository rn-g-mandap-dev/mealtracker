package com.example.meal2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceLimitException extends RuntimeException {
    public ResourceLimitException(String message){super(message);}
    public ResourceLimitException(String message, Throwable cause){super(message, cause);}
}
