package org.javaacademy.cryptowallet.controller;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.service.crypto.CryptoAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cryptowallet")
@RequiredArgsConstructor
public class CryptoAccountController {
    private static final String GET_CRYPTO_ACCOUNT_BY_UUID_PATH = "/{uuid}";
    private static final String ALL_ACCOUNTS_BY_USER_LOGIN_PATH = "/all/{userLogin}";
    private final CryptoAccountService cryptoAccountService;

    @GetMapping(GET_CRYPTO_ACCOUNT_BY_UUID_PATH)
    public CryptoAccountDto getCryptoAccountByUuid(@PathVariable UUID uuid) {
        return cryptoAccountService.getCryptoAccountByUuid(uuid);
    }

    @GetMapping(ALL_ACCOUNTS_BY_USER_LOGIN_PATH)
    public List<CryptoAccountDto> getAllCryptoAccountByUserLogin(@PathVariable String userLogin) {
        return cryptoAccountService.getAllCryptoAccountByUserLogin(userLogin);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UUID createCryptoAccount(@RequestBody CreateCryptoAccountDto cryptoAccountDto) {
        return cryptoAccountService.createCryptoAccount(cryptoAccountDto);
    }
}
