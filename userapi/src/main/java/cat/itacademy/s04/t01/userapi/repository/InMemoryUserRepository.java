package cat.itacademy.s04.t01.userapi.repository;

import cat.itacademy.s04.t01.userapi.entities.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    List<User> users = new ArrayList<>();
    private final Map<UUID, User> storage = new HashMap<>();

    @Override
    public User save(User user) {
        UUID id = user.getId();

        if (user == null) {
            throw new IllegalArgumentException("The user cannot be null.");
        }
        if (id == null) {
            id = UUID.randomUUID();
            user.setId(id);
        }

        users.add(user);
        return user;
    }

    @Override
    public List<User> findAll() {

        return users;
    }

    @Override
    public Optional<User> findById(UUID id) {
        if(id == null){
            throw new IllegalArgumentException("The id cannot be null");
        }
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> searchByName(String name) {
        if(name == null || name.isBlank()){
            return users;
        }

        String lower = name.toLowerCase();

        return users.stream()
                .filter(u -> u.getName().toLowerCase().contains(lower))
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("The email is invalid.");
        }

        return users.stream()
                .anyMatch(u -> u.getEmail().equals(email));

    }
}
