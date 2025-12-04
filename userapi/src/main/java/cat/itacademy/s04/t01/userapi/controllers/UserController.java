package cat.itacademy.s04.t01.userapi.controllers;

import cat.itacademy.s04.t01.userapi.entities.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class UserController {
    private static List<User> users;

    public UserController() {
        this.users = new ArrayList<>();
    }

    @GetMapping("/users")
    public static List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }


}
