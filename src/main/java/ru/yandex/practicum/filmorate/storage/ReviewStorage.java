package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    long create(Review review) throws ReviewNotFoundException;

    void update(Review review) throws ReviewNotFoundException;

    void delete(long id);

    Review findById(long id) throws ReviewNotFoundException;

    List<Review> findAllByFilmId(long filmId, int count);
    void addLike(long reviewId, long userId);
    void addDislike(long reviewId, long userId);
    void deleteLike(long reviewId, long userId);
    void deleteDislike(long reviewId, long userId);
}
