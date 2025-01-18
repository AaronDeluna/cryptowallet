package org.javaacademy.cryptowallet.web.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.javaacademy.cryptowallet.dto.ResetUserPasswordDto;
import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.mapper.UserMapper;
import org.javaacademy.cryptowallet.repository.UserStorageRepository;
import org.javaacademy.cryptowallet.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тест пользовательского контроллера")
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    private UserStorageRepository userStorageRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBasePath("/user")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void signupSuccess() {
        UserDto expectedUserDto = UserDto.builder()
                .userLogin("Anna")
                .email("mm@mail.ru")
                .password("1234")
                .build();

        given(requestSpecification)
                .body(expectedUserDto)
                .post("/signup")
                .then()
                .spec(responseSpecification)
                .statusCode(201);

        UserDto resultUserDto = userMapper.toDto(
                userStorageRepository.findByLogin(expectedUserDto.getUserLogin())
        );
        assertEquals(expectedUserDto, resultUserDto);
    }

    @Test
    @DisplayName("Ошибка при попытке регистрации с уже существующим логином")
    public void shouldSuccessfullyReturnBadRequestWhenUsernameAlreadyExists() {
        UserDto userDto = UserDto.builder()
                .userLogin("ivan")
                .email("mm@mail.ru")
                .password("1234")
                .build();
        userService.save(userDto);

        given(requestSpecification)
                .body(userDto)
                .post("/signup")
                .then()
                .spec(responseSpecification)
                .statusCode(400);
    }

    @Test
    @DisplayName("Успешный сброс пароля пользователя")
    public void resetPasswordSuccess() {
        UserDto userDto = UserDto.builder()
                .userLogin("ivan")
                .email("aa@mail.ru")
                .password("1234")
                .build();
        userService.save(userDto);
        ResetUserPasswordDto resetUserPasswordDto = ResetUserPasswordDto.builder()
                .login("ivan")
                .oldPassword("1234")
                .newPassword("5555")
                .build();

        given(requestSpecification)
                .body(resetUserPasswordDto)
                .post("/reset-password")
                .then()
                .spec(responseSpecification)
                .statusCode(200);

        String expectedNewPassword = "5555";
        String resultPassword = userStorageRepository.findByLogin(resetUserPasswordDto.getLogin()).getPassword();
        assertEquals(expectedNewPassword, resultPassword);
    }

    @Test
    @DisplayName("Ошибка при попытке сбросить пароль для несуществующего пользователя")
    public void shouldSuccessfullyReturnBadRequestWhenUserDoesNotExist() {
        ResetUserPasswordDto resetUserPasswordDto = ResetUserPasswordDto.builder()
                .login("ivan")
                .oldPassword("1111")
                .newPassword("5555")
                .build();

        given(requestSpecification)
                .body(resetUserPasswordDto)
                .post("/reset-password")
                .then()
                .spec(responseSpecification)
                .statusCode(404);
    }

    @Test
    @DisplayName("Ошибка при попытке ввода не вернорго старого пароля")
    public void shouldSuccessfullyReturnBadRequestWhenOldPasswordIsIncorrect() {
        UserDto userDto = UserDto.builder()
                .userLogin("ivan")
                .email("aa@mail.ru")
                .password("1234")
                .build();
        userService.save(userDto);
        ResetUserPasswordDto resetUserPasswordDto = ResetUserPasswordDto.builder()
                .login("ivan")
                .oldPassword("1111")
                .newPassword("5555")
                .build();

        given(requestSpecification)
                .body(resetUserPasswordDto)
                .post("/reset-password")
                .then()
                .spec(responseSpecification)
                .statusCode(403);
    }

}
