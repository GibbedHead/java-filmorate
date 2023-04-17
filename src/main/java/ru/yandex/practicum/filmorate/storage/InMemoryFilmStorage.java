package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id;

    public void create(Film film) {
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

    @Override
    public void delete(Film film) {

    }

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findById(long id) throws FilmNotFoundException {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new FilmNotFoundException(String.format("Фильм № %d не найден", id));
    }

    private long generateId() {
        return ++id;
    }
}
