package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserActivityEvent;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserActivityStorageInterface;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    @Autowired
    private final UserStorage userStorage;
    @Autowired
    private final FilmStorage filmStorage;
    @Autowired
    @Qualifier("UserActivityDbStorage")
    private final UserActivityStorageInterface userActivityStorage;

    public Review create(Review review) throws ReviewNotFoundException, UserNotFoundException, FilmNotFoundException {
        User testUserExist = userStorage.findById(review.getUserId());
        Film testFilmExist = filmStorage.findById(review.getFilmId());
        long id = reviewStorage.create(review);
        Review addedReview = reviewStorage.findById(id);
        userActivityStorage.save(testUserExist.getId(), UserActivityEvent.EventType.REVIEW, UserActivityEvent.Operation.ADD, addedReview.getReviewId());
        log.info("Добавлен отзыв: {}", addedReview);
        return addedReview;
    }

    public Review update(Review review) throws ReviewNotFoundException {
        Review sourceReview = reviewStorage.findById(review.getReviewId());
        reviewStorage.update(review);
        Review updatedReview = reviewStorage.findById(review.getReviewId());
        userActivityStorage.save(userStorage.findById(updatedReview.getUserId()).getId(), UserActivityEvent.EventType.REVIEW, UserActivityEvent.Operation.UPDATE, updatedReview.getReviewId());
        log.info("Обновлен отзыв {} на {}", sourceReview, updatedReview);
        return updatedReview;
    }

    public void delete(long id) throws ReviewNotFoundException {
        Review testReviewExist = reviewStorage.findById(id);
        reviewStorage.delete(id);
        userActivityStorage.save(userStorage.findById(testReviewExist.getUserId()).getId(), UserActivityEvent.EventType.REVIEW, UserActivityEvent.Operation.REMOVE, testReviewExist.getReviewId());
        log.info("Удален отзыв с id = {}", id);
    }

    public Review findById(long id) throws ReviewNotFoundException {
        return reviewStorage.findById(id);
    }

    public List<Review> findAllByFilmId(long filmId, int count) {
        return reviewStorage.findAllByFilmId(filmId, count);
    }

    public Review addLike(long reviewId, long userId) throws ReviewNotFoundException, UserNotFoundException {
        Review review = reviewStorage.findById(reviewId);
        User user = userStorage.findById(userId);
        reviewStorage.addLike(reviewId, userId);
        log.info("Лайк отзыву {} от пользователя {} добавлен", reviewId, userId);
        return reviewStorage.findById(reviewId);
    }

    public Review addDislike(long reviewId, long userId) throws ReviewNotFoundException, UserNotFoundException {
        Review review = reviewStorage.findById(reviewId);
        User user = userStorage.findById(userId);
        reviewStorage.addDislike(reviewId, userId);
        log.info("Дизлайк отзыву {} от пользователя {} добавлен", reviewId, userId);
        return reviewStorage.findById(reviewId);
    }

    public Review deleteLike(long reviewId, long userId) throws ReviewNotFoundException, UserNotFoundException {
        Review review = reviewStorage.findById(reviewId);
        User user = userStorage.findById(userId);
        reviewStorage.deleteLike(reviewId, userId);
        log.info("Лайк отзыву {} от пользователя {} удален", reviewId, userId);
        return reviewStorage.findById(reviewId);
    }

    public Review deleteDislike(long reviewId, long userId) throws ReviewNotFoundException, UserNotFoundException {
        Review review = reviewStorage.findById(reviewId);
        User user = userStorage.findById(userId);
        reviewStorage.deleteDislike(reviewId, userId);
        log.info("Дизлайк отзыву {} от пользователя {} удален", reviewId, userId);
        return reviewStorage.findById(reviewId);
    }

}
