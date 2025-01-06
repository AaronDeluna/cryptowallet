package org.javaacademy.cryptowallet.mapper;

import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CryptoAccountMapper {

    public CryptoAccount toEntity(CreateCryptoAccountDto createDto) {
        return new CryptoAccount(
                createDto.getUserLogin(),
                createDto.getCurrency()
        );
    }

    public CryptoAccountDto toDto(CryptoAccount cryptoAccount) {
        return CryptoAccountDto.builder()
                .uuid(cryptoAccount.getUuid())
                .userLogin(cryptoAccount.getUserLogin())
                .currency(cryptoAccount.getCurrency())
                .currencyCount(cryptoAccount.getCurrencyCount())
                .build();
    }

    public List<CryptoAccountDto> toDtos(List<CryptoAccount> cryptoAccounts) {
        return cryptoAccounts.stream()
                .map(this::toDto)
                .toList();
    }
}
