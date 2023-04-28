package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Review {
    long reviewId;
    @NotBlank
    String content;
    @NotNull
    @JsonProperty(value = "isPositive")
    Boolean isPositive;
    @NotNull
    Long userId;
    @NotNull
    Long filmId;
    int useful;

}
