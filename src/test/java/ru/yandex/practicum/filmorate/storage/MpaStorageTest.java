package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MpaStorageTest {
    private final MpaStorage mpaStorage;

    @Test
    public void findByIdIfPresented() throws MpaNotFoundException {
        Mpa mpa = mpaStorage.findById(1);

        assertEquals(1, mpa.getId());
        assertEquals("G", mpa.getName());
    }

    @Test
    public void findByIdIfNotFoundShouldThrowMpaNotFoundException() {
        final MpaNotFoundException e = assertThrows(
                MpaNotFoundException.class,
                () -> {
                    Mpa mpa = mpaStorage.findById(-1);
                }
        );
        assertEquals("Mpa id = -1 не найден", e.getMessage());
    }

    @Test
    public void findAllShouldReturn5SizeList() {
        List<Mpa> mpaList = mpaStorage.findAll();
        assertEquals(5, mpaList.size());
    }

}