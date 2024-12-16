package org.javaacademy.cryptowallet.service.crypto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.javaacademy.cryptowallet.mapper.CryptoAccountMapper;
import org.javaacademy.cryptowallet.repository.CryptoAccountRepository;
import org.javaacademy.cryptowallet.repository.UserStorageRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoAccountService {
    private static final String USER_LOGIN_NOTFOUND = "Ошибка: пользователя с логином %s не найден";
    private final CryptoAccountRepository cryptoAccountRepository;
    private final UserStorageRepository userStorageRepository;
    private final CryptoAccountMapper cryptoAccountMapper;

    public CryptoAccountDto getCryptoAccountByUuid(UUID id) {
        return cryptoAccountMapper.convertToDto(
                cryptoAccountRepository.getCryptoAccountByUuid(id)
        );
    }

    public List<CryptoAccountDto> getAllCryptoAccountByUserLogin(String userLogin) {
        return cryptoAccountRepository.getAllCryptoAccountByUserLogin(userLogin).stream()
                .map(cryptoAccountMapper::convertToDto)
                .toList();
    }

    public UUID createCryptoAccount(CreateCryptoAccountDto createDto) {
        Optional.ofNullable(userStorageRepository.getUserByLogin(createDto.getUserLogin()))
                .orElseThrow(() -> new RuntimeException(
                        USER_LOGIN_NOTFOUND.formatted(createDto.getUserLogin()))
                );
        CryptoAccount cryptoAccount = cryptoAccountMapper.convertToEntity(createDto);
        cryptoAccountRepository.save(cryptoAccount);
        return cryptoAccount.getUuid();
    }
}
