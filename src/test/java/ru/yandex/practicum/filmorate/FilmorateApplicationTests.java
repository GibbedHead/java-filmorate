package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {

	private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void filmRealiseDateValidatorShouldThrowExceptionOnDatesBefore28December1895() {
		Film film = new Film(
				0,
				"Test name",
				"Test desc",
				LocalDate.of(1800, 1, 1),
				90,
				null,
				null
		);
		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		assertEquals(violations.size(), 1);
	}

	@Test
	void filmRealiseDateValidatorShouldNotThrowExceptionOnDatesAfter28December1895() {
		Film film = new Film(
				0,
				"Test name",
				"Test desc",
				LocalDate.of(1990, 1, 1),
				90,
				null,
				null
		);
		Set<ConstraintViolation<Film>> violations = validator.validate(film);
		assertEquals(violations.size(), 0);
	}

}
