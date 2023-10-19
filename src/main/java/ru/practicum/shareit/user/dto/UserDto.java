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

    @NotBlank(message = "Имя не может быть пустым.")
    @Size(min = 1, max = 32, message = "Максимальная длина — 32 символа.")
    private String name;

    @NotBlank
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")

    @Size(min = 1, max = 64, message = "Максимальная длина описания — 64 символова.")
    private String email;
}
