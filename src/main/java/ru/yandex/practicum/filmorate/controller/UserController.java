package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStorage userStorage;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        userStorage.create(user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @GetMapping
    public List<User> getAll() {
        return userStorage.findAll();
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        userStorage.update(user);
        log.info("Обновлен пользователь: {}", user);
        return user;
    }

}
