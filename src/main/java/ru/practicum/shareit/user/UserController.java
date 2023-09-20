package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @ResponseBody
    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Integer userId) {
        log.info("Получен GET запрос для получения пользователя по идентификатору {}", userId);

        return service.getUser(userId);
    }

    @GetMapping()
    public List<UserDto> getUsers() {
        log.debug("Получен GET запрос на получение списка всех пользователей.");

        return service.getUsers();
    }

    @PostMapping()
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Получен POST запрос для создания нового пользователя.");

        return service.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Integer userId, @RequestBody UserDto userdto) {
        log.info("Получен PATCH запрос для обновления существующего пользователя.");

        return service.updateUser(userId, userdto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        log.info("Получен DELETE запрос для удаления пользователя {}", userId);

        service.deleteUser(userId);
    }
}
