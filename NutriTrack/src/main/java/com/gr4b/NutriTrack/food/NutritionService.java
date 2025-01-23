package com.gr4b.NutriTrack.food;

import com.gr4b.NutriTrack.user.User;
import com.gr4b.NutriTrack.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NutritionService {

    private final NutritionRepository nutritionRepository;
    private final UserService userService;

    @Autowired
    public NutritionService(NutritionRepository nutritionRepository, UserService userService) {
        this.nutritionRepository = nutritionRepository;
        this.userService = userService;
    }

    public void addNutritionEntry(NutritionRequest request, Long userId) {
        User user = userService.fetchUserById(userId);
        Food food = new Food();
        food.setUser(user);
        food.setName(request.getItemName());
        food.setCals(request.getCalorieCount());
        food.setPrice(request.getCost());
        nutritionRepository.save(food);
    }

    public void removeNutritionEntry(Long foodId, Long userId) {
        Food food = nutritionRepository.fetchFoodDetailsByIdAndUserId(foodId, userId);
        if (food != null) {
            nutritionRepository.delete(food);
        }
    }

    public List<Food> getDailyNutritionEntries(Long userId, LocalDateTime startOfDay) {
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return nutritionRepository.fetchFoodsByUserIdWithinDateRange(userId, startOfDay, endOfDay);
    }

    public List<Food> getNutritionEntriesForPeriod(Long userId, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime start = fromDate.atStartOfDay();
        LocalDateTime end = toDate.plusDays(1).atStartOfDay();
        return nutritionRepository.fetchFoodsByUserIdWithinDateRange(userId, start, end);
    }

    public Integer calculateDailyCalories(Long userId, LocalDateTime startOfDay) {
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return nutritionRepository.computeTotalCalories(userId, startOfDay, endOfDay);
    }

    public BigDecimal computeMonthlyExpenses(Long userId, int year, int month) {
        return nutritionRepository.computeMonthlyExpenses(userId, year, month);
    }

    public BigDecimal computeWeeklyExpenses(Long userId, LocalDate startOfWeek) {
        LocalDateTime start = startOfWeek.atStartOfDay();
        LocalDateTime end = startOfWeek.plusDays(6).atTime(23, 59, 59);
        return nutritionRepository.computeWeeklyExpenses(userId, start, end);
    }

    public List<Map<String, Object>> getLastSevenDaysCalories(Long userId) {
        LocalDate today = LocalDate.now();
        return today.minusDays(6).datesUntil(today.plusDays(1))
                .sorted(Comparator.reverseOrder())
                .map(date -> {
                    Integer dailyCalories = calculateDailyCalories(userId, date.atStartOfDay());
                    Map<String, Object> result = Map.of(
                            "date", date.toString(),
                            "calories", dailyCalories != null ? dailyCalories : 0
                    );
                    return result;
                })
                .collect(Collectors.toList());
    }
}