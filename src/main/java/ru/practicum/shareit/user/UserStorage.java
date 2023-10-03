package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {

    User getUser(Integer id);

    List<User> getUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Integer id);

    boolean isUserPresent(Integer id);
}
