package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public void addLike(long filmId, long userId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public List<Film> getTopLikedFilms(long count) {
        return films.values().stream()
                .sorted(
                        Comparator.comparingInt(
                                (Film f) -> f.getLikes().size()
                        ).reversed()
                )
                .limit(count)
                .collect(Collectors.toList());
    }

    private long generateId() {
        return ++id;
    }
}
