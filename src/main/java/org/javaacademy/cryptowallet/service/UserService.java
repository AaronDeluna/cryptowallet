package org.javaacademy.cryptowallet.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.ResetUserPassword;
import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.entity.User;
import org.javaacademy.cryptowallet.storage.UserStorage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String PASSWORD_NOT_CORRECT = "Ошибка: текущий пароль указан неверно.";
    private final UserStorage userStorage;

    public void save(UserDto userDto) {
        User user = new User(
                userDto.getLogin(),
                userDto.getEmail(),
                userDto.getPassword()
        );
        userStorage.save(user);
    }

    public User getUserByLogin(String login) throws IllegalArgumentException {
        return userStorage.getUserByLogin(login);
    }

    public void resetPassword(ResetUserPassword resetUserPassword) {
        User user = getUserByLogin(resetUserPassword.getLogin());
        if (!user.getPassword().equals(resetUserPassword.getOldPassword())) {
            throw new IllegalArgumentException(PASSWORD_NOT_CORRECT);
        }
        user.setPassword(resetUserPassword.getNewPassword());
    }
}
