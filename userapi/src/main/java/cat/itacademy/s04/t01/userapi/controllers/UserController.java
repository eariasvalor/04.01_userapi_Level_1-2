package cat.itacademy.s04.t01.userapi.controllers;

import cat.itacademy.s04.t01.userapi.entities.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {
    private static List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public static List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User newUser){
        UUID id = UUID.randomUUID();

        User user = new User(id, newUser.getName(), newUser.getEmail());

        users.add(user);

        return user;
    }






}
