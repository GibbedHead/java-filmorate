package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.UserActivityEvent;

import java.util.List;

public interface UserActivityStorageInterface {
    List<UserActivityEvent> getUserFeed(long userId);

    boolean hasId(long id);

    long save(Long userId, UserActivityEvent.EventType eventType, UserActivityEvent.Operation operation, Long entityId);

    List<UserActivityEvent> getAll(long userId);
}

