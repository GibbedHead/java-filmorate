package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void create(User user);

    void update(User user) throws UserNotFoundException;

    void delete(User user);

    List<User> findAll();

    User findById(long id) throws UserNotFoundException;

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getFriends(long id);

    public List<User> getCommonFriends(long id1, long id2);
}
