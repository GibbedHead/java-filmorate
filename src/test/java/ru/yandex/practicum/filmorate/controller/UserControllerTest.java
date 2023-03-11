package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void validAddShouldReturnOkAndJsonOfUser() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getUser())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@mail.com"))
                .andExpect(jsonPath("$.login").value("Login"))
                .andExpect(jsonPath("$.birthday").value("1990-01-01"))
                .andExpect(jsonPath("$.name").value("Name"));
    }

    @Test
    void emptyBodyAddShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyEmailAddShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getEmptyEmailUser())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyLoginAddShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getEmptyLoginUser())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void spacedLoginAddShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getSpacedLoginUser())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyNameAddShouldReturnLoginAsName() throws Exception {
        mockMvc.perform(
                        post(url)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(getEmptyNameUser()))
                )
                .andExpect(jsonPath("$.name").value("Login"));
    }

    @Test
    void birthdayInFutureAddShouldReturnBadRequest() throws Exception {
        mockMvc.perform(
                        post(url)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(getUnbornUser()))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void validUpdateShouldReturnOkAndUpdatedJsonOfUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        post(url)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(getUser()))

                )
                .andReturn();
        User addedUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        User updatedUser = getUser();
        updatedUser.setId(addedUser.getId());
        updatedUser.setName("Updated");

        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void invalidIdUpdateShouldReturnBadRequest() throws Exception {
        mockMvc.perform(
                post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(getUser()))
        );
        User invalidIdUser = getUser();
        invalidIdUser.setId(11111);
        invalidIdUser.setName("Update");
        mockMvc.perform(put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidIdUser)))
                .andExpect(status().isBadRequest());
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