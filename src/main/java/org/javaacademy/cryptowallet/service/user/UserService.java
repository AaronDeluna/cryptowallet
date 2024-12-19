package org.javaacademy.cryptowallet.service.user;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.entity.User;
import org.javaacademy.cryptowallet.exception.InvalidPasswordException;
import org.javaacademy.cryptowallet.exception.UserNotFoundException;
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

    public void resetPassword(ResetUserPassword resetUserPassword) throws UserNotFoundException,
            InvalidPasswordException {
        User user = userStorageRepository.findByLogin(resetUserPassword.getLogin());
        if (!user.getPassword().equals(resetUserPassword.getOldPassword())) {
            throw new InvalidPasswordException(PASSWORD_NOT_CORRECT);
        }
        user.setPassword(resetUserPassword.getNewPassword());
    }
}
