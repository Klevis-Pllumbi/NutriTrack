package com.gr4b.NutriTrack.user;

import com.gr4b.NutriTrack.user.CustomUserDetails;
import com.gr4b.NutriTrack.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    private CustomUserDetails customUserDetails;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(User.Role.USER);

        customUserDetails = new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void testGetId() {
        assertEquals(1L, customUserDetails.getId());
    }

    @Test
    void testGetRole() {
        assertEquals(User.Role.USER, customUserDetails.getRole());
    }

    @Test
    void testGetUsername() {
        assertEquals("test@example.com", customUserDetails.getUsername());
    }

    @Test
    void testGetPassword() {
        assertEquals("password", customUserDetails.getPassword());
    }

    @Test
    void testGetAuthorities() {
        assertEquals(1, customUserDetails.getAuthorities().size());
        GrantedAuthority authority = customUserDetails.getAuthorities().iterator().next();
        assertEquals("ROLE_USER", authority.getAuthority());
    }

    @Test
    void testIsEnabled() {
        assertTrue(customUserDetails.isEnabled());
    }
}
