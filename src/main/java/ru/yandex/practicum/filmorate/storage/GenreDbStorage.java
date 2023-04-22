package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component("GenreDbStorage")
@AllArgsConstructor
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        String sql = "" +
                "SELECT" +
                "  * " +
                "FROM" +
                "  GENRE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Genre findById(long id) throws GenreNotFoundException {
        String sql = "" +
                "SELECT" +
                "  * " +
                "FROM " +
                "  GENRE " +
                "WHERE " +
                "  GENRE_ID = ? " +
                "LIMIT " +
                "  1";
        List<Genre> userList = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
        if (userList.isEmpty()) {
            log.info("Жанр id = {} не найден", id);
            throw new GenreNotFoundException(String.format("Жанр id = %d не найден", id));
        } else {
            return userList.get(0);
        }
    }

    @Override
    public Set<Genre> findByFilmId(long id) {
        String sql = "" +
                "SELECT " +
                "  g.* " +
                "FROM " +
                "  GENRE g " +
                "  LEFT JOIN FILM_GENRE fg ON fg.GENRE_ID = g.GENRE_ID " +
                "  LEFT JOIN FILM f ON f.FILM_ID = fg.FILM_ID " +
                "WHERE " +
                "  f.FILM_ID = ? " +
                "ORDER BY g.GENRE_ID ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id)
                .stream()
                .sorted(Comparator.comparingLong(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("GENRE_ID"), rs.getString("GENRE_NAME"));
    }
}
