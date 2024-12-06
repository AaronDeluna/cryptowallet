package org.javaacademy.cryptowallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private String userLogin;
    private String email;
    private String password;
}
