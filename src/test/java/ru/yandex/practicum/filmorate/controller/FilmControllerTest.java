package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FilmController.class)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmService filmService;
    @MockBean
    private FilmStorage filmStorage;

    private final String url = "/films";

    @Test
    void validAddShouldReturnOkAndJsonOfFilm() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getFilm())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.description").value("Desc"))
                .andExpect(jsonPath("$.releaseDate").value("1990-01-01"))
                .andExpect(jsonPath("$.duration").value("90"));
    }

    @Test
    void emptyBodyAddShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyNameAddShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getEmptyNameFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void longDescAddShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getLongDescFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void dateBefore25Dec1895AddShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getReleseBeforeFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void negativeDurationAddShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getNegativeFurationFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void validUpdateShouldReturnOkAndUpdatedJsonOfUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        post(url)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(getFilm()))

                )
                .andReturn();
        Film addedFilm = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Film.class);
        Film updatedFilm = getFilm();
        updatedFilm.setId(addedFilm.getId());
        updatedFilm.setName("Updated");

        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedFilm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void emptyBodyUpdateShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyNameUpdateShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getEmptyNameFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void longDescUpdateShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getLongDescFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void dateBefore25Dec1895UpdateShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getReleseBeforeFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void negativeDurationUpdateShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getNegativeFurationFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidIdUpdateShouldReturnBadRequest() throws Exception {
        mockMvc.perform(
                post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getFilm()))
        );
        Film invalidIdFilm = getFilm();
        invalidIdFilm.setId(11111);
        invalidIdFilm.setName("Update");
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidIdFilm)))
                .andExpect(status().isNotFound());
    }

    private Film getFilm() {
        return new Film(
                0,
                "Name",
                "Desc",
                LocalDate.of(1990, 1, 1),
                90,
                null
        );
    }

    private Film getEmptyNameFilm() {
        return new Film(
                0,
                "",
                "Desc",
                LocalDate.of(1990, 1, 1),
                90,
                null
        );
    }

    private Film getLongDescFilm() {
        return new Film(
                0,
                "Name",
                "D".repeat(300),
                LocalDate.of(1990, 1, 1),
                90,
                null
        );
    }

    private Film getReleseBeforeFilm() {
        return new Film(
                0,
                "Name",
                "Desc",
                LocalDate.of(1790, 1, 1),
                90,
                null
        );
    }

    private Film getNegativeFurationFilm() {
        return new Film(
                0,
                "Name",
                "Desc",
                LocalDate.of(1990, 1, 1),
                -90,
                null
        );
    }
}