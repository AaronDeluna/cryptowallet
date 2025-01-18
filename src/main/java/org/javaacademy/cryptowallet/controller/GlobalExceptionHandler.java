package org.javaacademy.cryptowallet.controller;

import lombok.extern.slf4j.Slf4j;
import org.javaacademy.cryptowallet.dto.ErrorResponse;
import org.javaacademy.cryptowallet.exception.CryptoAccountIdExistException;
import org.javaacademy.cryptowallet.exception.CryptoAccountNotFoundException;
import org.javaacademy.cryptowallet.exception.CryptoPriceRetrievalException;
import org.javaacademy.cryptowallet.exception.CurrencyConversionException;
import org.javaacademy.cryptowallet.exception.InsufficientFundsException;
import org.javaacademy.cryptowallet.exception.InvalidCryptoCurrencyException;
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
            InsufficientFundsException.class,
            CryptoAccountIdExistException.class,
            UserLoginAlreadyExistsException.class,
            InvalidCryptoCurrencyException.class
    })
    public ResponseEntity<ErrorResponse> handleException(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({
            InvalidPasswordException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler({
            CryptoAccountNotFoundException.class,
            UserNotFoundException.class,
    })
    public ResponseEntity<ErrorResponse> handleNotFountException(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleServerError(Throwable e) {
        log.warn(e.getMessage(), e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler({
            CryptoPriceRetrievalException.class,
            CurrencyConversionException.class
    })
    public ResponseEntity<ErrorResponse> handleIntegrationServerError(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status.value(), status.name(), message));
    }
}
