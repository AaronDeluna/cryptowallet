package org.javaacademy.cryptowallet.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.javaacademy.cryptowallet.storage.CryptoStorage;
import org.javaacademy.cryptowallet.storage.UserStorage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CryptoAccountService {
    private final static String USER_LOGIN_NOTFOUND = "Ошибка: пользователя с логином %s не найден";
    private final CryptoStorage cryptoStorage;
    private final UserStorage userStorage;

    public CryptoAccount getCryptoAccountByUuid(UUID uuid) {
        return cryptoStorage.getCryptoAccountByUuid(uuid);
    }

    public List<CryptoAccount> getAllCryptoAccountByUserLogin(String userLogin) {
        return cryptoStorage.getAllCryptoAccountByUserLogin(userLogin);
    }

    public UUID createCryptoAccount(CryptoAccountDto cryptoAccountDto) {
        Optional.ofNullable(userStorage.getUserByLogin(cryptoAccountDto.getUserLogin()))
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                USER_LOGIN_NOTFOUND.formatted(cryptoAccountDto.getUserLogin()))
                );

        CryptoAccount cryptoAccount = new CryptoAccount(
                UUID.randomUUID(),
                cryptoAccountDto.getUserLogin(),
                cryptoAccountDto.getCurrency(),
                BigDecimal.ZERO
        );
        cryptoStorage.save(cryptoAccount);
        return cryptoAccount.getUuid();
    }
}
