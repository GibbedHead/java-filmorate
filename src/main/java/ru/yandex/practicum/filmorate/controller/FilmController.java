package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private int nextId = 1;

    @PostMapping
    public Film add(@RequestBody Film film) {
        if (!films.contains(film)) {
            films.add(film);
            film.setId(getNewId());
        }
        return film;
    }

    @GetMapping
    public List<Film> list() {
        return films;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (films.contains(film)) {
            films.set(films.indexOf(film), film);
            return film;
        }
        films.add(film);
        return film;
    }

    public int getNewId() {
        return nextId++;
    }
}
