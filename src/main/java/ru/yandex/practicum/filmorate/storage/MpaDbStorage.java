package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("MpaDbStorage")
@AllArgsConstructor
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        String sql = "" +
                "SELECT" +
                "  * " +
                "FROM" +
                "  MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Mpa findById(long id) throws MpaNotFoundException {
        String sql = "" +
                "SELECT" +
                "  * " +
                "FROM " +
                "  MPA " +
                "WHERE " +
                "  MPA_ID = ? " +
                "LIMIT " +
                "  1";
        List<Mpa> mpaList = jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), id);
        if (mpaList.isEmpty()) {
            log.info("Mpa id = {} не найден", id);
            throw new MpaNotFoundException(String.format("Mpa id = %d не найден", id));
        } else {
            return mpaList.get(0);
        }
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getLong("MPA_ID"), rs.getString("MPA_NAME"));
    }
}
