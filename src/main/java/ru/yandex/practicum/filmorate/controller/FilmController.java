package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final InMemoryFilmStorage films = new InMemoryFilmStorage();

    @PostMapping
    public @ResponseBody Film add(@Valid @RequestBody Film film) {
        films.add(film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @GetMapping
    public @ResponseBody List<Film> getAll() {
        return films.getAll();
    }

    @PutMapping
    public @ResponseBody Film update(@Valid @RequestBody Film film) throws ValidationException {
        films.update(film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

}
