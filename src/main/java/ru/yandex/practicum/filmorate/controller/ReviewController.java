package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review add(@Valid @RequestBody Review review) throws ReviewNotFoundException {
        log.info("Запрос создания отзыва: {}", review);
        return reviewService.create(review);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Review update(@Valid @RequestBody Review review) throws ReviewNotFoundException {
        log.info("Запрос обновления отзыва: {}", review);
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) throws ReviewNotFoundException {
        log.info("Запрос удаления отзыва с id: {}", id);
        reviewService.delete(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Review findById(@PathVariable long id) throws ReviewNotFoundException {
        log.info("Запрос отзыва с id: {}", id);
        return reviewService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Review> findAllByFilmId(@RequestParam (name = "filmId", required = true) Long filmId,
            @RequestParam(name = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Запрос {} отзывов фильма с id {} ", count, filmId);
        return reviewService.findAllByFilmId(filmId, count);
    } // тут подумать над тем, что если не указан id фильма - запрос выдает все фильмы

    // + добавить эндпоинты для добавления like/dislike на отзыв!!!


}
