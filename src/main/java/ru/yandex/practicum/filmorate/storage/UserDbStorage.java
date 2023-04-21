package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component("UserDbStorage")
@AllArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public long create(User user) {
        String sql = "INSERT INTO PUBLIC.USERS (EMAIL,LOGIN,NAME,BIRTHDAY) " +
                "VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void update(User user) {
        String updateSQL = "" +
                "UPDATE " +
                "  USERS " +
                "SET " +
                "  EMAIL = ?, " +
                "  LOGIN = ?, " +
                "  NAME = ?, " +
                "  BIRTHDAY = ? " +
                "WHERE " +
                "  USER_ID = ?";
        jdbcTemplate.update(
                updateSQL,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
    }

    @Override
    public void delete(long id) {
        String sql = "" +
                "DELETE " +
                "FROM " +
                "  USERS " +
                "WHERE " +
                "  USER_ID = ? ";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<User> findAll() {
        String sql = "" +
                "SELECT" +
                "  * " +
                "FROM" +
                "  USERS";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User findById(long id) throws UserNotFoundException {
        String sql = "" +
                "SELECT" +
                "  * " +
                "FROM " +
                "  USERS " +
                "WHERE " +
                "  USER_ID = ? " +
                "LIMIT " +
                "  1";
        List<User> userList = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
        if (userList.isEmpty()) {
            log.info("Пользователь id = {} не найден", id);
            throw new UserNotFoundException(String.format("Пользователь id = %d не найден", id));
        } else {
            return userList.get(0);
        }
    }

    private User makeUser(ResultSet rs) throws SQLException {
        long id = rs.getLong("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("NAME");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();

        return new User(
                id,
                login,
                name,
                email,
                birthday
        );
    }
}
