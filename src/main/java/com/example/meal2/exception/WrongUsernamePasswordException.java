package com.example.meal2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class WrongUsernamePasswordException extends RuntimeException {
    public WrongUsernamePasswordException(String message){
        super(message);
    }
    public WrongUsernamePasswordException(String message, Throwable cause){
        super(message, cause);
    }
}
