package org.javaacademy.cryptowallet.repository;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.entity.User;
import org.javaacademy.cryptowallet.mapper.UserMapper;
import org.javaacademy.cryptowallet.storage.UserStorage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStorageRepository {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public void save(User user) {
        userStorage.save(user);
    }

    public UserDto getUserByLogin(String login) {
        User user = userStorage.getUserByLogin(login);
        return userMapper.convertToDto(user);
    }
}
