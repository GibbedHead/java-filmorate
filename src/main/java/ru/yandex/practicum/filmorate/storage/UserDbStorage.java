package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {
    @Override
    public void create(User user) {

    }

    @Override
    public void update(User user) throws UserNotFoundException {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findById(long id) throws UserNotFoundException {
        return null;
    }
}
