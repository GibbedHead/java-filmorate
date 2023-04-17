package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id;

    @Override
    public void create(User user) {
        user.setId(generateId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Пользователь для обновления c id = " + user.getId() + " не найден");
        }
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(long id) throws UserNotFoundException {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new UserNotFoundException(String.format("Пользователь № %d не найден", id));
    }

    @Override
    public void addFriend(long userId, long friendId) {
        users.get(userId).getFriends().add(friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        users.get(userId).getFriends().remove(friendId);
    }

    @Override
    public List<User> getFriends(long id) {
        List<User> friends = new ArrayList<>();
        for (long friendId : users.get(id).getFriends()) {
            friends.add(users.get(friendId));
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(long id1, long id2) {
        Set<Long> commonFriendsIds = new HashSet<>(users.get(id1).getFriends());
        commonFriendsIds.retainAll(users.get(id2).getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (long friendId : commonFriendsIds) {
            commonFriends.add(users.get(friendId));
        }
        return commonFriends;
    }

    private long generateId() {
        return ++id;
    }
}
