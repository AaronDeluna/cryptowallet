package org.javaacademy.cryptowallet.service.crypto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.javaacademy.cryptowallet.mapper.CryptoAccountMapper;
import org.javaacademy.cryptowallet.repository.CryptoAccountStorageRepository;
import org.javaacademy.cryptowallet.storage.UserStorage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CryptoAccountService {
    private static final String USER_LOGIN_NOTFOUND = "Ошибка: пользователя с логином %s не найден";
    private final CryptoAccountStorageRepository cryptoAccountStorageRepository;
    private final UserStorage userStorage;
    private final CryptoAccountMapper cryptoAccountMapper;

    public CryptoAccount getCryptoAccountByUuid(UUID uuid) {
        return cryptoAccountStorageRepository.getCryptoAccountByUuid(uuid);
    }

    public List<CryptoAccount> getAllCryptoAccountByUserLogin(String userLogin) {
        return cryptoAccountStorageRepository.getAllCryptoAccountByUserLogin(userLogin);
    }

    public UUID createCryptoAccount(CryptoAccountDto cryptoAccountDto) {
        Optional.ofNullable(userStorage.getUserByLogin(cryptoAccountDto.getUserLogin()))
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                USER_LOGIN_NOTFOUND.formatted(cryptoAccountDto.getUserLogin()))
                );

        CryptoAccount cryptoAccount = cryptoAccountMapper.convertToEntity(cryptoAccountDto);
        cryptoAccountStorageRepository.save(cryptoAccount);
        return cryptoAccount.getUuid();
    }
}
