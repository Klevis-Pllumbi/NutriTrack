package com.gr4b.NutriTrack.food;

import com.gr4b.NutriTrack.user.User;
import com.gr4b.NutriTrack.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NutritionServiceTest {

    @Mock
    private NutritionRepository nutritionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private NutritionService nutritionService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
    }

    @Test
    void testPushFoodEntry() {
        NutritionRequest request = new NutritionRequest();
        request.setItemName("Pizza");
        request.setCalorieCount(400);
        request.setCost(BigDecimal.valueOf(5.99));
        when(userService.fetchUserById(1L)).thenReturn(user);
        nutritionService.addNutritionEntry(request, 1L);

        verify(nutritionRepository).save(any(Food.class));
    }

    @Test
    void testGetEntriesToday() {
        LocalDateTime startOfDay = LocalDateTime.of(2025, 1, 20, 0, 0);
        List<Food> foodList = List.of(new Food());
        when(nutritionRepository.fetchFoodsByUserIdWithinDateRange(1L, startOfDay, startOfDay.plusDays(1)))
                .thenReturn(foodList);

        List<Food> result = nutritionService.getDailyNutritionEntries(1L, startOfDay);
        assertEquals(1, result.size());
    }

    @Test
    void testGetDailyCals() {
        LocalDateTime startOfDay = LocalDateTime.of(2025, 1, 20, 0, 0);
        when(nutritionRepository.computeTotalCalories(1L, startOfDay, startOfDay.plusDays(1)))
                .thenReturn(500);

        Integer result = nutritionService.calculateDailyCalories(1L, startOfDay);
        assertEquals(500, result);
    }

    @Test
    void testGetMonthlySpending() {
        int year = 2025;
        int month = 1;
        when(nutritionRepository.computeMonthlyExpenses(1L, year, month))
                .thenReturn(BigDecimal.valueOf(200.00));

        BigDecimal result = nutritionService.computeMonthlyExpenses(1L, year, month);
        assertEquals(BigDecimal.valueOf(200.00), result);
    }

    @Test
    void testRemoveEntry() {
        Food food = new Food();
        food.setId(1L);
        when(nutritionRepository.fetchFoodDetailsByIdAndUserId(1L, 1L)).thenReturn(food);
        nutritionService.removeNutritionEntry(1L, 1L);

        verify(nutritionRepository).delete(food);
    }

}
