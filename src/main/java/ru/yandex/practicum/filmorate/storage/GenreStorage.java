package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    List<Genre> findAll();

    Genre findById(long id) throws GenreNotFoundException;

    Set<Genre> findByFilmId(long id);
}
