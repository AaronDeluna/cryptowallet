package org.javaacademy.cryptowallet.storage;

import lombok.Getter;
import org.javaacademy.cryptowallet.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class UserStorage {
    private final Map<String, User> userStorage = new HashMap<>();

}
