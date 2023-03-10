package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FilmController.class)
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilmController filmController;

    private final String url = "/films";

    @Test
    void emptyBodyAddShouldReturn400() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyNameAddShouldReturn400() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getEmptyNameFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void longDescAddShouldReturn400() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getLongDescFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void dateBefore25Dec1895AddShouldReturn400() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getReleseBeforeFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void negativeDurationAddShouldReturn400() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getNegativeFurationFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyBodyUpdateShouldReturn400() throws Exception {
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyNameUpdateShouldReturn400() throws Exception {
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getEmptyNameFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void longDescUpdateShouldReturn400() throws Exception {
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getLongDescFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void dateBefore25Dec1895UpdateShouldReturn400() throws Exception {
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getReleseBeforeFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void negativeDurationUpdateShouldReturn400() throws Exception {
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getNegativeFurationFilm())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidIdUpdateShouldReturn400() throws Exception {
        FilmorateApplication.main(new String[]{});
        Film film = getFilm();
        Film invalidIdfilm = getFilm();
        invalidIdfilm.setId(100);
        invalidIdfilm.setName("Updated name");
        doPost(film);
        HttpResponse<String> response = doPut(invalidIdfilm);
        assertEquals(response.statusCode(), 500);
    }

    private HttpResponse<String> doPost(Film film) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080" + url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-type", "application/json").POST(
                HttpRequest.BodyPublishers.ofString(
                        objectMapper.writeValueAsString(film)
                )
        ).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> doPut(Film film) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080" + url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-type", "application/json").PUT(
                HttpRequest.BodyPublishers.ofString(
                        objectMapper.writeValueAsString(film)
                )
        ).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private Film getFilm() {
        return new Film(
                0,
                "Name",
                "Desc",
                LocalDate.of(1990, 1, 1),
                90
        );
    }

    private Film getEmptyNameFilm() {
        return new Film(
                0,
                "",
                "Desc",
                LocalDate.of(1990, 1, 1),
                90
        );
    }

    private Film getLongDescFilm() {
        return new Film(
                0,
                "Name",
                "D".repeat(300),
                LocalDate.of(1990, 1, 1),
                90
        );
    }

    private Film getReleseBeforeFilm() {
        return new Film(
                0,
                "Name",
                "Desc",
                LocalDate.of(1790, 1, 1),
                90
        );
    }

    private Film getNegativeFurationFilm() {
        return new Film(
                0,
                "Name",
                "Desc",
                LocalDate.of(1990, 1, 1),
                -90
        );
    }
}