package cat.itacademy.s04.t01.userapi.controllers;

import cat.itacademy.s04.t01.userapi.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController.getUsers().clear();
    }


    @Test
    void getUsers_returnsEmptyListInitially() throws Exception {

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createUser_returnsUserWithId() throws Exception {
        User user = new User(null, "Ada Lovelace", "ada@example.com");

        mockMvc.perform(post("/users")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Ada Lovelace"))
                .andExpect(jsonPath("$.email").value("ada@example.com"));
    }


    @Test
    void getUserById_returnsCorrectUser() throws Exception {
        User user = new User(null, "Ada Lovelace", "ada@example.com");

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        User createdUser = objectMapper.readValue(responseBody, User.class);
        UUID id = createdUser.getId();

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Ada Lovelace"))
                .andExpect(jsonPath("$.email").value("ada@example.com"));

    }

    @Test
    void getUserById_returnsNotFoundIfMissing() {
        // Simula GET /users/{id} amb un id aleatori
        // Espera 404
    }

    @Test
    void getUsers_withNameParam_returnsFilteredUsers() {
        // Afegeix dos usuaris amb POST
        // Fa GET /users?name=jo i comprova que només torni els que contenen "jo"
    }
}