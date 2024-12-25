package org.javaacademy.cryptowallet.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javaacademy.cryptowallet.entity.User;
import org.javaacademy.cryptowallet.exception.UserNotFoundException;
import org.javaacademy.cryptowallet.storage.UserStorage;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserStorageRepository {
    private static final String USER_LOGIN_IS_EXIST = "Ошибка: пользователь с логином '%s' уже зарегистрирован.";
    private static final String LOGIN_NOTFOUND = "Ошибка: Пользователь с login: %s не найден!";
    private final UserStorage userStorage;

    public void save(User user) throws RuntimeException {
        if (getStorage().containsKey(user.getLogin())) {
            throw new RuntimeException(USER_LOGIN_IS_EXIST.formatted(user.getLogin()));
        }
        getStorage().put(user.getLogin(), user);
    }

    public User findByLogin(String login) throws UserNotFoundException {
        return Optional.ofNullable(getStorage().get(login))
                .orElseThrow(
                        () -> new UserNotFoundException(LOGIN_NOTFOUND.formatted(login))
                );
    }

    private Map<String, User> getStorage() {
        return userStorage.getUserStorage();
    }
}
