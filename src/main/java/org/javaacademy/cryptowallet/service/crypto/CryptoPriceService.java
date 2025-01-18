package org.javaacademy.cryptowallet.service.crypto;

import org.javaacademy.cryptowallet.entity.CryptoCurrency;

import java.math.BigDecimal;

public interface CryptoPriceService {
    BigDecimal getCryptoPriceByCurrency(CryptoCurrency currency);
}
