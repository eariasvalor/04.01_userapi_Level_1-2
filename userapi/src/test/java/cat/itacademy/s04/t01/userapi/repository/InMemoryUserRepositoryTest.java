package cat.itacademy.s04.t01.userapi.repository;

import cat.itacademy.s04.t01.userapi.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();

        user1 = new User(UUID.randomUUID(), "John Doe", "john@example.com");
        user2 = new User(UUID.randomUUID(), "Jane Smith", "jane@example.com");
        user3 = new User(UUID.randomUUID(), "John Williams", "jwilliams@example.com");
    }

    @Test
    @DisplayName("save() - Should save user successfully")
    void testSave_Success() {
        User savedUser = repository.save(user1);

        assertNotNull(savedUser);
        assertEquals(user1, savedUser);
        assertEquals(1, repository.findAll().size());
        assertTrue(repository.findAll().contains(user1));
    }

    @Test
    @DisplayName("save() - Should save multiple users")
    void testSave_MultipleUsers() {
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);

        assertEquals(3, repository.findAll().size());
    }

    @Test
    @DisplayName("save() - Should throw exception when user is null")
    void testSave_NullUser() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(null)
        );

        assertEquals("The user cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("save() - Should return the same user object")
    void testSave_ReturnsSameObject() {
        User result = repository.save(user1);

        assertSame(user1, result);
    }


    @Test
    @DisplayName("findAll() - Should return empty list when no users")
    void testFindAll_EmptyList() {
        List<User> users = repository.findAll();

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("findAll() - Should return all users")
    void testFindAll_WithUsers() {
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);

        List<User> users = repository.findAll();

        assertEquals(3, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        assertTrue(users.contains(user3));
    }



    @Test
    @DisplayName("findById() - Should return user when exists")
    void testFindById_UserExists() {
        repository.save(user1);
        UUID id = user1.getId();

        Optional<User> result = repository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(user1, result.get());
    }

    @Test
    @DisplayName("findById() - Should return empty when user not exists")
    void testFindById_UserNotExists() {
        repository.save(user1);
        UUID nonExistentId = UUID.randomUUID();

        Optional<User> result = repository.findById(nonExistentId);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findById() - Should return empty when repository is empty")
    void testFindById_EmptyRepository() {
        Optional<User> result = repository.findById(UUID.randomUUID());

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findById() - Should find correct user among multiple users")
    void testFindById_MultipleUsers() {
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);

        Optional<User> result = repository.findById(user2.getId());

        assertTrue(result.isPresent());
        assertEquals(user2, result.get());
    }



    @Test
    @DisplayName("searchByName() - Should return all users when name is null")
    void testSearchByName_NullName() {
        repository.save(user1);
        repository.save(user2);

        List<User> results = repository.searchByName(null);

        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("searchByName() - Should return all users when name is blank")
    void testSearchByName_BlankName() {
        repository.save(user1);
        repository.save(user2);

        List<User> results = repository.searchByName("   ");


        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("searchByName() - Should return all users when name is empty")
    void testSearchByName_EmptyName() {
        repository.save(user1);
        repository.save(user2);

        List<User> results = repository.searchByName("");

        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("searchByName() - Should find users by exact name")
    void testSearchByName_ExactMatch() {
        repository.save(user1);
        repository.save(user2);

        List<User> results = repository.searchByName("John Doe");

        assertEquals(1, results.size());
        assertEquals(user1, results.get(0));
    }

    @Test
    @DisplayName("searchByName() - Should find users by partial name")
    void testSearchByName_PartialMatch() {
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);

        List<User> results = repository.searchByName("John");

        assertEquals(2, results.size());
        assertTrue(results.contains(user1));
        assertTrue(results.contains(user3));
    }

    @Test
    @DisplayName("searchByName() - Should be case insensitive")
    void testSearchByName_CaseInsensitive() {
        repository.save(user1);

        List<User> resultsLower = repository.searchByName("john");
        List<User> resultsUpper = repository.searchByName("JOHN");
        List<User> resultsMixed = repository.searchByName("JoHn");

        assertEquals(1, resultsLower.size());
        assertEquals(1, resultsUpper.size());
        assertEquals(1, resultsMixed.size());
        assertEquals(user1, resultsLower.get(0));
        assertEquals(user1, resultsUpper.get(0));
        assertEquals(user1, resultsMixed.get(0));
    }

    @Test
    @DisplayName("searchByName() - Should return empty list when no matches")
    void testSearchByName_NoMatches() {
        repository.save(user1);
        repository.save(user2);

        List<User> results = repository.searchByName("Bob");

        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("searchByName() - Should return empty list when repository is empty")
    void testSearchByName_EmptyRepository() {
        List<User> results = repository.searchByName("John");

        assertTrue(results.isEmpty());
    }


    @Test
    @DisplayName("existsByEmail() - Should return true when email exists")
    void testExistsByEmail_EmailExists() {
        repository.save(user1);

        boolean exists = repository.existsByEmail("john@example.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("existsByEmail() - Should return false when email does not exist")
    void testExistsByEmail_EmailNotExists() {
        repository.save(user1);

        boolean exists = repository.existsByEmail("notfound@example.com");

        assertFalse(exists);
    }

    @Test
    @DisplayName("existsByEmail() - Should return false when repository is empty")
    void testExistsByEmail_EmptyRepository() {
        boolean exists = repository.existsByEmail("any@example.com");

        assertFalse(exists);
    }

    @Test
    @DisplayName("existsByEmail() - Should throw exception when email is null")
    void testExistsByEmail_NullEmail() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.existsByEmail(null)
        );

        assertEquals("The email is invalid.", exception.getMessage());
    }

    @Test
    @DisplayName("existsByEmail() - Should be case sensitive")
    void testExistsByEmail_CaseSensitive() {
        repository.save(user1);

        boolean existsLower = repository.existsByEmail("john@example.com");
        boolean existsUpper = repository.existsByEmail("JOHN@EXAMPLE.COM");

        assertTrue(existsLower);
        assertFalse(existsUpper);
    }

    @Test
    @DisplayName("existsByEmail() - Should find email among multiple users")
    void testExistsByEmail_MultipleUsers() {
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);
        boolean exists = repository.existsByEmail("jane@example.com");
        assertTrue(exists);
    }

    @Test
    @DisplayName("existsByEmail() - Should match exact email only")
    void testExistsByEmail_ExactMatch() {
        repository.save(user1);
        boolean existsPartial = repository.existsByEmail("john");
        boolean existsSubstring = repository.existsByEmail("@example.com");
        assertFalse(existsPartial);
        assertFalse(existsSubstring);
    }

    @Test
    @DisplayName("Integration - Should handle complete workflow")
    void testIntegration_CompleteWorkflow() {
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);
        assertEquals(3, repository.findAll().size());
        Optional<User> found = repository.findById(user1.getId());
        assertTrue(found.isPresent());
        List<User> johns = repository.searchByName("John");
        assertEquals(2, johns.size());
        assertTrue(repository.existsByEmail("john@example.com"));
        assertFalse(repository.existsByEmail("notfound@example.com"));
    }
}