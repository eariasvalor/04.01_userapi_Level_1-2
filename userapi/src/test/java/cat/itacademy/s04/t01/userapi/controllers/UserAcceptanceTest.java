package cat.itacademy.s04.t01.userapi.controllers;

import cat.itacademy.s04.t01.userapi.entities.User;
import cat.itacademy.s04.t01.userapi.repository.InMemoryUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;
import java.util.UUID;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserAcceptanceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.findAll().clear();
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Ada Lovelace"))
                .andExpect(jsonPath("$.email").value("ada@example.com"));
    }


    @Test
    void getUserById_returnsCorrectUser() throws Exception {
        User user = new User(null, "Ada Lovelace", "ada@example.com");

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        User createdUser = objectMapper.readValue(responseBody, User.class);
        UUID id = createdUser.getId();

        System.out.println("Created user ID: " + id);
        System.out.println("Users in repository: " + userRepository.findAll().size());
        System.out.println("Looking for user: " + userRepository.findById(id));

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Ada Lovelace"))
                .andExpect(jsonPath("$.email").value("ada@example.com"));

    }

    @Test
    void getUserById_returnsNotFoundIfMissing() throws Exception {
        UUID randomId = UUID.randomUUID();
        mockMvc.perform(get("/users/{id}", randomId))
                .andExpect(status().isNotFound());
            }

    @Test
    void getUsers_withNameParam_returnsFilteredUsers() throws Exception {
        User user1 = new User(null, "Joan", "joan@gmail.com");
        User user2 = new User(null, "María", "mawi@gmail.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users").param("name", "jo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Joan"));

    }
}