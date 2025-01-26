package com.gr4b.NutriTrack.food;

import com.gr4b.NutriTrack.user.CustomUserDetails;
import com.gr4b.NutriTrack.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class NutritionControllerTest {

    @Mock
    private NutritionService nutritionService;

    @InjectMocks
    private NutritionController nutritionController;

    private CustomUserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUserDetails = new CustomUserDetails(
                1L,
                "user@example.com",
                "password",
                User.Role.USER,
                Collections.emptyList()
        );
    }

    @Test
    void testShowDashboard() {
        Model model = new BindingAwareModelMap();
        List<NutritionResponse> mockResponses = List.of(
                new NutritionResponse(1L, "Food1", 300, BigDecimal.valueOf(10.00), LocalDateTime.now()),
                new NutritionResponse(2L, "Food2", 500, BigDecimal.valueOf(15.00), LocalDateTime.now())
        );

        when(nutritionService.getDailyNutritionEntries(eq(1L), any())).thenReturn(Collections.emptyList());
        when(nutritionService.calculateDailyCalories(eq(1L), any())).thenReturn(800);
        when(nutritionService.computeMonthlyExpenses(eq(1L), anyInt(), anyInt())).thenReturn(new BigDecimal("50.00"));
        when(nutritionService.computeWeeklyExpenses(eq(1L), any())).thenReturn(new BigDecimal("30.00"));
        when(nutritionService.getLastSevenDaysCalories(eq(1L))).thenReturn(List.of(Map.of("date", "2025-01-18", "cals", 800)));

        String viewName = nutritionController.showDashboard(mockUserDetails, model);

        assertEquals("dashboard", viewName);
        assertTrue(model.containsAttribute("foodEntries"));
        assertTrue(model.containsAttribute("dailyCalories"));
        assertTrue(model.containsAttribute("monthlySpending"));
        assertTrue(model.containsAttribute("weeklySpending"));
        assertTrue(model.containsAttribute("last7DaysCalories"));
        assertTrue(model.containsAttribute("userRole"));
    }

    @Test
    void testShowPushPage() {
        Model model = new BindingAwareModelMap();
        String viewName = nutritionController.showPushPage(model);

        assertEquals("push", viewName);
        assertTrue(model.containsAttribute("foodRequest"));
    }

    @Test
    void testPushFoodEntry() {
        NutritionRequest request = new NutritionRequest();
        request.setItemName("Food1");
        request.setCalorieCount(300);
        request.setCost(new BigDecimal("10.00"));

        String viewName = nutritionController.addNutritionEntry(request, mockUserDetails);

        assertEquals("redirect:/dashboard", viewName);
        verify(nutritionService, times(1)).addNutritionEntry(eq(request), eq(1L));
    }

    @Test
    void testRemoveEntry() {
        String viewName = nutritionController.deleteFoodEntry(1L, mockUserDetails);

        assertEquals("redirect:/dashboard", viewName);
        verify(nutritionService, times(1)).removeNutritionEntry(eq(1L), eq(1L));
    }

    @Test
    void testGetDailyCals() {
        Model model = new BindingAwareModelMap();

        when(nutritionService.calculateDailyCalories(eq(1L), any())).thenReturn(800);

        String viewName = nutritionController.getDailyCals(LocalDate.now(), mockUserDetails, model);

        assertEquals("dailyCals", viewName);
        assertTrue(model.containsAttribute("cals"));
    }

    @Test
    void testGetMonthlySpending() {
        Model model = new BindingAwareModelMap();

        when(nutritionService.computeMonthlyExpenses(eq(1L), anyInt(), anyInt())).thenReturn(new BigDecimal("100.00"));

        String viewName = nutritionController.getMonthlySpending(2025, 1, mockUserDetails, model);

        assertEquals("monthlySpending", viewName);
        assertTrue(model.containsAttribute("spending"));
    }

    @Test
    void testGetJournalWithDates() {
        Model model = new BindingAwareModelMap();
        List<Food> mockEntries = List.of(
                createMockFood(1L, "Food1", 300, new BigDecimal("10.00"), LocalDateTime.now()),
                createMockFood(2L, "Food2", 500, new BigDecimal("15.00"), LocalDateTime.now().minusDays(1))
        );

        when(nutritionService.getNutritionEntriesForPeriod(eq(1L), any(), any())).thenReturn(mockEntries);

        String viewName = nutritionController.getJournal(LocalDate.now().minusDays(7), LocalDate.now(), mockUserDetails, model);

        assertEquals("journal", viewName);
        assertTrue(model.containsAttribute("groupedEntries"));
    }

    @Test
    void testGetJournalWithoutDates() {
        Model model = new BindingAwareModelMap();

        when(nutritionService.getNutritionEntriesForPeriod(eq(1L), any(), any())).thenThrow(RuntimeException.class);

        String viewName = nutritionController.getJournal(null, null, mockUserDetails, model);

        assertEquals("journal", viewName);
        assertTrue(model.containsAttribute("error"));
    }

    private Food createMockFood(Long id, String name, int cals, BigDecimal price, LocalDateTime createdAt) {
        Food food = new Food();
        food.setId(id);
        food.setName(name);
        food.setCals(cals);
        food.setPrice(price);
        food.setCreatedAt(createdAt);
        return food;
    }
}
