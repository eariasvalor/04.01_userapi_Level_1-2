package cat.itacademy.s04.t01.userapi.service;

import cat.itacademy.s04.t01.userapi.entities.User;
import cat.itacademy.s04.t01.userapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldThrowExceptionWhenEmailAlreadyExists() {
        User user = new User (UUID.randomUUID(), "John Doe", "john@example.com");
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user)
        );

        assertEquals("Email already registered: john@example.com", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, times(1)).existsByEmail("john@example.com");
    }
}