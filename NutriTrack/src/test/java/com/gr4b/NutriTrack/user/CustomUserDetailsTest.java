package com.gr4b.NutriTrack.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    private CustomUserDetails customUserDetails;
    private User testUser;

    @BeforeEach
    void initialize() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRole(User.Role.USER);

        customUserDetails = new CustomUserDetails(
                testUser.getId(),
                testUser.getEmail(),
                testUser.getPassword(),
                testUser.getRole(),
                Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );
    }

    @Test
    void shouldReturnCorrectId() {
        assertEquals(1L, customUserDetails.getId());
    }

    @Test
    void shouldReturnCorrectRole() {
        assertEquals(User.Role.USER, customUserDetails.getRole());
    }

    @Test
    void shouldReturnCorrectUsername() {
        assertEquals("test@example.com", customUserDetails.getUsername());
    }

    @Test
    void shouldReturnCorrectPassword() {
        assertEquals("password", customUserDetails.getPassword());
    }

    @Test
    void shouldReturnCorrectAuthorities() {
        assertEquals(1, customUserDetails.getAuthorities().size());
        GrantedAuthority authority = customUserDetails.getAuthorities().iterator().next();
        assertEquals("ROLE_USER", authority.getAuthority());
    }

    @Test
    void shouldBeAccountNonExpired() {
        assertTrue(customUserDetails.isAccountNonExpired());
    }

    @Test
    void shouldBeAccountNonLocked() {
        assertTrue(customUserDetails.isAccountNonLocked());
    }

    @Test
    void shouldBeCredentialsNonExpired() {
        assertTrue(customUserDetails.isCredentialsNonExpired());
    }

    @Test
    void shouldBeEnabled() {
        assertTrue(customUserDetails.isEnabled());
    }
}