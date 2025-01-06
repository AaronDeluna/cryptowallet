package org.javaacademy.cryptowallet.controller;

import lombok.extern.slf4j.Slf4j;
import org.javaacademy.cryptowallet.dto.ErrorResponse;
import org.javaacademy.cryptowallet.exception.CryptoAccountIdExistException;
import org.javaacademy.cryptowallet.exception.CryptoAccountNotFoundException;
import org.javaacademy.cryptowallet.exception.CryptoPriceRetrievalException;
import org.javaacademy.cryptowallet.exception.CurrencyConversionException;
import org.javaacademy.cryptowallet.exception.InsufficientFundsException;
import org.javaacademy.cryptowallet.exception.InvalidPasswordException;
import org.javaacademy.cryptowallet.exception.UserLoginAlreadyExistsException;
import org.javaacademy.cryptowallet.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CurrencyConversionException.class,
            CryptoPriceRetrievalException.class,
            InsufficientFundsException.class,
            CryptoAccountIdExistException.class,
            InvalidPasswordException.class,
            UserLoginAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({
            CryptoAccountNotFoundException.class,
            UserNotFoundException.class,
    })
    public ResponseEntity<ErrorResponse> handleNotFountException(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status.value(), message));
    }
}
