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
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
    private final DirectorStorage directorStorage;

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
        assertEquals(4, filmStorage.findAll().size());
    }

    @Test
    void getPopular() {
        assertEquals(
                1,
                filmStorage.getPopular(10, null, null).get(0).getId());
    }

    @Test
    void addLike() {
        filmStorage.addLike(1, 3);
        assertEquals(
                1,
                filmStorage.getPopular(10, null, null).get(0).getId());
    }

    @Test
    void deleteLike() {
        filmStorage.deleteLike(1, 1);
        filmStorage.deleteLike(1, 2);
        assertEquals(
                5,
                filmStorage.getPopular(10, null, null).get(0).getId());
    }

    @Test
    void commonFilms() {
        assertEquals(2, filmStorage.getCommon(1, 2).size());
        filmStorage.deleteLike(1, 1);
        assertEquals(1, filmStorage.getCommon(1, 2).size());
        filmStorage.addLike(4, 2);
        filmStorage.addLike(3, 1);
        assertEquals(3, filmStorage.getCommon(1, 2).size());
    }

    @Test
    void findFilmsByDirectorId_emptyList_directorNoAdded() {
        List<Film> result = filmStorage.findFilmsByDirectorId(1L);
        assertThat(result).isEmpty();
    }

    @Test
    void findFilmsByDirectorId_notEmptyList_directorAddedIntoFilmAndFilterParamIsEmpty() throws FilmNotFoundException {
        Director director = Director.builder().name("Director").build();
        director = directorStorage.save(director);
        Film film = filmStorage.findById(1L);
        film.setDirectors(Set.of(director));
        filmStorage.update(film);

        List<Film> result = filmStorage.findFilmsByDirectorId(director.getId());

        assertAll(
                () -> assertThat(result).isNotEmpty(),
                () -> assertThat(result.get(0).getName()).isEqualTo(film.getName())
        );
    }

    @Test
    void findFilmsByDirectorId_notEmptyList_directorAddedIntoFilmIdAndFilterByYear() throws FilmNotFoundException {
        Director director = Director.builder().name("Director").build();
        director = directorStorage.save(director);

        Film film1 = filmStorage.findById(1L);
        Film film2 = filmStorage.findById(2L);

        film1.setDirectors(Set.of(director));
        film2.setDirectors(Set.of(director));

        filmStorage.update(film1);
        filmStorage.update(film2);

        List<Film> result = filmStorage.findFilmsByDirectorId(director.getId(), "year");

        assertAll(
                () -> assertThat(result).isNotEmpty(),
                () -> assertThat(result.size()).isGreaterThan(1),
                () -> assertThat(result.get(0).getName()).isEqualTo(film2.getName()),
                () -> assertThat(result.get(1).getName()).isEqualTo(film1.getName())
        );
    }

    @Test
    void findFilmsByDirectorId_notEmptyList_directorAddedIntoFilmIdAndFilterByLikes() throws FilmNotFoundException {
        Director director = Director.builder().name("Director").build();
        director = directorStorage.save(director);

        Film film1 = filmStorage.findById(1L);
        Film film2 = filmStorage.findById(2L);

        film1.setDirectors(Set.of(director));
        film2.setDirectors(Set.of(director));

        filmStorage.update(film1);
        filmStorage.update(film2);

        List<Film> result = filmStorage.findFilmsByDirectorId(director.getId(), "likes");

        assertAll(
                () -> assertThat(result).isNotEmpty(),
                () -> assertThat(result.size()).isGreaterThan(1),
                () -> assertThat(result.get(0).getName()).isEqualTo(film1.getName()),
                () -> assertThat(result.get(1).getName()).isEqualTo(film2.getName())
        );
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