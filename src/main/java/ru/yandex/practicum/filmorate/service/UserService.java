package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long userId, long friendId) throws UserNotFoundException {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        userStorage.update(user);
        userStorage.update(friend);
    }

    public void deleteFriend(long userId, long friendId) throws UserNotFoundException {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        userStorage.update(user);
        userStorage.update(friend);
    }

    public List<User> getFriends(long id) throws UserNotFoundException {
        User user = userStorage.findById(id);
        List<User> friends = new ArrayList<>();
        for (long friendId : user.getFriends()) {
            friends.add(userStorage.findById(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(long id1, long id2) throws UserNotFoundException {
        User user1 = userStorage.findById(id1);
        User user2 = userStorage.findById(id2);
        Set<Long> commonFriendsIds = new HashSet<>(user1.getFriends());
        commonFriendsIds.retainAll(user2.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (long friendId : commonFriendsIds) {
            commonFriends.add(userStorage.findById(friendId));
        }
        return commonFriends;
    }

    public User delete(long id) throws UserNotFoundException {
        User user = userStorage.findById(id);
        userStorage.delete(user);
        return user;
    }
}
