package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    long create(User user);

    void update(User user);

    void delete(long id);

    List<User> findAll();

    User findById(long id) throws UserNotFoundException;

    void createOrConfirmFriendship(long user1Id, long user2Id);

    List<User> getFriends(long id);
}
