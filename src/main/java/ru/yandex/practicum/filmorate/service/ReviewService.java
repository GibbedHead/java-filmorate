package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    @Autowired
    @Qualifier("UserDbStorage")
    private final UserStorage userStorage;

    public Review create(Review review) throws ReviewNotFoundException {
        long id = reviewStorage.create(review);
        Review addedReview = reviewStorage.findById(id);
        log.info("Добавлен отзыв: {}", addedReview);
        return addedReview;
    }

    public Review update(Review review) throws ReviewNotFoundException {
        Review sourceReview = reviewStorage.findById(review.getId());
        reviewStorage.update(review);
        Review updatedReview = reviewStorage.findById(review.getId());
        log.info("Обновлен отзыв {} на {}", sourceReview, updatedReview);
        return updatedReview;
    }

    public void delete(long id) throws ReviewNotFoundException {
        Review testReviewExist = reviewStorage.findById(id);
        reviewStorage.delete(id);
        log.info("Удален отзыв с id = {}", id);
    }

    public Review findById(long id) throws ReviewNotFoundException {
        return reviewStorage.findById(id);
    }

    public List<Review> findAllByFilmId(long filmId, int count) {
        return reviewStorage.findAllByFilmId(filmId, count);
    }

    public void addLike(long reviewId, long userId) throws ReviewNotFoundException, UserNotFoundException {
        Review review = reviewStorage.findById(reviewId);
        User user = userStorage.findById(userId);
        reviewStorage.addLike(reviewId, userId);
        log.info("Лайк отзыва {} пользователем {} добавлен", reviewId, userId);
    }

    public void addDislike(long reviewId, long userId) throws ReviewNotFoundException, UserNotFoundException {
        Review review = reviewStorage.findById(reviewId);
        User user = userStorage.findById(userId);
        reviewStorage.addLike(reviewId, userId);
        log.info("Дизлайк отзыва {} пользователем {} добавлен", reviewId, userId);
    }

    public void deleteLike(long reviewId, long userId) throws ReviewNotFoundException, UserNotFoundException {
        Review review = reviewStorage.findById(reviewId);
        User user = userStorage.findById(userId);
        reviewStorage.deleteLikeOrDislike(reviewId, userId);
        log.info("Лайк отзыва {} пользователем {} удален", reviewId, userId);
    }

    public void deleteDislike(long reviewId, long userId) throws ReviewNotFoundException, UserNotFoundException {
        Review review = reviewStorage.findById(reviewId);
        User user = userStorage.findById(userId);
        reviewStorage.deleteLikeOrDislike(reviewId, userId);
        log.info("Дизлайк отзыва {} пользователем {} удален", reviewId, userId);
    }

}
