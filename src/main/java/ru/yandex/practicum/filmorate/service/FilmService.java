package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void create(Film film) {
        filmStorage.create(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(long id) throws FilmNotFoundException {
        return filmStorage.findById(id);
    }

    public Film update(Film film) throws FilmNotFoundException {
        filmStorage.update(film);
        return film;
    }

    public void addLike(long filmId, long userId) throws FilmNotFoundException, UserNotFoundException {
        filmStorage.addLike(filmStorage.findById(filmId).getId(), userStorage.findById(userId).getId());
    }

    public void deleteLike(long filmId, long userId) throws FilmNotFoundException, UserNotFoundException {
        filmStorage.deleteLike(filmStorage.findById(filmId).getId(), userStorage.findById(userId).getId());
    }

    public List<Film> getTopLikedFilms(long count) {
        return filmStorage.getTopLikedFilms(count);
    }

    public Film delete(long id) throws FilmNotFoundException {
        Film film = filmStorage.findById(id);
        filmStorage.delete(film);
        return film;
    }
}
