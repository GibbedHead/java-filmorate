package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film add(@Valid @RequestBody Film film) throws FilmNotFoundException {
        log.info("Запрос создания фильма: {}", film);
        return filmService.create(film);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> findAll() {
        log.info("Запрос всех фильмы");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film findById(@PathVariable long id) throws FilmNotFoundException {
        log.info("Запрос фильма {}", id);
        return filmService.findById(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Valid @RequestBody Film film) throws FilmNotFoundException {
        log.info("Запрос обновления фильма: {}", film);
        return filmService.update(film);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) throws FilmNotFoundException {
        log.info("Запрос удаления фильма с id: {}", id);
        filmService.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable long id, @PathVariable long userId)
            throws FilmNotFoundException, UserNotFoundException {
        log.info("Запрос лайка фильма {} пользователем {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable long id, @PathVariable long userId)
            throws FilmNotFoundException, UserNotFoundException {
        log.info("Запрос удаления лайка фильма {} пользователем {}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getTopLikedFilms(
            @RequestParam(name = "count", defaultValue = "10", required = false) Integer count
    ) {
        log.info("Запрос {} самых популярных фильмов", count);
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/common")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getCommon(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "friendId") Long friendId
    ) {
        log.info("Запрос общих фильмов пользователей {} и {}", userId, friendId);
        return filmService.getCommon(userId, friendId);
    }
}
