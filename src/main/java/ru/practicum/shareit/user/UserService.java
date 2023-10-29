package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto save(UserDto userDto);

    UserDto update(Integer userId, UserDto userDto);

    UserDto getById(Integer id);

    void removeById(Integer id);
}