package org.javaacademy.cryptowallet.controller;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.ResetUserPasswordDto;
import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.exception.InvalidPasswordException;
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
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto) {
        try {
            userService.save(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
