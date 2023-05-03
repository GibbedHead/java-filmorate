package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    long create(Film film);

    void update(Film film) throws FilmNotFoundException;

    void delete(long id);

    List<Film> findAll();

    Film findById(long id) throws FilmNotFoundException;

    List<Film> getPopular(int count);

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);
}
