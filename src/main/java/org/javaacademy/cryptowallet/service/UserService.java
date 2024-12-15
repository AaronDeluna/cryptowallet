package org.javaacademy.cryptowallet.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.ResetUserPassword;
import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.entity.User;
import org.javaacademy.cryptowallet.mapper.UserMapper;
import org.javaacademy.cryptowallet.repository.UserStorageRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String PASSWORD_NOT_CORRECT = "Ошибка: текущий пароль указан неверно.";
    private final UserStorageRepository userStorageRepository;
    private final UserMapper userMapper;

    public void save(UserDto userDto) {
        User user = userMapper.convertToEntity(userDto);
        userStorageRepository.save(user);
    }

    public UserDto getUserByLogin(String login) throws IllegalArgumentException {
        return userMapper.convertToDto(userStorageRepository.getUserByLogin(login));
    }

    public void resetPassword(ResetUserPassword resetUserPassword) {
        UserDto userDto = getUserByLogin(resetUserPassword.getLogin());
        if (!userDto.getPassword().equals(resetUserPassword.getOldPassword())) {
            throw new IllegalArgumentException(PASSWORD_NOT_CORRECT);
        }
        userDto.setPassword(resetUserPassword.getNewPassword());
    }
}
