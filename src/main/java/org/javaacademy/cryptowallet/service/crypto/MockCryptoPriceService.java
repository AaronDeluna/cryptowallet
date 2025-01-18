package org.javaacademy.cryptowallet.service.crypto;

import org.javaacademy.cryptowallet.entity.CryptoCurrency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Profile("local")
public class MockCryptoPriceService implements CryptoPriceService {
    @Value("${test-data.crypto.price}")
    private BigDecimal price;

    @Override
    public BigDecimal getCryptoPriceByCurrency(CryptoCurrency currency) {
        return price;
    }
}
