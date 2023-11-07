package ru.practicum.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.exception.AlreadyExistException;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody UserDto userDto) {
        log.info("Попытка добавить пользователя: {}", userDto);
        try {
            return userClient.saveUser(userDto);
        } catch (AlreadyExistException e) {
            throw new AlreadyExistException("Пользователь с таким email уже существует");
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable Integer userId,
                                         @RequestBody UserDto userDto) {
        log.info("Попытка обновить пользователя: {}", userDto);
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> removeById(@PathVariable Integer userId) {
        log.info("Попытка удаления пользователя с идентификатором: {}", userId);
        return userClient.removeById(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable Integer userId) {
        log.info("Попытка получить пользователя с id: {}", userId);
        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Попытка получить всех пользователей");
        return userClient.getAll();
    }
}