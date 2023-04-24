package ru.yandex.practicum.filmorate.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmStorageTest {

    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final ObjectMapper objectMapper;

    @Test
    void create() throws JsonProcessingException, FilmNotFoundException, MpaNotFoundException, GenreNotFoundException {
        long id = filmStorage.create(getFilmFromJson());
        Film film = filmStorage.findById(id);
        assertEquals("New film", film.getName());
        assertEquals(
                mpaStorage.findById(3),
                film.getMpa()
        );
        assertEquals(
                Set.of(genreStorage.findById(1)),
                film.getGenres()
        );
    }

    @Test
    void update() throws FilmNotFoundException, GenreNotFoundException, MpaNotFoundException {
        Film film = filmStorage.findById(1);
        film.setGenres(Set.of(
                genreStorage.findById(1),
                genreStorage.findById(6)
        ));
        film.setMpa(mpaStorage.findById(4));
        film.setDescription("TestUpdate");
        filmStorage.update(film);
        Film updatedFilm = filmStorage.findById(1);
        assertEquals("TestUpdate", updatedFilm.getDescription());
        assertEquals(
                Set.of(
                        genreStorage.findById(1),
                        genreStorage.findById(6)
                ),
                updatedFilm.getGenres()
        );
        assertEquals(
                mpaStorage.findById(4),
                film.getMpa()
        );
    }

    @Test
    void delete() {
        filmStorage.delete(1);
        assertEquals(1, filmStorage.findAll().size());
    }

    @Test
    void getPopular() {
        assertEquals(
                1,
                filmStorage.getPopular(10).get(0).getId());
    }

    @Test
    void addLike() {
        filmStorage.addLike(2, 1);
        filmStorage.addLike(2, 2);
        assertEquals(
                2,
                filmStorage.getPopular(10).get(0).getId());
    }

    @Test
    void deleteLike() {
        filmStorage.deleteLike(1, 1);
        filmStorage.deleteLike(1, 2);
        assertEquals(
                2,
                filmStorage.getPopular(10).get(0).getId());
    }

    private Film getFilmFromJson() throws JsonProcessingException {
        String filmJson = "{\n" +
                "  \"name\": \"New film\",\n" +
                "  \"releaseDate\": \"1999-04-30\",\n" +
                "  \"description\": \"New film about friends\",\n" +
                "  \"duration\": 120,\n" +
                "  \"rate\": 4,\n" +
                "  \"mpa\": { \"id\": 3},\n" +
                "  \"genres\": [{ \"id\": 1}]\n" +
                "}";
        return objectMapper.readValue(filmJson, Film.class);
    }
}