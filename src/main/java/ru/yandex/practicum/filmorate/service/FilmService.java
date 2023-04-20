package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);
        film.getLikes().add(user.getId());
        filmStorage.update(film);
    }

    public void deleteLike(long filmId, long userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);
        film.getLikes().remove(user.getId());
        filmStorage.update(film);
    }

    public List<Film> getTopLikedFilms(long count) {
        return filmStorage.findAll().stream()
                .sorted(
                        Comparator.comparingInt(
                                (Film f) -> f.getLikes().size()
                        ).reversed()
                )
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film delete(long id) throws FilmNotFoundException {
        Film film = filmStorage.findById(id);
        filmStorage.delete(film);
        return film;
    }
}
