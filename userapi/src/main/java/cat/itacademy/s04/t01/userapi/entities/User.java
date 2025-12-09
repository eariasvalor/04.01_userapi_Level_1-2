package cat.itacademy.s04.t01.userapi.entities;

import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;

    public User(UUID id, String name, String email) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("The name cannot be empty.");
        }
        if (email == null || email.isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("The email is invalid.");
        }
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
