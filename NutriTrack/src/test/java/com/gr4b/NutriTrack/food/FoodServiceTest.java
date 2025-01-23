package com.gr4b.NutriTrack.food;

import com.gr4b.NutriTrack.user.User;
import com.gr4b.NutriTrack.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoodServiceTest {

    @Mock
    private NutritionRepository foodRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private NutritionService foodService;

    private User testUser;

    @BeforeEach
    void initialize() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
    }

    @Test
    void shouldCreateFoodEntrySuccessfully() {

        NutritionRequest request = new NutritionRequest();
        request.setItemName("Pizza");
        request.setCalorieCount(400);
        request.setCost(BigDecimal.valueOf(5.99));

        when(userService.fetchUserById(1L)).thenReturn(testUser);

        foodService.addNutritionEntry(request, 1L);
        verify(foodRepository).save(any(Food.class));
    }

    @Test
    void shouldRetrieveUserFoodEntriesForDay() {

        LocalDateTime startOfDay = LocalDateTime.of(2025, 1, 20, 0, 0);
        List<Food> foodList = List.of(new Food());

        when(foodRepository.fetchFoodsByUserIdWithinDateRange(1L, startOfDay, startOfDay.plusDays(1)))
                .thenReturn(foodList);

        List<Food> result = foodService.getDailyNutritionEntries(1L, startOfDay);
        assertEquals(1, result.size(), "Expected exactly one food entry for the day");
    }

    @Test
    void shouldCalculateDailyCaloriesCorrectly() {

        LocalDateTime startOfDay = LocalDateTime.of(2025, 1, 20, 0, 0);
        when(foodRepository.computeTotalCalories(1L, startOfDay, startOfDay.plusDays(1)))
                .thenReturn(500);

        Integer result = foodService.calculateDailyCalories(1L, startOfDay);
        assertEquals(500, result, "Expected daily calories to match the mock value");
    }

    @Test
    void shouldCalculateMonthlySpendingCorrectly() {

        int year = 2025;
        int month = 1;
        when(foodRepository.computeMonthlyExpenses(1L, year, month))
                .thenReturn(BigDecimal.valueOf(200.00));

        BigDecimal result = foodService.computeMonthlyExpenses(1L, year, month);
        assertEquals(BigDecimal.valueOf(200.00), result, "Expected monthly spending to match the mock value");
    }

    @Test
    void shouldCalculateWeeklySpendingCorrectly() {

        LocalDate startOfWeek = LocalDate.of(2025, 1, 19);
        when(foodRepository.computeWeeklyExpenses(1L, startOfWeek.atStartOfDay(), startOfWeek.plusDays(6).atTime(23, 59, 59)))
                .thenReturn(BigDecimal.valueOf(50.00));

        BigDecimal result = foodService.computeWeeklyExpenses(1L, startOfWeek);
        assertEquals(BigDecimal.valueOf(50.00), result, "Expected weekly spending to match the mock value");
    }

    @Test
    void shouldDeleteFoodEntrySuccessfully() {

        Food food = new Food();
        food.setId(1L);

        when(foodRepository.fetchFoodDetailsByIdAndUserId(1L, 1L)).thenReturn(food);

        foodService.removeNutritionEntry(1L, 1L);
        verify(foodRepository).delete(food);
    }

    @Test
    void shouldNotDeleteFoodEntryIfNotFound() {

        when(foodRepository.fetchFoodDetailsByIdAndUserId(1L, 1L)).thenReturn(null);

        foodService.removeNutritionEntry(1L, 1L);
        verify(foodRepository, never()).delete(any(Food.class));
    }

    @Test
    void shouldRetrieveUserFoodEntriesBetweenDates() {

        LocalDate fromDate = LocalDate.of(2025, 1, 1);
        LocalDate toDate = LocalDate.of(2025, 1, 5);
        List<Food> foodList = List.of(new Food());

        when(foodRepository.fetchFoodsByUserIdWithinDateRange(1L, fromDate.atStartOfDay(), toDate.plusDays(1).atStartOfDay()))
                .thenReturn(foodList);

        List<Food> result = foodService.getNutritionEntriesForPeriod(1L, fromDate, toDate);
        assertEquals(1, result.size(), "Expected one food entry between the specified dates");
    }
}