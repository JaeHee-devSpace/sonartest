package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        UserRepository repo = new UserRepository();
        repo.save(new User("user@example.com", "1234"));
        authService = new AuthService(repo);
    }

    @Test
    void loginShouldSucceed() {
        assertTrue(authService.login("user@example.com", "1234"));
    }
}
