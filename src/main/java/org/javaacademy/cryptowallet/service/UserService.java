package org.javaacademy.cryptowallet.service;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.ResetUserPassword;
import org.javaacademy.cryptowallet.entity.User;
import org.javaacademy.cryptowallet.storage.UserStorage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void save(User user) {
        userStorage.save(user);
    }

    public User getUserByLogin(String login) throws IllegalArgumentException {
        return userStorage.getUserByLogin(login);
    }

    public void resetPassword(ResetUserPassword resetUserPassword) {
        User user = getUserByLogin(resetUserPassword.getLogin());
        if (!user.getPassword().equals(resetUserPassword.getOldPassword())) {
            throw new IllegalArgumentException("Ошибка: Не верно указан старый пароль, попробуйте еще раз");
        }
        user.setPassword(resetUserPassword.getNewPassword());
    }
}
