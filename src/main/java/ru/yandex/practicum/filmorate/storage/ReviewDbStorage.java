package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public long create(Review review) {
        return 0;
    }

    @Override
    public Review update(Review review) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Review findById(long id) {
        return null;
    }

    @Override
    public List<Review> findAllByFilmId(long filmId, int count) {
        return null;
    }

}
