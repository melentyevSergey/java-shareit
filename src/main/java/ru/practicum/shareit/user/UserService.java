package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utils.NotFoundException;
import ru.practicum.shareit.utils.validators.ValidateUser;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public UserDto getUser(Integer id) {
        if (id > 0 && userStorage.isUserPresent(id)) {
            log.debug("Валидация GET запроса на получение пользователя " +
                    "по идентификатору {} завершена успешно.", id);

            return UserMapper.toUserDto(userStorage.getUser(id));
        } else {
            throw new NotFoundException("Нет пользователя с таким идентификатором.");
        }
    }

    public List<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(UserDto userDto) {
        ValidateUser.validate(userDto);

        return UserMapper.toUserDto(userStorage.createUser(UserMapper.toUser(userDto)));
    }

    public UserDto updateUser(Integer id, UserDto userDto) {
        if (userStorage.isUserPresent(id)) {
            userDto.setId(id);
            return UserMapper.toUserDto(userStorage.updateUser(UserMapper.toUser(userDto)));
        } else {
            throw new NotFoundException("Нет пользователя с таким идентификатором.");
        }
    }

    public void deleteUser(Integer id) {
        if (userStorage.isUserPresent(id)) {
            userStorage.deleteUser(id);
        } else {
            throw new NotFoundException("Нет пользователя с таким идентификатором.");
        }
    }
}
