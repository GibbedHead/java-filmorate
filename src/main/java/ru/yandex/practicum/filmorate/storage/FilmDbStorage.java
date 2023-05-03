package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component("FilmDbStorage")
@AllArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    @Override
    public long create(Film film) {
        String filmSql = "INSERT INTO PUBLIC.FILM (NAME,DESCRIPTION,RELEASE_DATE,DURATION,MPA_ID) " +
                "VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(filmSql, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        if (film.getGenres() != null) {
            addFilmGenre(id, film.getGenres());
        }

        if (film.getDirectors() != null) {
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update("INSERT INTO DIRECTORS_FILMS (director_id, film_id) VALUES (?, ?)",
                        director.getId(), id);
            }
        }
        return id;
    }

    private void addFilmGenre(long filmId, Set<Genre> genres) {
        String genresSql = "INSERT INTO PUBLIC.FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for (Genre genre : genres) {
            Object[] args = {filmId, genre.getId()};
            batchArgs.add(args);
        }
        jdbcTemplate.batchUpdate(genresSql, batchArgs);
    }

    @Override
    public void update(Film film) throws FilmNotFoundException {
        String deleteGenreSql = "DELETE FROM " +
                "  FILM_GENRE " +
                "WHERE " +
                "  FILM_ID = ?";
        jdbcTemplate.update(deleteGenreSql, film.getId());

        String updateFilmSql = "UPDATE " +
                "  FILM " +
                "SET " +
                "  NAME = ?, " +
                "  DESCRIPTION = ?, " +
                "  RELEASE_DATE = ?, " +
                "  DURATION = ?, " +
                "  MPA_ID = ? " +
                "WHERE " +
                "  FILM_ID = ?";
        jdbcTemplate.update(
                updateFilmSql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        insertFilmGenres(film.getId(), film.getGenres());

        jdbcTemplate.update("DELETE FROM DIRECTORS_FILMS WHERE FILM_ID = ?",
                film.getId());
        if (film.getDirectors() != null) {
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update("INSERT INTO DIRECTORS_FILMS (director_id, film_id) VALUES (?, ?)",
                        director.getId(), film.getId());
            }
        }

        setDirectorsFilmByFilmsIds(Map.of(film.getId(), film));
    }

    private void insertFilmGenres(long filmId, Set<Genre> genres) {
        if (genres != null) {
            for (Genre genre : genres) {
                jdbcTemplate.update(
                        "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)",
                        filmId,
                        genre.getId()
                );
            }
        }
    }

    @Override
    public void delete(long id) {
        String sql = "" +
                "DELETE " +
                "FROM " +
                "  FILM " +
                "WHERE " +
                "  FILM_ID = ? ";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT f.*, m.MPA_ID, m.MPA_NAME " +
                "FROM PUBLIC.FILM f " +
                "LEFT JOIN PUBLIC.MPA m ON f.MPA_ID = m.MPA_ID " +
                "GROUP BY f.FILM_ID, m.MPA_ID, m.MPA_NAME ";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        Map<Long, Film> mapFilms = films
                .stream()
                .collect(Collectors.toMap(Film::getId, film -> film));
        setDirectorsFilmByFilmsIds(mapFilms);

        return films;
    }

    @Override
    public Film findById(long id) throws FilmNotFoundException {
        String sql = "SELECT f.*, m.MPA_ID, m.MPA_NAME " +
                "FROM PUBLIC.FILM f " +
                "LEFT JOIN PUBLIC.MPA m ON f.MPA_ID = m.MPA_ID " +
                "WHERE f.FILM_ID = ? " +
                "GROUP BY f.FILM_ID, m.MPA_ID, m.MPA_NAME";

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id);
        if (films.isEmpty()) {
            throw new FilmNotFoundException(String.format("Фильм id = %d не найден", id));
        }
        Film film = films.get(0);
        setDirectorsFilmByFilmsIds(Map.of(film.getId(), film));
        return film;
    }

    @Override
    public List<Film> getPopular(Integer count, Long genreId, String year) {
        List<Object> argList = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("" +
                "SELECT " +
                "  f.*, " +
                "  m.*, " +
                "  COUNT(DISTINCT fl.USER_ID) as likes_count " +
                "FROM " +
                "  FILM f " +
                "  LEFT JOIN FILM_LIKES fl ON f.FILM_ID = fl.FILM_ID " +
                "  LEFT JOIN MPA m ON f.MPA_ID = m.MPA_ID " +
                "  LEFT JOIN FILM_GENRE fg ON f.FILM_ID = fg.FILM_ID " +
                "  LEFT JOIN GENRE g ON fg.GENRE_ID = g.GENRE_ID " +
                "WHERE 1 "
        );
        if (genreId != null) {
            sqlBuilder.append("AND fg.GENRE_ID = ? ");
            argList.add(genreId);
        }
        if (year != null) {
            sqlBuilder.append("AND YEAR(f.RELEASE_DATE) = ? ");
            argList.add(year);
        }
        sqlBuilder.append(
                "GROUP BY " +
                        "  f.FILM_ID " +
                        "ORDER BY " +
                        "  likes_count DESC " +
                        "LIMIT ?"
        );
        argList.add(count);
        List<Film> films = jdbcTemplate.query(sqlBuilder.toString(), (rs, rowNum) -> makeFilm(rs), argList.toArray(new Object[0]));
        Map<Long, Film> mapFilms = films
                .stream()
                .collect(Collectors.toMap(Film::getId, film -> film));
        setDirectorsFilmByFilmsIds(mapFilms);

        return films;
    }

    @Override
    public void addLike(long filmId, long userId) {
        String checkLikeSql = "SELECT COUNT(*) FROM PUBLIC.FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        if (jdbcTemplate.queryForObject(checkLikeSql, Integer.class, filmId, userId) > 0) {
            return;
        }
        String addLikeSql = "INSERT INTO PUBLIC.FILM_LIKES (FILM_ID, USER_ID) " +
                "VALUES " +
                "  (?, ?)";
        jdbcTemplate.update(addLikeSql, filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String addLikeSql = "DELETE FROM " +
                "  PUBLIC.FILM_LIKES " +
                "WHERE " +
                "  FILM_ID = ? " +
                "  AND USER_ID = ?";
        jdbcTemplate.update(addLikeSql, filmId, userId);
    }

    @Override
    public List<Film> getCommon(long userId, long friendId) {
        String sql = "" +
                "SELECT " +
                "  DISTINCT f.*, " +
                "  m.*, " +
                "  COUNT(DISTINCT fl.USER_ID) as likes_count " +
                "FROM " +
                "  FILM_LIKES fl " +
                "  LEFT JOIN FILM f ON f.FILM_ID = fl.FILM_ID " +
                "  LEFT JOIN MPA m ON f.MPA_ID = m.MPA_ID " +
                "WHERE " +
                "  fl.FILM_ID IN (" +
                "    SELECT " +
                "      fl2.FILM_ID " +
                "    FROM " +
                "      FILM_LIKES fl2 " +
                "    WHERE " +
                "      FL2.USER_ID = ?" +
                "  ) " +
                "  AND fl.FILM_ID IN (" +
                "    SELECT " +
                "      fl2.FILM_ID " +
                "    FROM " +
                "      FILM_LIKES fl2 " +
                "    WHERE " +
                "      FL2.USER_ID = ?" +
                "  )" +
                "GROUP BY " +
                "  f.FILM_ID " +
                "ORDER BY " +
                "  likes_count DESC ";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), userId, friendId);
    }

    @Override
    public List<Film> getFilmRecommendations(long userId) {
        String sqlQuery = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, m.MPA_ID, m.MPA_NAME " +
                "FROM FILM_LIKES fl1 " +
                "JOIN FILM_LIKES fl2 ON fl2.FILM_ID = fl1.FILM_ID AND fl1.USER_ID = ? " +
                "JOIN FILM_LIKES fl3 ON fl3.USER_ID = fl2.USER_ID AND fl3.USER_ID <> ? " +
                "LEFT JOIN FILM_LIKES fl4 ON fl4.USER_ID = ? AND fl4.FILM_ID = fl3.FILM_ID " +
                "JOIN FILM f ON f.FILM_ID = fl3.FILM_ID " +
                "JOIN MPA m ON f.MPA_ID = m.MPA_ID " +
                "WHERE fl4.FILM_ID IS NULL OR fl4.USER_ID IS NULL " +
                "GROUP BY fl3.FILM_ID, f.FILM_ID " +
                "ORDER BY COUNT(*) DESC";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), userId, userId, userId);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film(
                rs.getLong("FILM_ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION")
        );

        Mpa mpa = new Mpa(rs.getLong("MPA_ID"), rs.getString("MPA_NAME"));
        film.setMpa(mpa);
        film.setGenres(genreStorage.findByFilmId(film.getId()));

        return film;
    }

    private void setDirectorsFilmByFilmsIds(Map<Long, Film> films) {
        class Row {
            Long filmId;
            Long directorId;
            String directorName;
        }

        String inSql = String.join(",", Collections.nCopies(films.keySet().size(), "?"));

        List<Row> result = jdbcTemplate.query(
                String.format("SELECT fd.FILM_ID, d.director_id, d.name as director_name FROM DIRECTORS_FILMS fd " +
                        "INNER JOIN DIRECTORS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID WHERE fd.FILM_ID in (%s)", inSql),
                films.keySet().toArray(),
                (rs, rowNum) -> {
                    Row row = new Row();
                    row.filmId = rs.getLong("film_Id");
                    row.directorId = rs.getLong("director_id");
                    row.directorName = rs.getString("director_name");
                    return row;
                });

        films.values().forEach(film -> film.setDirectors(new HashSet<>()));

        for (Row row : result) {
            Director director = Director.builder()
                    .id(row.directorId)
                    .name(row.directorName)
                    .build();

            films.get(row.filmId).getDirectors().add(director);
        }
    }

    @Override
    public List<Film> findFilmsByDirectorId(Long directorId) {
        return jdbcTemplate.query(
                "SELECT f.film_id, f.name, f.description, f.MPA_ID, f.RELEASE_DATE, f.duration, " +
                        "m.mpa_id as mpa_id, m.MPA_NAME as mpa_name, " +
                        "df.director_id as director_id " +
                        "FROM FILM f " +
                        "INNER JOIN DIRECTORS_FILMS df ON f.FILM_ID = df.film_id " +
                        "LEFT OUTER JOIN mpa m ON m.mpa_id = f.mpa_id " +
                        "WHERE df.director_id = ? " +
                        "ORDER BY RELEASE_DATE", (rs, rowNum) -> makeFilm(rs), directorId);
    }

    @Override
    public List<Film> findFilmsByDirectorId(Long directorId, String param) {
        String sqlResultParametriseQuery;
        List<Film> films;
        Map<Long, Film> filmMap;
        switch (param) {
            case "year":
                sqlResultParametriseQuery = "SELECT f.film_id, f.name, f.description, f.MPA_ID, f.RELEASE_DATE, f.duration, " +
                        "m.mpa_id as mpa_id, m.MPA_NAME as mpa_name, " +
                        "df.director_id as director_id " +
                        "FROM FILM f " +
                        "INNER JOIN DIRECTORS_FILMS df ON f.FILM_ID = df.film_id " +
                        "LEFT OUTER JOIN mpa m ON m.mpa_id = f.mpa_id " +
                        "WHERE df.director_id = ? " +
                        "ORDER BY RELEASE_DATE";
                films = jdbcTemplate.query(sqlResultParametriseQuery, (rs, rowNum) -> makeFilm(rs), directorId);
                filmMap = films.stream()
                        .collect(Collectors.toMap(
                                Film::getId,
                                film -> film
                        ));
                setDirectorsFilmByFilmsIds(filmMap);
                setGenresForFilmsByIds(filmMap);
                return films;
            case "likes":
                sqlResultParametriseQuery = "SELECT f.film_id, f.name, f.description, f.MPA_ID, f.RELEASE_DATE, f.duration," +
                        "m.mpa_id as mpa_id, m.MPA_NAME as mpa_name,\n" +
                        "df.director_id as director_id, COUNT(fl.USER_ID) as likes_count\n" +
                        "FROM FILM f\n" +
                        "LEFT JOIN FILM_LIKES fl ON f.FILM_ID = fl.FILM_ID\n" +
                        "INNER JOIN DIRECTORS_FILMS df ON f.FILM_ID = df.film_id\n" +
                        "LEFT OUTER JOIN mpa m ON m.mpa_id = f.mpa_id\n" +
                        "WHERE df.director_id = ?\n" +
                        "GROUP BY f.FILM_ID\n" +
                        "ORDER BY likes_count DESC";
                films = jdbcTemplate.query(sqlResultParametriseQuery, (rs, rowNum) -> makeFilm(rs), directorId);
                filmMap = films.stream()
                        .collect(Collectors.toMap(
                                Film::getId,
                                film -> film
                        ));
                setDirectorsFilmByFilmsIds(filmMap);
                setGenresForFilmsByIds(filmMap);
                return films;
            default:
                throw new IllegalStateException("Invalid request parameter passed: " + param);
        }
    }

    private void setGenresForFilmsByIds(Map<Long, Film> films) {
        class Row {
            Long filmId;
            Integer genreId;
            String genreName;
        }

        String inSql = String.join(",", Collections.nCopies(films.keySet().size(), "?"));

        List<Row> result = jdbcTemplate.query(
                String.format("SELECT fg.FILM_ID as film_id, g.GENRE_ID as genre_id, g.GENRE_NAME as genre_name " +
                        "FROM GENRE g JOIN FILM_GENRE fg ON g.GENRE_ID = fg.GENRE_ID " +
                        "WHERE fg.FILM_ID in (%s)", inSql),
                films.keySet().toArray(),
                (rs, rowNum) -> {
                    Row row = new Row();
                    row.filmId = rs.getLong("film_Id");
                    row.genreId = rs.getInt("genre_id");
                    row.genreName = rs.getString("genre_name");
                    return row;
                });

        films.values().forEach(film -> film.setGenres(new HashSet<>()));

        for (Row row : result) {
            Long filmId = row.filmId;
            Set<Genre> genres = films.get(filmId).getGenres();
            genres.add(new Genre(row.genreId, row.genreName));
        }
    }

    @Override
    public List<Film> search(String query, String by) {
        StringJoiner stringJoiner = new StringJoiner(" OR LOWER ");
        String[] stringSplitters = by.split(",");
        String filter;
        for (String str : stringSplitters) {
            if (str.equals("director")) {
                stringJoiner.add("(dir.name) LIKE LOWER(?) ");
            } else {
                stringJoiner.add("(f.name) LIKE LOWER(?) ");
            }
        }
        filter = stringJoiner.toString();
        String sql = String.format("SELECT f.*, mp.MPA_NAME, dir.director_ID \n" +
                "FROM FILM f \n" +
                "LEFT JOIN FILM_LIKES lf ON LF.FILM_ID = f.FILM_ID \n" +
                "LEFT JOIN DIRECTORS_FILMS df ON f.FILM_ID = df.FILM_ID \n" +
                "LEFT JOIN Directors dir ON dir.director_ID = df.director_ID \n" +
                "LEFT JOIN MPA mp ON f.MPA_ID = mp.MPA_ID \n" +
                "WHERE LOWER %s \n" +
                "GROUP BY lf.FILM_ID \n" +
                "ORDER BY COUNT (lf.FILM_ID) DESC ", filter);
        Object[] params = new Object[stringSplitters.length];
        Arrays.fill(params, "%" + query + "%");

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), params);
        Map<Long, Film> mapFilms = films
                .stream()
                .collect(Collectors.toMap(film -> film.getId(), film -> film));
        setDirectorsFilmByFilmsIds(mapFilms);
        return films;
    }
}
