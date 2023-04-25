package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    long id;
    String content;
    boolean isPositive;
    long userId;
    long filmId;
    int useful;
}

