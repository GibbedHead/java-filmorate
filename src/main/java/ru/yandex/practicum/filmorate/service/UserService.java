package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserActivityEvent;
import ru.yandex.practicum.filmorate.storage.UserActivityStorageInterface;
=======
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;


    @Autowired
    @Qualifier("UserActivityDbStorage")
    private final UserActivityStorageInterface userActivityStorage;

    public UserService(UserStorage userStorage, UserActivityStorageInterface userActivityStorage) {
        this.userStorage = userStorage;
        this.userActivityStorage = userActivityStorage;

    public UserService(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public User create(User user) throws UserNotFoundException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        long id = userStorage.create(user);
        User addedUser = userStorage.findById(id);
        log.info("Добавлен пользователь: {}", addedUser);
        return addedUser;
    }

    public User update(User user) throws UserNotFoundException {
        User sourceUser = userStorage.findById(user.getId());
        userStorage.update(user);
        User updatedUser = userStorage.findById(user.getId());
        log.info("Обновлен пользователь c {} на {}", sourceUser, updatedUser);
        return updatedUser;
    }

    public void delete(long id) throws UserNotFoundException {
        User testUserExist = userStorage.findById(id);
        userStorage.delete(id);
        log.info("Удален пользователь id = {}", id);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(long id) throws UserNotFoundException {
        return userStorage.findById(id);
    }

    public void addFriend(long user1Id, long user2Id) throws UserNotFoundException {
        User user1 = userStorage.findById(user1Id);
        User user2 = userStorage.findById(user2Id);
        userStorage.createOrConfirmFriendship(user1Id, user2Id);
        userActivityStorage.save(user1Id, UserActivityEvent.EventType.FRIEND, UserActivityEvent.Operation.ADD, user2Id);
    }

    public void deleteFriend(long user1Id, long user2Id) throws UserNotFoundException {
        User user1 = userStorage.findById(user1Id);
        User user2 = userStorage.findById(user2Id);
        userStorage.deleteFriend(user1Id, user2Id);
        userActivityStorage.save(user1Id, UserActivityEvent.EventType.FRIEND, UserActivityEvent.Operation.REMOVE, user2Id);
        log.info("Дружба {} и {} удалена", user1Id, user2Id);
    }

    public List<User> getFriends(long id) throws UserNotFoundException {
        User user = userStorage.findById(id);
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long user1Id, long user2Id) throws UserNotFoundException {
        User user1 = userStorage.findById(user1Id);
        User user2 = userStorage.findById(user2Id);
        List<User> friends1 = getFriends(user1Id);
        List<User> friends2 = getFriends(user2Id);
        friends1.retainAll(friends2);
        return friends1;
    }
}
