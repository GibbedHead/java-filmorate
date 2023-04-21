package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    @Override
    public void create(Film film) {

    }

    @Override
    public void update(Film film) throws FilmNotFoundException {

    }

    @Override
    public void delete(Film film) {

    }

    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public Film findById(long id) throws FilmNotFoundException {
        return null;
    }
}
