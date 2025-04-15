package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceFailureTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        UserRepository repo = new UserRepository();
        repo.save(new User("user@example.com", "1234"));
        authService = new AuthService(repo);
    }

    @Test
    void loginFailsWithWrongEmail() {
        assertFalse(authService.login("wrong@example.com", "1234"));
    }

    @Test
    void loginFailsWithWrongPassword() {
        assertFalse(authService.login("user@example.com", "wrongpass"));
    }

    @Test
    void loginFailsWhenEmpty() {
        assertFalse(authService.login("", ""));
    }
}
