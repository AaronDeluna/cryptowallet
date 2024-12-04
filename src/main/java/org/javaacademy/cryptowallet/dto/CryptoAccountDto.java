package org.javaacademy.cryptowallet.dto;

import lombok.Data;
import org.javaacademy.cryptowallet.entity.CryptoCurrency;

@Data
public class CryptoAccountDto {
    private String userLogin;
    private CryptoCurrency currency;
}
