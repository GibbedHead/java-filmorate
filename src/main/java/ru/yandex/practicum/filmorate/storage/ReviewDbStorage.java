package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
@Slf4j
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public long create(Review review) {
        String reviewSql = "INSERT INTO PUBLIC.REVIEWS(CONTENT, IS_POSITIVE, USER_ID, FILM_ID) " +
                "VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(reviewSql, new String[]{"REVIEW_ID"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.isPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            return stmt;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void update(Review review) {
        String updateReviewSql = "UPDATE " +
                "  PUBLIC.REVIEWS " +
                "SET " +
                "  CONTENT = ?, " +
                "  IS_POSITIVE = ?, " +
                "  USER_ID = ?, " +
                "  FILM_ID = ? " +
                "WHERE " +
                "  REVIEW_ID = ?";
        jdbcTemplate.update(
                updateReviewSql,
                review.getContent(),
                review.isPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getId()
        );
    }

    @Override
    public void delete(long id) {
        String sql = "" +
                "DELETE " +
                "FROM " +
                "  PUBLIC.REVIEWS " +
                "WHERE " +
                "  REVIEW_ID = ? ";
        jdbcTemplate.update(sql, id);
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
        String sql = "" +
                "SELECT * " +
                "FROM PUBLIC.REVIEWS " +
                "WHERE FILM_ID = ? " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs), filmId, count);
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
