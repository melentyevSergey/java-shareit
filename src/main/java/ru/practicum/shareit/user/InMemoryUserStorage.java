package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.utils.DuplicateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {

    private Integer uid = 0;

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User getUser(Integer id) {
        return users.get(id);
    }

    @Override
    public List<User> getUsers() {
        log.info("Текущее количество пользователей: {}", users.values().size());

        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User newUser) {
        for (User user : getUsers()) {
            if (user.getEmail().equals(newUser.getEmail())) {
                log.debug("Пользователь с почтой {} уже существует", newUser.getEmail());

                throw new DuplicateException("Пользователь с таким адресом " +
                        "электронной почты уже существует.");
            }
        }

        newUser.setId(++uid);
        users.put(uid, newUser);

        log.debug("Создан новый пользователь с идентификатором: {}", newUser.getId());

        return newUser;
    }

    @Override
    public User updateUser(User user) {
        int id = user.getId();

        for (User userCheck : users.values()) {
            if (userCheck.getEmail().equals(user.getEmail()) && !user.getId().equals(userCheck.getId())) {
                throw new DuplicateException("Пользователь с таким email уже существует");
            }
        }

        if (user.getName() == null) {
            user.setName(users.get(user.getId()).getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(users.get(user.getId()).getEmail());
        }

        users.replace(user.getId(), user);

        log.debug("Обновлен пользователь с идентификатором: {}", id);

        return user;
    }

    @Override
    public void deleteUser(Integer id) {
        users.remove(id);

        log.debug("Удален пользователь с идентификатором: {}", id);
    }

    @Override
    public boolean isUserPresent(Integer id) {
        return users.containsKey(id);
    }
}
