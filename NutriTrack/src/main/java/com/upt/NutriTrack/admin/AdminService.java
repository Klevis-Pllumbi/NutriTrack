package com.upt.NutriTrack.admin;

import com.upt.NutriTrack.food.Food;
import com.upt.NutriTrack.food.NutritionRepository;
import com.upt.NutriTrack.user.User;
import com.upt.NutriTrack.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepo;
    private final NutritionRepository foodRepo;

    @Autowired
    public AdminService(UserRepository userRepo, NutritionRepository foodRepo) {
        this.userRepo = userRepo;
        this.foodRepo = foodRepo;
    }

    public AdminData fetchAdminDashboardData() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastWeekStart = now.minusDays(7);
        LocalDateTime lastWeekEnd = now.minusDays(14);

        int entriesThisWeek = foodRepo.countFoodsWithinPeriod(lastWeekStart, now);
        int entriesLastWeek = foodRepo.countFoodsWithinPeriod(lastWeekEnd, lastWeekEnd);

        List<HighSpendingUser> highSpendingUsers = fetchHighSpendingUsers();

        return new AdminData(entriesThisWeek, entriesLastWeek, highSpendingUsers);
    }

    public AdminData fetchAdminDashboardData(Long userId) {
        AdminData dashboardData = fetchAdminDashboardData();

        LocalDateTime lastWeek = LocalDateTime.now().minusDays(7);
        double avgCaloriesLastWeek = foodRepo.fetchAverageCaloriesForLastWeek(userId, lastWeek);

        return dashboardData;
    }

    protected List<HighSpendingUser> fetchHighSpendingUsers() {
        BigDecimal spendingThreshold = new BigDecimal("1000");

        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        List<User> allUsers = fetchAllUsers();

        return allUsers.stream()
                .map(user -> {
                    BigDecimal monthlySpending = foodRepo.computeMonthlyExpenses(user.getId(), currentYear, currentMonth);
                    if (monthlySpending != null && monthlySpending.compareTo(spendingThreshold) > 0) {
                        return new HighSpendingUser(user.getId(), user.getName(), user.getEmail(), monthlySpending);
                    }
                    return null;
                })
                .filter(user -> user != null)
                .collect(Collectors.toList());
    }

    public List<User> fetchAllUsers() {
        return userRepo.findAll();
    }

    public List<Food> fetchUserFoodEntries(Long userId) {
        return foodRepo.fetchFoodsByUserIdSortedByDate(userId);
    }

    @Transactional
    public void removeFoodEntry(Long foodEntryId) {
        foodRepo.deleteById(foodEntryId);
    }

    public User fetchUserById(Long userId) {
        Optional<User> userOptional = userRepo.findById(userId);
        return userOptional.orElse(null);
    }

    public double fetchAverageCaloriesForUserLastWeek(Long userId) {
        LocalDateTime lastWeek = LocalDateTime.now().minusDays(7);
        Double avgCalories = foodRepo.fetchAverageCaloriesForLastWeek(userId, lastWeek);
        return avgCalories != null ? avgCalories : 0;
    }

    public Food fetchFoodEntry(Long foodEntryId) {
        return foodRepo.findById(foodEntryId).orElse(null);
    }

    public void updateFoodEntry(Long foodEntryId, Food updatedFoodEntry) {
        Food existingFoodEntry = foodRepo.findById(foodEntryId).orElse(null);
        if (existingFoodEntry != null) {
            existingFoodEntry.setName(updatedFoodEntry.getName());
            existingFoodEntry.setCals(updatedFoodEntry.getCals());
            existingFoodEntry.setPrice(updatedFoodEntry.getPrice());
            foodRepo.save(existingFoodEntry);
        }
    }

    public void saveFoodEntry(Food newFoodEntry) {
        foodRepo.save(newFoodEntry);
    }
}