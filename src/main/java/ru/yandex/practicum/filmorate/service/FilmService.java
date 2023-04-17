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

    public void addLike(long filmId, long userId) throws FilmNotFoundException, UserNotFoundException {
        filmStorage.addLike(filmStorage.findById(filmId).getId(), userStorage.findById(userId).getId());
    }

    public void deleteLike(long filmId, long userId) throws FilmNotFoundException, UserNotFoundException {
        filmStorage.deleteLike(filmStorage.findById(filmId).getId(), userStorage.findById(userId).getId());
    }

    public List<Film> getTopLikedFilms() {
        return filmStorage.getTopLikedFilms();
    }
}
