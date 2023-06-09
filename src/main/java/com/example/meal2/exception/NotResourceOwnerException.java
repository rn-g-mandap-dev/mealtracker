package com.example.meal2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotResourceOwnerException extends RuntimeException {
    public NotResourceOwnerException (String message){
        super(message);
    }
    public NotResourceOwnerException (String message, Throwable cause){
        super(message, cause);
    }
}
