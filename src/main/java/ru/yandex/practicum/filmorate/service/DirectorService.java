package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;


@RequiredArgsConstructor
@Service
public class DirectorService {
    private final DirectorStorage directorStorage;

    public Director save(Director director) {
        return directorStorage.save(director);
    }

    public Director getDirectorById(Long id){
        return directorStorage.findById(id);
    }

    public Director update(Director director) {
        return directorStorage.update(director);
    }

    public List<Director> getAll() {
        return directorStorage.findAll();
    }

    public void deleteById(Long id) {
        directorStorage.deleteById(id);
    }
}
