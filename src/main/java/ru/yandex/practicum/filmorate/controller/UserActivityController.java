package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.UserActivityEvent;
import ru.yandex.practicum.filmorate.service.UserActivityService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserActivityController {
    private final UserActivityService userActivityService;

    @GetMapping("/{id}/feed")
    public ResponseEntity<List<UserActivityEvent>> getUserFeed(@PathVariable("id") long userId) {
            List<UserActivityEvent> userFeed = userActivityService.getUserFeed(userId);
            log.info("Запрос ленты для пользователя: {}", userId);

            return new ResponseEntity<>(userFeed, HttpStatus.OK);
    }
}

