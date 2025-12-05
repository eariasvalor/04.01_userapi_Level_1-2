package cat.itacademy.s04.t01.userapi.controllers;

import cat.itacademy.s04.t01.userapi.entities.User;
import cat.itacademy.s04.t01.userapi.exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private static List<User> users = new ArrayList<>();


    public static List<User> getUsers() {
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User newUser){
        UUID id = UUID.randomUUID();
        User user = new User(id, newUser.getName(), newUser.getEmail());
        users.add(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id){
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @GetMapping
    public static List<User> getUserByName(@RequestParam(required = false) String name){
        if(name == null || name.isBlank()){
            return users;
        }

        String lower = name.toLowerCase();

        return users.stream()
                .filter(u -> u.getName().toLowerCase().contains(name))
                .toList();

    }

}
