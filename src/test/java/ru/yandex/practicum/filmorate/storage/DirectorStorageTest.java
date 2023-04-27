package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DirectorStorageTest {
    private final DirectorStorage directorStorage;
    private final JdbcTemplate jdbcTemplate;
    Director director;

    @BeforeEach
    void prepare() {
        director = Director
                .builder()
                .name("Director")
                .build();
    }

    @Test
    void findById_directorNotFoundException_noAddedDirector() {
        assertThrows(DirectorNotFoundException.class, () -> directorStorage.findById(555L));
    }

    @Test
    void findById_directorNotFoundException_directorAdded() {
        directorStorage.save(director);

        Director result = directorStorage.findById(1L);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo(director.getName())
        );
    }

    @Test
    void save_successfulSave_createCorrectDirector() {
        Director result = directorStorage.save(director);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo(director.getName())
        );
    }

    @Test
    void findAll_emptyListDirectors_noAddedDirector() {
        List<Director> result = directorStorage.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_notEmptyListDirectors_directorAdded() {
        directorStorage.save(director);

        List<Director> result = directorStorage.findAll();
        assertAll(
                () -> assertThat(result).isNotEmpty(),
                () -> assertThat(result).asList().contains(director)
        );
    }

    @Test
    void update_directorNotFoundException_idUpdatedDirectorNotExist() {
        Director director = Director
                .builder()
                .id(555L)
                .name("Updated director")
                .build();

        assertThrows(DirectorNotFoundException.class, () -> directorStorage.update(director));
    }

    @Test
    void update_successfulUpdated_updatedDirectorExist() {
        directorStorage.save(director);

        Director updatedDirector = Director
                .builder()
                .id(1L)
                .name("Updated director")
                .build();

        directorStorage.update(updatedDirector);

        Director result = directorStorage.findById(1L);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getId()).isEqualTo(updatedDirector.getId()),
                () -> assertThat(result.getName()).isEqualTo(updatedDirector.getName())
        );

    }

}