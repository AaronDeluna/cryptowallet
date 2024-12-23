package org.javaacademy.cryptowallet.controller;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.dto.RefillRequestDto;
import org.javaacademy.cryptowallet.exception.CryptoAccountNotFoundException;
import org.javaacademy.cryptowallet.exception.InsufficientFundsException;
import org.javaacademy.cryptowallet.exception.UserNotFoundException;
import org.javaacademy.cryptowallet.service.crypto.CryptoAccountService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cryptowallet")
@RequiredArgsConstructor
public class CryptoAccountController {
    private final CryptoAccountService cryptoAccountService;

    @GetMapping()
    public ResponseEntity<?> getAllCryptoAccountByUserLogin(
            @RequestParam(name = "user_login") String userLogin) {
        if (cryptoAccountService.getAllCryptoAccountByUserLogin(userLogin).isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok().body(cryptoAccountService.getAllCryptoAccountByUserLogin(userLogin));
    }

    @PostMapping
    public ResponseEntity<?> createCryptoAccount(@RequestBody CreateCryptoAccountDto cryptoAccountDto) {
        try {
            UUID uuid = cryptoAccountService.createCryptoAccount(cryptoAccountDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(uuid);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/refill")
    @CacheEvict(value = "cryptoAccount", allEntries = true)
    public ResponseEntity<?> replenishAccountInRubles(@RequestBody RefillRequestDto refillRequestDto) {
        try {
            cryptoAccountService.replenishAccountInRubles(
                    refillRequestDto.getAccountId(), refillRequestDto.getRubleAmount()
            );
            return ResponseEntity.ok().build();
        } catch (IOException | CryptoAccountNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body("Ошибка при пополнении кошелька: %s".formatted(e.getMessage()));
        }
    }

    @PostMapping("/withdrawal")
    @CacheEvict(value = "cryptoAccount", allEntries = true)
    public ResponseEntity<?> withdrawRublesFromAccount(@RequestBody RefillRequestDto refillRequestDto) {
        try {
            return ResponseEntity.ok().body(cryptoAccountService.withdrawRublesFromAccount(
                    refillRequestDto.getAccountId(), refillRequestDto.getRubleAmount()));
        } catch (IOException | CryptoAccountNotFoundException | InsufficientFundsException e) {
            return ResponseEntity.badRequest().body("Ошибка: %s".formatted(e.getMessage()));
        }
    }

    @GetMapping("/balance/{id}")
    @Cacheable(value = "cryptoAccount")
    public ResponseEntity<?> showAccountBalanceInRublesById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok().body(cryptoAccountService.showAccountBalanceInRublesById(id));
        } catch (IOException | CryptoAccountNotFoundException e) {
            return ResponseEntity.badRequest().body("Ошибка: %s".formatted(e.getMessage()));
        }
    }

    @GetMapping("/balance/user/{login}")
    @Cacheable(value = "cryptoAccount")
    public ResponseEntity<?> showAllAccountBalanceInRublesByUserLogin(@PathVariable String login) {
        return ResponseEntity.ok().body(cryptoAccountService.showAllAccountBalanceInRublesByUserLogin(login));
    }
}
