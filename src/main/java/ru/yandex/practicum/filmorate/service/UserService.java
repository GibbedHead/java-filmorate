package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long userId, long friendId) {

    }

    public void deleteFriend(long userId, long friendId) {

    }

    public List<User> getFriends(long id) {
        return new ArrayList<>();
    }

    public List<User> getCommonFriends(long id1, long id2) {
        return new ArrayList<>();
    }

    public User delete(long id) throws UserNotFoundException {
        User user = userStorage.findById(id);
        userStorage.delete(user);
        return user;
    }
}
