package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    long create(User user);

    void update(User user);

    void delete(User user);

    List<User> findAll();

    User findById(long id);

    boolean isUserExists(User user);
}
