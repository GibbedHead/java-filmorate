package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User add(@Valid @RequestBody User user) throws UserNotFoundException {
        log.info("Запрос создания пользователя: {}", user);
        return userService.create(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        log.info("Запрос всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User findById(@PathVariable long id) throws UserNotFoundException {
        log.info("Запрос пользователя id = {}", id);
        return userService.findById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Valid @RequestBody User user) throws UserNotFoundException {
        log.info("Запрос обновления пользователя: {}", user);
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) throws UserNotFoundException {
        log.info("Запрос удаления пользователя с id: {}", id);
        userService.delete(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable long id, @PathVariable long friendId) throws UserNotFoundException {
        log.info("Запрос создания дружбы {} и {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Запрос удаления дружбы {} и {}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable long id) {
        log.info("Запрос всех друзей пользователя {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Запрос общих друзей пользователя {} и {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
