package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserActivityEvent;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserActivityStorageInterface;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class FilmService {

    @Qualifier("FilmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;
    @Qualifier("UserActivityDbStorage")
    private final UserActivityStorageInterface userActivityStorage;

    private final DirectorStorage directorStorage;

    public Film create(Film film) throws FilmNotFoundException {
        long id = filmStorage.create(film);
        Film addedFilm = filmStorage.findById(id);
        log.info("Добавлен фильм: {}", addedFilm);
        return addedFilm;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(long id) throws FilmNotFoundException {
        return filmStorage.findById(id);
    }

    public Film update(Film film) throws FilmNotFoundException {
        Film sourceFilm = filmStorage.findById(film.getId());
        filmStorage.update(film);
        Film updatedFilm = filmStorage.findById(film.getId());
        log.info("Обновлен фильм c {} на {}", sourceFilm, updatedFilm);
        return updatedFilm;
    }

    public void addLike(long filmId, long userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);
        filmStorage.addLike(filmId, userId);
        userActivityStorage.save(userId, UserActivityEvent.EventType.LIKE, UserActivityEvent.Operation.ADD, filmId);
        log.info("Лайк фильма {} пользователем {} добавлен", filmId, userId);
    }

    public void deleteLike(long filmId, long userId) throws FilmNotFoundException, UserNotFoundException {
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);
        filmStorage.deleteLike(filmId, userId);
        userActivityStorage.save(userId, UserActivityEvent.EventType.LIKE, UserActivityEvent.Operation.REMOVE, filmId);
        log.info("Лайк фильма {} пользователем {} удален", filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count, Long genreId, String year) {
        return filmStorage.getPopular(count, genreId, year);
    }

    public void delete(long id) throws FilmNotFoundException {
        Film testFilmExist = filmStorage.findById(id);
        filmStorage.delete(id);
        log.info("Удален фильм id = {}", id);
    }

    public List<Film> getCommon(long userId, long friendId) {
        return filmStorage.getCommon(userId, friendId);
    }


    public List<Film> getDirectorsFilms(Long directorId, String param) {
        directorStorage.findById(directorId);
        List<Film> films;
        if (param.equals("noParam")) {
            films = filmStorage.findFilmsByDirectorId(directorId);
        } else if (param.equals("year") || param.equals("likes")) {
            films = filmStorage.findFilmsByDirectorId(directorId, param);
        } else {
            log.error("Invalid search query films by director's id with parameter: {}", param);
            throw new IllegalArgumentException("Invalid search query films by director's id with parameter: " + param);
        }
        return films;
    }

    public List<Film> searchFilm(String query, String by) {
        return filmStorage.search(query, by);
    }
}
