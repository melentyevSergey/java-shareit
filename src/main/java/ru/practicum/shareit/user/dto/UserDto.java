package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 1, max = 50, message = "максимальная длина длина — 50 символов")
    private String name;
    @NotBlank
    @Email(message = "электронная почта не может быть пустой и должна содержать символ @")
    @Size(min = 1, max = 100, message = "максимальная длина описания — 100 символов")
    private String email;
}