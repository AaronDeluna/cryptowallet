package org.javaacademy.cryptowallet.service.currency_converter;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.javaacademy.cryptowallet.exception.CurrencyConversionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Profile("prod")
@RequiredArgsConstructor
public class CurrencyConversionServiceImpl implements CurrencyConversionService {
    private static final String INVALID_RESPONSE_ERROR = "Ошибка: Запрос не выполнен успешно или тело ответа пустое.";
    private static final String DOLLAR_RATE_JSON_PATH = "$.rates.USD";
    private static final int SCALE = 2;
    @Value("${central-bank.api}")
    private String centralBankApi;
    private final OkHttpClient client;

    @Override
    public BigDecimal convertDollarToRubles(BigDecimal dollarCount) {
        return dollarCount.divide(fetchDollarRate(), SCALE, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal convertRubleToDollar(BigDecimal rubleCount) {
        return rubleCount.multiply(fetchDollarRate());
    }

    private BigDecimal fetchDollarRate() throws CurrencyConversionException {
        Request request = buildRequest();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new CurrencyConversionException(INVALID_RESPONSE_ERROR);
            }
            String responseBody = response.body().string();
            return parseDollarPriceToResponse(responseBody);
        } catch (IOException e) {
            throw new CurrencyConversionException(e);
        }
    }

    private Request buildRequest() {
        return new Request.Builder()
                .url(centralBankApi)
                .get()
                .build();
    }

    private BigDecimal parseDollarPriceToResponse(String responseBody) {
        return JsonPath.parse(responseBody)
                .read(JsonPath.compile(DOLLAR_RATE_JSON_PATH), BigDecimal.class);
    }
}
