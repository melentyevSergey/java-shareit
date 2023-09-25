package ru.practicum.shareit.utils.validators;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.ValidationException;

@Slf4j
public class ValidateItem {

    private ValidateItem() {}

    public static void validate(ItemDto itemDto) {
        log.info("Запущен процесс валидации вещи.");
        log.debug("Идентификатор {}", itemDto.getId());

        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean isAvailable = itemDto.getAvailable();

        if (name == null || name.isEmpty()) {
            throw new ValidationException("Название вещи не может быть пустым.");
        }

        if (description == null || description.isEmpty()) {
            throw new ValidationException("Описание вещи не может быть пустым.");
        }

        if (isAvailable == null || !isAvailable) {
            throw new ValidationException("Вещь должна быть доступна.");
        }

        log.info("Валидация вещи успешно завершена.");
    }
}
