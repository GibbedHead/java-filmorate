package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String url = "/users";

    @Test
    void emptyBodyAddShouldReturn400() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyEmailAddShouldReturn400() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getEmptyEmailUser())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyLoginAddShouldReturn400() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getEmptyLoginUser())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void spacedLoginAddShouldReturn400() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getSpacedLoginUser())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyNameAddShouldReturnLoginAsName() throws Exception {
        User user = getEmptyNameUser();
        User userWName = getEmptyNameUser();
        userWName.setName(userWName.getLogin());
        userWName.setId(1);
        mockMvc.perform(
                        post(url)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(content().string(objectMapper.writeValueAsString(userWName)));
    }

    @Test
    void in1validIdUpdateShouldReturn400() throws Exception {
        mockMvc.perform(
                post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getUser()))
        );
        User user = getUser();
        user.setId(11111);
        user.setName("Update");
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void invalidIdUpdateShouldReturn400() throws Exception {
        FilmorateApplication.main(new String[]{});
        User user = getUser();
        User invalidIdUser = getUser();
        invalidIdUser.setId(100);
        invalidIdUser.setName("Updated name");
        doPost(user);
        HttpResponse<String> response = doPut(invalidIdUser);
        assertEquals(response.statusCode(), 500);
    }

    private HttpResponse<String> doPost(User user) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080" + url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-type", "application/json").POST(
                HttpRequest.BodyPublishers.ofString(
                        objectMapper.writeValueAsString(user)
                )
        ).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> doPut(User user) throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080" + url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-type", "application/json").PUT(
                HttpRequest.BodyPublishers.ofString(
                        objectMapper.writeValueAsString(user)
                )
        ).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private User getUser() {
        return new User(
                0,
                "user@mail.com",
                "Login",
                "Name",
                LocalDate.of(1990, 1, 1)
        );
    }

    private User getEmptyEmailUser() {
        return new User(
                0,
                "",
                "Login",
                "Name",
                LocalDate.of(1990, 1, 1)
        );
    }

    private User getEmptyLoginUser() {
        return new User(
                0,
                "user@mail.com",
                "",
                "Name",
                LocalDate.of(1990, 1, 1)
        );
    }

    private User getSpacedLoginUser() {
        return new User(
                0,
                "user@mail.com",
                "Lo Gin",
                "Name",
                LocalDate.of(1990, 1, 1)
        );
    }

    private User getEmptyNameUser() {
        return new User(
                0,
                "user@mail.com",
                "Login",
                "",
                LocalDate.of(1990, 1, 1)
        );
    }

    private User getUnbornUser() {
        return new User(
                0,
                "user@mail.com",
                "Login",
                "Name",
                LocalDate.of(2100, 1, 1)
        );
    }
}