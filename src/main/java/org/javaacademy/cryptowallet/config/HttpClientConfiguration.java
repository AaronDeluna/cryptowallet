package org.javaacademy.cryptowallet.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class HttpClientConfiguration {

    @Bean
    public OkHttpClient createClient() {
        return new OkHttpClient();
    }
}
