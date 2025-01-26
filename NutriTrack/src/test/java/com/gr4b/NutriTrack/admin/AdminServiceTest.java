package com.gr4b.NutriTrack.admin;

import com.gr4b.NutriTrack.food.Food;
import com.gr4b.NutriTrack.food.NutritionRepository;
import com.gr4b.NutriTrack.user.User;
import com.gr4b.NutriTrack.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NutritionRepository nutritionRepository;

    private AdminService adminService;

    private User testUser;

    private Food testFoodEntry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminService = new AdminService(userRepository, nutritionRepository);
        testUser = new User(1L, "Test User", "testuser@example.com", "password123", User.Role.USER);


        testFoodEntry = new Food();
        testFoodEntry.setId(1L);
        testFoodEntry.setUser(testUser);
        testFoodEntry.setName("Pizza");
        testFoodEntry.setCals(500);
        testFoodEntry.setPrice(BigDecimal.valueOf(10.99));
        testFoodEntry.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        List<User> result = adminService.fetchAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getId(), result.get(0).getId());
    }

    @Test
    void testDeleteEntry() {
        Long entryId = 1L;
        doNothing().when(nutritionRepository).deleteById(entryId);

        adminService.removeFoodEntry(entryId);

        verify(nutritionRepository, times(1)).deleteById(entryId);
    }

    @Test
    void testFindById() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        User result = adminService.fetchUserById(userId);

        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
    }

    @Test
    void testGetFoodEntry() {
        Long entryId = 1L;
        when(nutritionRepository.findById(entryId)).thenReturn(Optional.of(testFoodEntry));

        Food result = adminService.fetchFoodEntry(entryId);

        assertNotNull(result);
        assertEquals(testFoodEntry.getId(), result.getId());
    }

    @Test
    void testSaveUserEntry() {
        when(nutritionRepository.save(any(Food.class))).thenReturn(testFoodEntry);

        adminService.saveFoodEntry(testFoodEntry);

        verify(nutritionRepository, times(1)).save(testFoodEntry);
    }
}
