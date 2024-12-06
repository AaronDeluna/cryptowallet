package org.javaacademy.cryptowallet.mapper;

import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User convertToEntity(UserDto userDto) {
        return new User(
                userDto.getUserLogin(),
                userDto.getEmail(),
                userDto.getPassword()
        );
    }

    public UserDto convertToDto(User user) {
        return new UserDto(
                user.getLogin(),
                user.getEmail(),
                user.getPassword()
        );
    }
}
