package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
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
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        users.put(user.getId(), user);
    }

    @Override
    public void update(User user) throws UserNotFoundException {
        if (users.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getFriends() == null) {
                user.setFriends(new HashSet<>());
            }
            users.put(user.getId(), user);
        } else {
            throw new UserNotFoundException("Пользователь для обновления c id = " + user.getId() + " не найден");
        }
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
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

    private long generateId() {
        return ++id;
    }
}
