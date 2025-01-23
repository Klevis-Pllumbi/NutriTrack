package com.gr4b.NutriTrack.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addNewUser(User user) {
        user.setPassword(this.hashPassword(user.getPassword()));
        this.userRepository.save(user);
    }

    public User retrieveByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User fetchUserById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User not found with id: %d", userId)));
    }

    private String hashPassword(String password) {
        return this.passwordEncoder.encode(password);
    }
}

class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
