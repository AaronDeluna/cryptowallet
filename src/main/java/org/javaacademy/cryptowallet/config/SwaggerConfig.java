package org.javaacademy.cryptowallet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {
        Contact contact = new Contact()
                .email("ivan_senior@yandex.ru")
                .name("Ivan");

        Info info = new Info()
                .title("Сервис Криптокошелек")
                .contact(contact)
                .description("Сервис позволяет пользователям управлять своими" +
                        " криптовалютными счетами и получать актуальную информацию о " +
                        "стоимости их криптовалютных активов в реальном времени.");

        return new OpenAPI()
                .info(info);
    }
}
