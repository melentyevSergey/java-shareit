package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ItemDto {

    private Integer id;

    @NotBlank(message = "Название не может быть пустым.")
    @Size(min = 1, max = 32, message = "Максимальная длина — 32 символа.")
    private String name;

    @NotBlank(message = "Описание не может быть пустым.")
    @Size(min = 1, max = 64, message = "Максимальная длина описания — 64 символа.")
    private String description;

    @NotNull
    private Boolean available;
}
