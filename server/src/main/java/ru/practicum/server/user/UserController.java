package ru.practicum.server.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.exception.AlreadyExistException;
import ru.practicum.server.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Количество пользователей: {}", userService.getAll());
        return userService.getAll();
    }

    @PostMapping
    public UserDto save(@RequestBody @Valid UserDto userDto) {
        log.info("Попытка добавить пользователя: {}", userDto);
        try {
            return userService.save(userDto);
        } catch (AlreadyExistException e) {
            throw new AlreadyExistException("Пользователь с таким email уже существует");
        }
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userdto, @PathVariable Integer userId) {
        log.info("Попытка обновить пользователя: {}", userdto);
        return userService.update(userId, userdto);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Integer userId) {
        log.info("Попытка получить пользователя с id: {}", userId);
        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public void removeById(@PathVariable Integer userId) {
        log.info("Попытка удаления пользователя с идентификатором: {}", userId);
        userService.removeById(userId);
    }
}
