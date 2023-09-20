package ru.practicum.shareit.user;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class User {
    private Integer id;

    private String name;

    private String email;
}
