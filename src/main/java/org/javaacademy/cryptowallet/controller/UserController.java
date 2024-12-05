package org.javaacademy.cryptowallet.controller;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.ResetUserPassword;
import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody UserDto userDto) {
        userService.save(userDto);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody ResetUserPassword resetUserPassword) {
        userService.resetPassword(resetUserPassword);
    }
}
