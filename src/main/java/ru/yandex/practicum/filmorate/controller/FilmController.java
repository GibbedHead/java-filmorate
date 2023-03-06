package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        int id = getNewId();
        film.setId(id);
        films.put(id, film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> list() {
        return new ArrayList<>(films.values());
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлен фильм: {}", film);
            return film;
        }
        log.warn("Ошибка обновления фильма: {}", film);
        throw new ValidationException("Фильм для обновления c id = " + film.getId() + " не найден");
    }

    public int getNewId() {
        return nextId++;
    }
}
