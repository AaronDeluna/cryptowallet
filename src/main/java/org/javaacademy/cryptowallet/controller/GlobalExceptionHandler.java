package org.javaacademy.cryptowallet.controller;

import org.javaacademy.cryptowallet.exception.InvalidPasswordException;
import org.javaacademy.cryptowallet.exception.UserLoginAlreadyExistsException;
import org.javaacademy.cryptowallet.exception.UserNotFoundException;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserLoginAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> userLoginAlreadyExistsException(UserLoginAlreadyExistsException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> userNotFoundException(UserNotFoundException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorMessage> invalidPasswordException(InvalidPasswordException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(e.getMessage()));
    }
}
