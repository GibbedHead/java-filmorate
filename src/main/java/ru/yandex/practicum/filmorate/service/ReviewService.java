package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;

    public Review create(Review review) {
        //throws FilmNotFoundException {
        long id = reviewStorage.create(review);
        Review addedReview = reviewStorage.findById(id);
        log.info("Добавлен отзыв: {}", addedReview);
        return addedReview;
    }

    public Review update(Review review) {
        // throws FilmNotFoundException {
        Review sourceReview = reviewStorage.findById(review.getId());
        reviewStorage.update(review);
        Review updatedReview = reviewStorage.findById(review.getId());
        log.info("Обновлен отзыв {} на {}", sourceReview, updatedReview);
        return updatedReview;
    }

    public void delete(long id) {
        // throws FilmNotFoundException {
        Review testReviewExist = reviewStorage.findById(id);
        reviewStorage.delete(id);
        log.info("Удален отзыв с id = {}", id);
    }

    public Review findById(long id) {
        //throws FilmNotFoundException {
        return reviewStorage.findById(id);
    }

    public List<Review> findAllByFilmId(long filmId, int count) {
        return reviewStorage.findAllByFilmId(filmId, count);
    }

}
