package org.javaacademy.cryptowallet.storage;

import org.javaacademy.cryptowallet.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserStorage {
    private final Map<String, User> userStorage = new HashMap<>();

    public void save(User user) {
        if (userStorage.containsKey(user.getLogin())) {
            throw new IllegalArgumentException(
                    "Ошибка: пользователь с логином '%s' уже зарегистрирован.".formatted(user.getLogin())
            );
        }
        userStorage.put(user.getLogin(), user);
    }

    public User getUserByLogin(String login) throws IllegalArgumentException {
        return userStorage.get(login);
    }
}
