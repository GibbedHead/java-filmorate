package ru.yandex.practicum.filmorate.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserStorageTest {
    private final UserStorage userStorage;
    private final ObjectMapper objectMapper;

    @Test
    public void create() throws UserNotFoundException, JsonProcessingException {
        long id = userStorage.create(getUserFromJson());
        User u = userStorage.findById(id);
        assertEquals("dolore", u.getLogin());
        assertEquals(4, u.getId());
    }

    @Test
    public void update() throws UserNotFoundException {
        User user = userStorage.findById(1);
        user.setName("TestUpdate");
        userStorage.update(user);
        User updatedUser = userStorage.findById(1);
        assertEquals("TestUpdate", updatedUser.getName());
    }

    @Test
    public void delete() {
        userStorage.delete(1);
        List<User> users = userStorage.findAll();
        assertEquals(2, users.size());
    }

    @Test
    public void addFriend() {
        userStorage.createOrConfirmFriendship(2, 1);
        assertEquals(2, userStorage.getFriends(1).size());
    }

    @Test
    public void deleteFriend() {
        userStorage.deleteFriend(1, 2);
        assertEquals(1, userStorage.getFriends(1).size());
    }

    private User getUserFromJson() throws JsonProcessingException {
        String userJson = "{\n" +
                "  \"login\": \"dolore\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"mail@mail.ru\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}";
        return objectMapper.readValue(userJson, User.class);
    }

}