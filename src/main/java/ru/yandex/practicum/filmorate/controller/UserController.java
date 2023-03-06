package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        int id = getNewId();
        user.setId(id);
        users.put(id, user);
        log.info("Добавлен пользователь: {}", user);
        return user;
    }

    @GetMapping
    public List<User> list() {
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Обновлен пользователь: {}", user);
            return user;
        }
        log.warn("Ошибка обновления пользователя: {}", user);
        throw new ValidationException("Пользователь для обновления c id = " + user.getId() + " не найден");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            String rejectedValue = ((FieldError) error).getRejectedValue().toString();
            errors.put(fieldName, errorMessage);
            log.warn("Поле '" + fieldName
                    + "' со значением '" + rejectedValue
                    +  "' нарушило правило '" + errorMessage + "'");
        });
        return errors;
    }

    public int getNewId() {
        return nextId++;
    }
}
