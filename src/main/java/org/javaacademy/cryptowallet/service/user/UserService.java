package org.javaacademy.cryptowallet.service.user;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.ResetUserPasswordDto;
import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.entity.User;
import org.javaacademy.cryptowallet.exception.InvalidPasswordException;
import org.javaacademy.cryptowallet.exception.UserLoginAlreadyExistsException;
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

    public void save(UserDto userDto) throws UserLoginAlreadyExistsException {
        User user = userMapper.toEntity(userDto);
        userStorageRepository.save(user);
    }

    public void resetPassword(ResetUserPasswordDto resetUserPasswordDto) throws UserNotFoundException,
            InvalidPasswordException {
        User user = userStorageRepository.findByLogin(resetUserPasswordDto.getLogin());
        if (!user.getPassword().equals(resetUserPasswordDto.getOldPassword())) {
            throw new InvalidPasswordException(PASSWORD_NOT_CORRECT);
        }
        user.setPassword(resetUserPasswordDto.getNewPassword());
    }
}
