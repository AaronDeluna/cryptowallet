package org.javaacademy.cryptowallet.service.crypto;

import org.javaacademy.cryptowallet.entity.CryptoCurrency;

import java.io.IOException;
import java.math.BigDecimal;

public interface CryptoPriceHandler {
    BigDecimal getCryptoPriceByCurrency(CryptoCurrency currency) throws IOException;
}
