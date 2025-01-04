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

    //Мне тоже страшно на это смотреть)

    @ExceptionHandler(UserLoginAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserLoginAlreadyExistsException(UserLoginAlreadyExistsException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }

    @ExceptionHandler(CryptoAccountIdExistException.class)
    public ResponseEntity<ErrorResponse> handleCryptoAccountIdExistException(CryptoAccountIdExistException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }

    @ExceptionHandler(CryptoAccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCryptoAccountNotFoundException(CryptoAccountNotFoundException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFundsException(InsufficientFundsException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }

    @ExceptionHandler(CryptoPriceRetrievalException.class)
    public ResponseEntity<ErrorResponse> handleCryptoPriceRetrievalException(CryptoPriceRetrievalException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }

    @ExceptionHandler(CurrencyConversionException.class)
    public ResponseEntity<ErrorResponse> handleCurrencyConversionException(CurrencyConversionException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), e);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, Exception e) {
        log.warn(message, e);
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status.value(), message));
    }
}
