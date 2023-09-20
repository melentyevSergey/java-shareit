package ru.practicum.shareit.utils.validators;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utils.ValidationException;

@Slf4j
public class ValidateUser {

    private ValidateUser() {}

    public static void validate(UserDto user) {
        log.info("Запущен процесс валидации пользователя");
        log.debug("Идентификатор {}", user.getId());

        String email = user.getEmail();

        if (email == null || !email.contains("@")) {
            log.debug("Адрес электронной почты не может быть пустым и должен содержать символа @");

            throw new ValidationException("Адрес электронной почты не может быть пустым и" +
                    " должен содержать символа @");
        }

        log.info("Валидация пользователя успешно завершена.");
    }
}
