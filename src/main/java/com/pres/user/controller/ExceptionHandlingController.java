package com.pres.user.controller;


import com.pres.user.model.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(Exception.class)
    public ResponseError handleError(Exception exception) {
        return new ResponseError(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}
