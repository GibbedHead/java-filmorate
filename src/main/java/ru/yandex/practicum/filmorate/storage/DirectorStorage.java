package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    public Director save(Director director) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            final PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO DIRECTORS (NAME) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, director.getName());
            return ps;
        }, kh);

        Long directorId = kh.getKeyAs(Long.class);
        director.setId(directorId);

        return director;
    }

    public Director findById(Long id) {
        try {
            Optional<Director> director = Optional.ofNullable(jdbcTemplate.queryForObject(
                    "SELECT * FROM DIRECTORS WHERE DIRECTOR_ID = ?",
                    (rs, rowNum) -> Director.builder()
                            .id(rs.getLong("director_id"))
                            .name(rs.getString("name"))
                            .build(), id));
            return director.get();
        } catch (EmptyResultDataAccessException ex) {
            log.error("Director with id='{}' not found", id);
            throw new DirectorNotFoundException("Director with id='" + id + "' not found");
        }

    }

    public Director update(Director director) {
        if (!isExistDirectorById(director.getId())) {
            log.error("Director with id='{}' not found", director.getId());
            throw new DirectorNotFoundException("Director with id='" + director.getId() + "' not found");
        }

        int update = jdbcTemplate.update(
                "UPDATE DIRECTORS SET NAME = ? WHERE DIRECTOR_ID = ?",
                director.getName(),
                director.getId());

        if (update == 0) {
            return null;
        }

        return director;
    }

    public List<Director> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM DIRECTORS",
                (rs, rowNum) -> Director.builder()
                        .id(rs.getLong("director_id"))
                        .name(rs.getString("name"))
                        .build());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM DIRECTORS WHERE DIRECTOR_ID = ?", id);
    }

    private boolean isExistDirectorById(Long id) {
        return Boolean.TRUE.equals(jdbcTemplate
                .queryForObject("SELECT COUNT(director_id) > 0 FROM DIRECTORS WHERE director_id = ?",
                        Boolean.class, id));
    }
}
