package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserActivityEvent {
    private long timestamp;
    private long userId;
    private EventType eventType;
    private Operation operation;
    private long eventId;
    private long entityId;

    public UserActivityEvent() {

    }

    public enum EventType {
        LIKE, REVIEW, FRIEND
    }

    public enum Operation {
        REMOVE, ADD, UPDATE
    }
}

