package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.UserActivityEvent;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component("UserActivityDbStorage")
@AllArgsConstructor
@Slf4j
public class UserActivityDbStorage implements UserActivityStorageInterface {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<UserActivityEvent> activityRowMapper = (resultSet, rowNum) -> {
        UserActivityEvent userActivityEvent = new UserActivityEvent();
        userActivityEvent.setTimestamp(resultSet.getTimestamp("TIMESTAMP").getTime());
        userActivityEvent.setUserId(resultSet.getLong("USER_ID"));
        userActivityEvent.setEventType(UserActivityEvent.EventType.valueOf(resultSet.getString("EVENT_TYPE")));
        userActivityEvent.setOperation(UserActivityEvent.Operation.valueOf(resultSet.getString("OPERATION")));
        userActivityEvent.setEventId(resultSet.getLong("ACTIVITY_ID"));
        userActivityEvent.setEntityId(resultSet.getLong("ENTITY_ID"));
        return userActivityEvent;
    };

    @Override
    public List<UserActivityEvent> getUserFeed(long userId) {
        String sql = "SELECT * FROM PUBLIC.USER_ACTIVITIES WHERE USER_ID = ? ORDER BY TIMESTAMP ASC";
        return jdbcTemplate.query(sql, activityRowMapper, userId);
    }

    @Override
    public boolean hasId(long id) {
        String sql = "SELECT COUNT(*) FROM PUBLIC.USER_ACTIVITIES WHERE ACTIVITY_ID = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    @Override
    public long save(Long userId, UserActivityEvent.EventType eventType, UserActivityEvent.Operation operation, Long entityId) {
        String sql = "INSERT INTO PUBLIC.USER_ACTIVITIES (USER_ID, EVENT_TYPE, TIMESTAMP, OPERATION, ENTITY_ID) VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"ACTIVITY_ID"});
            stmt.setLong(1, userId);
            stmt.setString(2, String.valueOf(eventType));
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(4, String.valueOf(operation));
            stmt.setLong(5, entityId);
            return stmt;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<UserActivityEvent> getAll(long userId) {
        String sql = "SELECT * FROM PUBLIC.USER_ACTIVITIES";
        List<UserActivityEvent> events = jdbcTemplate.query(sql, activityRowMapper);
        return new ArrayList<>(events);
    }
}