package org.javaacademy.cryptowallet.controller;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.exception.UserNotFoundException;
import org.javaacademy.cryptowallet.service.crypto.CryptoAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            return ResponseEntity.ok().body("У вас пока нет созданных крипто кошельков");
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
}
