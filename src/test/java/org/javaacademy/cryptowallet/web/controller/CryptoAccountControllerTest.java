package org.javaacademy.cryptowallet.web.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.javaacademy.cryptowallet.mapper.CryptoAccountMapper;
import org.javaacademy.cryptowallet.service.crypto.CryptoAccountService;
import org.javaacademy.cryptowallet.service.user.UserService;
import org.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.javaacademy.cryptowallet.entity.CryptoCurrency.BTC;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Контроллер криптосчета")
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CryptoAccountControllerTest {
    @Autowired
    private CryptoAccountService cryptoAccountService;
    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBasePath("/cryptowallet")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

    @Test
    @DisplayName("Успешное получение всех крипто кошельков пользователя по логину")
    public void getAllCryptoAccountByUserLoginSuccess() {
        String expectedName = "Anton";

        UUID expectedUuid = cryptoAccountService.createCryptoAccount(CreateCryptoAccountDto.builder()
                .userLogin(expectedName)
                .currency(BTC)
                .build()
        );

        CryptoAccountDto expectedCryptoAccountDto = CryptoAccountDto.builder()
                .uuid(expectedUuid)
                .userLogin(expectedName)
                .currency(BTC)
                .currencyCount(BigDecimal.ZERO)
                .build();

        String url = "cryptowallet?user_login=%s".formatted(expectedName);
        List<CryptoAccountDto> cryptoAccountDtos = given()
                .get(url)
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {});

        int expectedSize = 1;
        int resultSize = cryptoAccountDtos.size();
        CryptoAccountDto resultCryptoAccountDto = cryptoAccountDtos.get(0);

        assertEquals(expectedSize, resultSize);
        assertEquals(expectedCryptoAccountDto, resultCryptoAccountDto);
    }
}
