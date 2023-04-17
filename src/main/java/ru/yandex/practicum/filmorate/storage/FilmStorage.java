package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void create(Film film);
    void update(Film film) throws ValidationException;
    void delete(Film film);
    List<Film> findAll();
    Film findById(long id) throws FilmNotFoundException;
}
