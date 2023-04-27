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
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

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
    public void create() throws ReviewNotFoundException, JsonProcessingException {
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

    @Test
    public void addLike() {
        List<Review> beforeLike = reviewStorage.findAllByFilmId(1, 10);
        assertEquals(1, beforeLike.get(0).getReviewId());
        reviewStorage.addLike(2, 3);
        List<Review> afterLike = reviewStorage.findAllByFilmId(1, 10);
        assertEquals(2, afterLike.get(0).getReviewId());
    }

    @Test
    public void addDislike() {
        List<Review> beforeDislike = reviewStorage.findAllByFilmId(1, 10);
        assertEquals(1, beforeDislike.get(0).getReviewId());
        reviewStorage.addDislike(1, 3);
        List<Review> afterDislike = reviewStorage.findAllByFilmId(1, 10);
        assertEquals(2, afterDislike.get(0).getReviewId());
    }

    @Test
    public void deleteLike() {
        reviewStorage.addLike(2, 3);
        List<Review> afterLike = reviewStorage.findAllByFilmId(1, 10);
        assertEquals(2, afterLike.get(0).getReviewId());
        reviewStorage.deleteLike(2, 3);
        List<Review> afterDeleteLike = reviewStorage.findAllByFilmId(1, 10);
        assertEquals(1, afterDeleteLike.get(0).getReviewId());
    }

    @Test
    public void deleteDislike() {
        reviewStorage.addDislike(1, 3);
        List<Review> afterDislike = reviewStorage.findAllByFilmId(1, 10);
        assertEquals(2, afterDislike.get(0).getReviewId());
        reviewStorage.deleteDislike(1, 3);
        List<Review> afterDeleteDislike = reviewStorage.findAllByFilmId(1, 10);
        assertEquals(1, afterDeleteDislike.get(0).getReviewId());
    }
    
}
