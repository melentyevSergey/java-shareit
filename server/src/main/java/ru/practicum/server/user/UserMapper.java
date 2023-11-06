package ru.practicum.server.user;

import lombok.experimental.UtilityClass;
import ru.practicum.server.user.dto.UserDto;

@UtilityClass
public class UserMapper {

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(user.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
