package cat.itacademy.s04.t01.userapi.service;

import cat.itacademy.s04.t01.userapi.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    User getUserById(UUID id);
    List<User> getAllUsers();
    List<User> searchUsersByName(String name);
    boolean isEmailAlreadyRegistered(String email);
}
