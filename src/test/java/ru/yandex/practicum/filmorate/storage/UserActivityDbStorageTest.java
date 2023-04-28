package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.UserActivityEvent;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserActivityDbStorageTest {

    @Autowired
    private UserActivityStorageInterface userActivityStorage;

    @Test
    void getUserFeed() {
        List<UserActivityEvent> userFeed = userActivityStorage.getUserFeed(1);
        assertNotNull(userFeed);
        assertFalse(userFeed.isEmpty());
        assertEquals(1, userFeed.get(0).getUserId());
        assertEquals(4, userFeed.size());
    }

    @Test
    void save() {
        long id = userActivityStorage.save(1L, UserActivityEvent.EventType.REVIEW, UserActivityEvent.Operation.ADD, 1L);
        assertTrue(userActivityStorage.hasId(id));
    }

    @Test
    void getAll() {
        List<UserActivityEvent> allEvents = userActivityStorage.getAll(1);
        assertNotNull(allEvents);
        assertFalse(allEvents.isEmpty());
    }
}

