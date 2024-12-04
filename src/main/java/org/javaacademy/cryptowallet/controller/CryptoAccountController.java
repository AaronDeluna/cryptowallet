package org.javaacademy.cryptowallet.controller;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.javaacademy.cryptowallet.service.CryptoAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cryptowallet")
@RequiredArgsConstructor
public class CryptoAccountController {
    private final CryptoAccountService cryptoAccountService;

    @PostMapping
    public UUID createCryptoAccount(@RequestBody CryptoAccountDto cryptoAccountDto) {
        return cryptoAccountService.createCryptoAccount(cryptoAccountDto);
    }

    @GetMapping("/{uuid}")
    public CryptoAccount getCryptoAccountByUuid(@PathVariable UUID uuid) {
        return cryptoAccountService.getCryptoAccountByUuid(uuid);
    }

    @GetMapping("/all/{userLogin}")
    public List<CryptoAccount> getAllCryptoAccountByUserLogin(@PathVariable String userLogin) {
        return cryptoAccountService.getAllCryptoAccountByUserLogin(userLogin);
    }
}
