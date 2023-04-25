package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationsService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public List<Film> getFilmRecommendations(long userId) throws UserNotFoundException {
        User checkUser = userStorage.findById(userId);
        return null;
    }

}
