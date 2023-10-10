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

    @NotBlank(message = "Название не может быть пустым")
    @Size(min = 1, max = 55, message = "максимальная длина длина — 45 символов")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 1, max = 88, message = "максимальная длина описания — 90 символов")
    private String description;

    @NotNull
    private Boolean available;
}
