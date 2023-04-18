package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
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
    public List<Film> findAll() {
        log.info("Показываем все фильмы");
        return filmStorage.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable long id) throws FilmNotFoundException {
        log.info("Показываем фильм {}", id);
        return filmStorage.findById(id);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws FilmNotFoundException {
        filmStorage.update(film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @DeleteMapping("/{id}")
    public Film delete(@PathVariable long id) throws FilmNotFoundException {
        log.info("Удален фильм с id: {}", id);
        return filmService.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId)
            throws FilmNotFoundException, UserNotFoundException {
        filmService.addLike(id, userId);
        log.info("Фильм {} лайкнут пользователем {}", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId)
            throws FilmNotFoundException, UserNotFoundException {
        filmService.deleteLike(id, userId);
        log.info("Фильм {} разлайкнут пользователем {}", id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopLikedFilms(
            @RequestParam(name = "count", defaultValue = "10", required = false) Long count
    ) {
        log.info("Показываем {} самых популярных фильмов", count);
        return filmService.getTopLikedFilms(count);
    }
}
