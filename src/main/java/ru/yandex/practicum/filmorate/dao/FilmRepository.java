package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FilmRepository {
    private final Map<Long, Film> films = new HashMap<>();
    private long id;

    public void save(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
    }

    public void update(Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Фильм для обновления c id = " + film.getId() + " не найден");
        }
    }

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    private long generateId() {
        return ++id;
    }
}
