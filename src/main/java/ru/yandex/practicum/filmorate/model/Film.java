package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.IsAfter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Size(max=200)
    private String description;
    @Past
    @IsAfter(fromDate = "1895-12-28")
    @DateTimeFormat( pattern="yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Set<Long> likes;
}
