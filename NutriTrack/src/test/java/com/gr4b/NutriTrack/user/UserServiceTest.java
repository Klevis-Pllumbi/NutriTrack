package com.gr4b.NutriTrack.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;  // Correct import

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;  // Correct JUnit 5 assertion import
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;  // Correct PasswordEncoder declaration

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); }


    @Test
    public void registerUser_ShouldEncodePasswordAndSaveUser() {

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("plainPassword");

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode("plainPassword")).thenReturn(encodedPassword);

        userService.addNewUser(user);

        assertEquals(encodedPassword, user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void findByEmail_ShouldReturnUserIfFound() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.retrieveByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }



}
