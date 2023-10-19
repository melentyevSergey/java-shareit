package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUser(Integer id);

    List<UserDto> getUsers();

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Integer userId, UserDto userDto);

    void deleteUser(Integer id);

    User getUserIfExist(Integer userId);
}
