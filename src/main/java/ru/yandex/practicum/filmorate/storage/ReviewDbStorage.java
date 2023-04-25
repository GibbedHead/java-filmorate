package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    public Review findById(long id) throws ReviewNotFoundException {
        String sql = "SELECT * " +
                "FROM PUBLIC.REVIEWS " +
                "WHERE REVIEW_ID = ? ";

        List<Review> reviews = jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs), id);
        if (reviews.isEmpty()) {
            throw new ReviewNotFoundException(String.format("Отзыв с id = %d не найден", id));
        }
        return reviews.get(0);
    }

    @Override
    public List<Review> findAllByFilmId(long filmId, int count) {
        return null;
    }

    private Review makeReview(ResultSet rs) throws SQLException {

        return new Review(
                rs.getLong("REVIEW_ID"),
                rs.getString("CONTENT"),
                rs.getBoolean("IS_POSITIVE"),
                rs.getLong("USER_ID"),
                rs.getLong("FILM_ID"),
                rs.getInt("USEFUL")
        );
    }

}
