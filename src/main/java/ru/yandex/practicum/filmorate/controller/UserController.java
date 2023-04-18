package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
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
    public List<User> findAll() {
        log.info("Показываем всех пользователей");
        return userStorage.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable long id) throws UserNotFoundException {
        log.info("Показываем пользователя: {}", id);
        return userStorage.findById(id);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws UserNotFoundException {
        userStorage.update(user);
        log.info("Обновлен пользователь: {}", user);
        return user;
    }

    @DeleteMapping("/{id}")
    public User delete(@PathVariable long id) throws UserNotFoundException {
        log.info("Удален пользователь {}", id);
        return userService.delete(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) throws UserNotFoundException {
        log.info("Пользователи {} и {} подружились", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) throws UserNotFoundException {
        log.info("Пользователи {} и {} перестали дружить", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) throws UserNotFoundException {
        log.info("Показываем всех друзей пользователя {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) throws UserNotFoundException {
        log.info("Показываем общих друзей пользователя {} и {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
