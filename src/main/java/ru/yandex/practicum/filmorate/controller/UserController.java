package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> users = new ArrayList<>();
    private int nextId = 1;

    @PostMapping
    public User add(@RequestBody User user) {
        if (!users.contains(user)) {
            users.add(user);
            user.setId(getNewId());
        }
        return user;
    }

    @GetMapping
    public List<User> list() {
        return users;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (users.contains(user)) {
            users.set(users.indexOf(user), user);
            return user;
        }
        users.add(user);
        return user;
    }

    public int getNewId() {
        return nextId++;
    }
}
