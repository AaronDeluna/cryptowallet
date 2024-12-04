package org.javaacademy.cryptowallet.storage;

import org.javaacademy.cryptowallet.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserStorage {
    private static final String USER_LOGIN_IS_EXIST = "Ошибка: пользователь с логином '%s' уже зарегистрирован.";
    private final Map<String, User> userStorage = new HashMap<>();

    public void save(User user) {
        if (userStorage.containsKey(user.getLogin())) {
            throw new IllegalArgumentException(
                    USER_LOGIN_IS_EXIST.formatted(user.getLogin())
            );
        }
        userStorage.put(user.getLogin(), user);
    }

    public User getUserByLogin(String login) throws IllegalArgumentException {
        return userStorage.get(login);
    }
}
