package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void create(User user);

    void update(User user) throws UserNotFoundException;

    void delete(User user);

    List<User> findAll();

    User findById(long id) throws UserNotFoundException;
}
