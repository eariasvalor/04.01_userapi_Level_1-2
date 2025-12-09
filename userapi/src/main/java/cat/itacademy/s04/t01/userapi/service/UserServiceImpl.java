package cat.itacademy.s04.t01.userapi.service;

import cat.itacademy.s04.t01.userapi.entities.User;
import cat.itacademy.s04.t01.userapi.exceptions.UserNotFoundException;
import cat.itacademy.s04.t01.userapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements  UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + user.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> searchUsersByName(String name) {
        return userRepository.searchByName(name);
    }

    @Override
    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }
}
