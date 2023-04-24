package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GenreStorageTest {
    private final GenreStorage genreStorage;

    @Test
    public void findByIdIfPresented() throws GenreNotFoundException {
        Genre genre = genreStorage.findById(1);

        assertEquals(1, genre.getId());
        assertEquals("Комедия", genre.getName());
    }

    @Test
    public void findByIdIfNotFoundShouldThrowGenreNotFoundException() {
        final GenreNotFoundException e = assertThrows(
                GenreNotFoundException.class,
                () -> {
                    Genre genre = genreStorage.findById(-1);
                }
        );
        assertEquals("Жанр id = -1 не найден", e.getMessage());
    }

    @Test
    public void findAllShouldReturn6SizeList() {
        List<Genre> genreList = genreStorage.findAll();
        assertEquals(6, genreList.size());
    }

}