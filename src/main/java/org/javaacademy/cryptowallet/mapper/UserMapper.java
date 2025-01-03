package org.javaacademy.cryptowallet.mapper;

import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDto userDto) {
        return new User(
                userDto.getUserLogin(),
                userDto.getEmail(),
                userDto.getPassword()
        );
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .userLogin(user.getLogin())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
