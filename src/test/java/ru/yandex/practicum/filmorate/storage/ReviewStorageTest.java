package ru.yandex.practicum.filmorate.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ReviewStorageTest {

    private final ReviewStorage reviewStorage;
    private final ObjectMapper objectMapper;

    private Review getReviewFromJson() throws JsonProcessingException {
        String reviewJson = "{\n" +
                "  \"content\": \"some content\",\n" +
                "  \"isPositive\": true,\n" +
                "  \"userId\": 1,\n" +
                "  \"filmId\": 1\n" +
                "}";
        return objectMapper.readValue(reviewJson, Review.class);
    }

    @Test
    public void  create() throws ReviewNotFoundException, JsonProcessingException {
        long id = reviewStorage.create(getReviewFromJson());
        Review review = reviewStorage.findById(id);
        assertEquals("some content", review.getContent());
        assertTrue(review.getIsPositive());
        assertEquals(1, review.getUserId());
        assertEquals(1, review.getFilmId());
    }

    @Test
    public void update() throws ReviewNotFoundException {
        Review review = reviewStorage.findById(1);
        review.setContent("updated content");
        review.setIsPositive(false);
        Review updatedReview = reviewStorage.findById(1);
        assertEquals("updated content", review.getContent());
        assertFalse(review.getIsPositive());
    }

    @Test
    public void delete() {
        reviewStorage.delete(1);
        assertEquals(1, reviewStorage.findAllByFilmId(1, 10).size());
    }

 /*   Review findById(long id) throws ReviewNotFoundException;

    List<Review> findAllByFilmId(long filmId, int count);

    void addLike(long reviewId, long userId);

    void addDislike(long reviewId, long userId);

    void deleteLike(long reviewId, long userId);

    void deleteDislike(long reviewId, long userId);
     */
}
