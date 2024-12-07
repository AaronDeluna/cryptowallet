package org.javaacademy.cryptowallet.service.currency_converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Profile("local")
public class LocalCurrencyConversionService implements CurrencyConversion {
    private static final int SCALE = 2;
    @Value("${test-data.dollar.rate}")
    private BigDecimal dollarRate;
    @Override
    public BigDecimal convertDollarToRubles(BigDecimal dollarCount) {
        return dollarCount.divide(dollarRate, SCALE, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal convertRubleToDollar(BigDecimal rubleCount) {
        return rubleCount.multiply(dollarRate);
    }
}
