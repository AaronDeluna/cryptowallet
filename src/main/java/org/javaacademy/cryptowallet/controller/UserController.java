package org.javaacademy.cryptowallet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.ResetUserPasswordDto;
import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.exception.InvalidPasswordException;
import org.javaacademy.cryptowallet.exception.UserLoginAlreadyExistsException;
import org.javaacademy.cryptowallet.exception.UserNotFoundException;
import org.javaacademy.cryptowallet.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User controller", description = "Контроллер для работы с пользователем")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Регистрация пользователя", description = "Регестрирует нового пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Создан"),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto) {
        try {
            userService.save(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UserLoginAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Сброс пароля пользователя", description = "Сбрасывает пароль пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetUserPasswordDto resetUserPasswordDto) {
        try {
            userService.resetPassword(resetUserPasswordDto);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException | InvalidPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
