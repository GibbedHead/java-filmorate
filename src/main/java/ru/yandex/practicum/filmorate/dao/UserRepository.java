package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long id;

    public void save(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
    }

    public void update(User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Пользователь для обновления c id = " + user.getId() + " не найден");
        }
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    private long generateId() {
        return ++id;
    }
}
