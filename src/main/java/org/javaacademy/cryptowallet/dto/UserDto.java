package org.javaacademy.cryptowallet.dto;

import lombok.Data;

@Data
public class UserDto {
    private String login;
    private String email;
    private String password;
}
