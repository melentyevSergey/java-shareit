package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDto {
    private Integer id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank
    @Email(message = "электронная почта не может быть пустой и должна содержать символ @")
    private String email;
}
