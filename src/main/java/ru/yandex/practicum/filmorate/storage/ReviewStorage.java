package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    long create(Review review);

    Review update(Review review);

    void delete(long id);

    Review findById(long id) throws ReviewNotFoundException;

    List<Review> findAllByFilmId(long filmId, int count);
}
