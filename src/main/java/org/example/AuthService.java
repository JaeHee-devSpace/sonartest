package org.example;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) return false;
        return user.getPassword().equals(password);
    }
}
