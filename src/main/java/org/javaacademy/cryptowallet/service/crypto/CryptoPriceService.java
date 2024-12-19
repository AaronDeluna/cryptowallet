package org.javaacademy.cryptowallet.service.crypto;

import com.jayway.jsonpath.JsonPath;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.javaacademy.cryptowallet.entity.CryptoCurrency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@Profile("prod")
public class CryptoPriceService implements CryptoPriceHandler {
    private static final String INVALID_RESPONSE_ERROR = "Ошибка: Запрос не выполнен успешно или тело ответа пустое.";
    private static final String API_KEY_HEADER = "x_cg_demo_api_key";
    private static final String CRYPTO_CURRENCY_PRICE_PATH = "$.%s.usd";
    @Value("${crypto.api}")
    private String url;
    @Value("${crypto.token}")
    private String token;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public BigDecimal getCryptoPriceByCurrency(CryptoCurrency currency) throws IOException {
        Request request = buildRequest(currency);

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new RuntimeException(INVALID_RESPONSE_ERROR);
            }
            return extractPriceFromResponse(response.body().string(), currency);
        }
    }

    private Request buildRequest(CryptoCurrency currency) {
        return new Request.Builder()
                .url(url.formatted(currency.getDesc()))
                .addHeader(API_KEY_HEADER, token)
                .get()
                .build();
    }

    private BigDecimal extractPriceFromResponse(String responseBody, CryptoCurrency currency) {
        String jsonPath = CRYPTO_CURRENCY_PRICE_PATH.formatted(currency.getDesc());
        return JsonPath.parse(responseBody)
                .read(JsonPath.compile(jsonPath), BigDecimal.class);
    }
}
