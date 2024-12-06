package org.javaacademy.cryptowallet.storage;

import org.javaacademy.cryptowallet.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserStorage {
    private static final String USER_LOGIN_IS_EXIST = "Ошибка: пользователь с логином '%s' уже зарегистрирован.";
    private static final String LOGIN_NOTFOUND = "Пользователь с login: %s не найден!";
    private final Map<String, User> userStorage = new HashMap<>();

    public void save(User user) {
        if (userStorage.containsKey(user.getLogin())) {
            throw new RuntimeException(USER_LOGIN_IS_EXIST.formatted(user.getLogin()));
        }
        userStorage.put(user.getLogin(), user);
    }

    public User getUserByLogin(String login) throws IllegalArgumentException {
        return Optional.ofNullable(userStorage.get(login))
                .orElseThrow(
                        () -> new RuntimeException(LOGIN_NOTFOUND.formatted(login))
                );
    }
}
