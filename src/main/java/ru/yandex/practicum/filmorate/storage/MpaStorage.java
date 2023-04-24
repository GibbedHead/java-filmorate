package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> findAll();

    Mpa findById(long id) throws MpaNotFoundException;
}
