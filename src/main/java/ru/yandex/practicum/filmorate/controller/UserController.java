package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserRepository users = new UserRepository();

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        users.save(user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @GetMapping
    public List<User> list() {
        return users.getAll();
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        users.update(user);
        log.info("Обновлен пользователь: {}", user);
        return user;
    }

}
