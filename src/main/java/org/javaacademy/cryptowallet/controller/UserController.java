package org.javaacademy.cryptowallet.controller;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.exception.InvalidPasswordException;
import org.javaacademy.cryptowallet.exception.UserNotFoundException;
import org.javaacademy.cryptowallet.service.user.ResetUserPassword;
import org.javaacademy.cryptowallet.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private static final String SIGNUP_PATH = "/signup";
    private static final String RESET_PASSWORD_PATH = "/reset-password";
    private final UserService userService;

    @PostMapping(SIGNUP_PATH)
    public void signup(@RequestBody UserDto userDto) {
        userService.save(userDto);
    }


    @PostMapping(RESET_PASSWORD_PATH)
    public ResponseEntity<?> resetPassword(@RequestBody ResetUserPassword resetUserPassword) {
        try {
            userService.resetPassword(resetUserPassword);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException | InvalidPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
