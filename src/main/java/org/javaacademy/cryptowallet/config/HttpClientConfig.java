package org.javaacademy.cryptowallet.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {

    @Bean
    public OkHttpClient createClient() {
        return new OkHttpClient();
    }
}
