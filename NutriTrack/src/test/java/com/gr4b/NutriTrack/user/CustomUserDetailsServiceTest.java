package com.gr4b.NutriTrack.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;

    @BeforeEach
    void initialize() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRole(User.Role.USER);
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails, "UserDetails should not be null");
        assertEquals("test@example.com", userDetails.getUsername(), "Email should match the expected username");
        assertTrue(
                userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")),
                "Authorities should contain 'ROLE_USER'"
        );
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        // Act & Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("test@example.com"),
                "Expected UsernameNotFoundException when user is not found"
        );
    }
}