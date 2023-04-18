package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriend(long userId, long friendId) throws UserNotFoundException {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        userStorage.addFriend(userId, friendId);
        userStorage.addFriend(friendId, userId);
    }

    public void deleteFriend(long userId, long friendId) throws UserNotFoundException {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        userStorage.deleteFriend(userId, friendId);
        userStorage.deleteFriend(friendId, userId);
    }

    public List<User> getFriends(long id) throws UserNotFoundException {
        User user = userStorage.findById(id);
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long id1, long id2) throws UserNotFoundException {
        User user1 = userStorage.findById(id1);
        User user2 = userStorage.findById(id2);
        return userStorage.getCommonFriends(id1, id2);
    }

    public User delete(long id) throws UserNotFoundException {
        User user = userStorage.findById(id);
        userStorage.delete(user);
        return user;
    }
}
