package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserActivityEvent;
import ru.yandex.practicum.filmorate.storage.UserActivityStorageInterface;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserActivityService {
    private final UserActivityStorageInterface userActivityStorage;
    private final UserStorage userDbStorage;

    public List<UserActivityEvent> getUserFeed(long userId) {
        if (!hasEventId(userId)) {
            log.warn("Не удалось обновить пользователя: Failed to update user due to bad ID");
            throw new UserNotFoundException("No such ID");
        }
        User user = userDbStorage.findById(userId);
        return userActivityStorage.getUserFeed(userId);
    }

    public boolean hasEventId(long id) {
        return userActivityStorage.hasId(id);
    }

}

