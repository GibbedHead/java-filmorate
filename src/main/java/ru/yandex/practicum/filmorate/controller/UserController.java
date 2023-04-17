package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final InMemoryUserStorage users = new InMemoryUserStorage();

    @PostMapping
    public @ResponseBody User add(@Valid @RequestBody User user) {
        users.add(user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @GetMapping
    public @ResponseBody List<User> getAll() {
        return users.getAll();
    }

    @PutMapping
    public @ResponseBody User update(@Valid @RequestBody User user) throws ValidationException {
        users.update(user);
        log.info("Обновлен пользователь: {}", user);
        return user;
    }

}
