package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmRepository films = new FilmRepository();

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        films.save(film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> list() {
        return films.getAll();
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        films.update(film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

}
