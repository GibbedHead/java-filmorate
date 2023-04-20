package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id;

    public void create(Film film) {
        film.setId(generateId());
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        films.put(film.getId(), film);
    }

    public void update(Film film) throws FilmNotFoundException {
        if (films.containsKey(film.getId())) {
            if (film.getLikes() == null) {
                film.setLikes(new HashSet<>());
            }
            films.put(film.getId(), film);
        } else {
            throw new FilmNotFoundException(String.format("Фильм для обновления c id = %d не найден", film.getId()));
        }
    }

    @Override
    public void delete(Film film) {
        films.remove(film.getId());
    }

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findById(long id) throws FilmNotFoundException {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new FilmNotFoundException(String.format("Фильм c id = %d не найден", id));
    }

    private long generateId() {
        return ++id;
    }
}
