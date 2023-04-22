package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("GENRE_ID"), rs.getString("GENRE_NAME"));
    }
}
