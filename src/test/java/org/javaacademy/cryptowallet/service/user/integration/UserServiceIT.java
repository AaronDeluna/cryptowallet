package org.javaacademy.cryptowallet.service.user.integration;

import org.javaacademy.cryptowallet.dto.ResetUserPasswordDto;
import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.exception.InvalidPasswordException;
import org.javaacademy.cryptowallet.exception.UserLoginAlreadyExistsException;
import org.javaacademy.cryptowallet.exception.UserNotFoundException;
import org.javaacademy.cryptowallet.mapper.UserMapper;
import org.javaacademy.cryptowallet.repository.UserStorageRepository;
import org.javaacademy.cryptowallet.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Интеграционный тест пользовательского сервиса")
class UserServiceIT {
    @Autowired
    private UserService userService;
    @Autowired
    private UserStorageRepository userStorageRepository;
    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("Успешное сохранение пользователя")
    void saveSuccess() {
        String userLogin = "Anton";

        UserDto expectedUserDto = UserDto.builder()
                .userLogin(userLogin)
                .email("aa@mail.ru")
                .password("1234")
                .build();

        userService.save(expectedUserDto);
        UserDto resultUserDto = userMapper.toDto(userStorageRepository.findByLogin(userLogin));
        assertEquals(expectedUserDto, resultUserDto);
    }

    @Test
    @DisplayName("Успешно выбросит исключение, если пытаться сохранить уже существующего пользователя")
    public void shouldThrowExceptionWhenSavingExistingUser() {
        UserDto userDto = UserDto.builder()
                .userLogin("Anton")
                .email("aa@mail.ru")
                .password("1234")
                .build();

        userService.save(userDto);
        assertThrows(UserLoginAlreadyExistsException.class, () -> userService.save(userDto));
    }

    @Test
    @DisplayName("Успешный сброс пароля у пользователя")
    void resetPassword() {
        String userLogin = "Anton";
        String userPassword = "1234";
        String email = "aa@mail.ru";
        String newPassword = "333";

        UserDto userDto = UserDto.builder()
                .userLogin(userLogin)
                .email(email)
                .password(userPassword)
                .build();

        ResetUserPasswordDto resetUserPasswordDto = ResetUserPasswordDto.builder()
                .login(userLogin)
                .newPassword(newPassword)
                .oldPassword(userPassword)
                .build();

        userService.save(userDto);
        userService.resetPassword(resetUserPasswordDto);

        UserDto expectedUserDto = UserDto.builder()
                .userLogin(userLogin)
                .email(email)
                .password(newPassword)
                .build();

        UserDto resultUserDto = userMapper.toDto(userStorageRepository.findByLogin(userLogin));

        assertEquals(expectedUserDto, resultUserDto);
    }

    @Test
    @DisplayName("Успешно выбросит исключение, если пытаться сбросить пароль у несуществующего пользователя")
    public void shouldThrowExceptionWhenResettingPasswordForNonExistentUser() {
        ResetUserPasswordDto resetUserPasswordDto = ResetUserPasswordDto.builder()
                .login("Anton")
                .newPassword("1111")
                .oldPassword("1234")
                .build();

        assertThrows(UserNotFoundException.class, () -> userService.resetPassword(resetUserPasswordDto));
    }

    @Test
    @DisplayName("Успешно выбросит исключение, если попытаться указать неверный старый пароль")
    public void shouldThrowExceptionWhenOldPasswordIsIncorrect() {
        UserDto userDto = UserDto.builder()
                .userLogin("Anton")
                .email("aa@mail.ru")
                .password("1234")
                .build();

        ResetUserPasswordDto resetUserPasswordDto = ResetUserPasswordDto.builder()
                .login("Anton")
                .newPassword("1111")
                .oldPassword("2222")
                .build();

        userService.save(userDto);

        assertThrows(InvalidPasswordException.class, () -> userService.resetPassword(resetUserPasswordDto));
    }

}
