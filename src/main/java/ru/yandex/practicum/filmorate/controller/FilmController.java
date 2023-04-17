package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        filmStorage.create(film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        return filmStorage.findAll();
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws FilmNotFoundException {
        filmStorage.update(film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

}
