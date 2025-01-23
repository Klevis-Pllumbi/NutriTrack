package com.gr4b.NutriTrack.admin;

import com.gr4b.NutriTrack.food.Food;
import com.gr4b.NutriTrack.food.NutritionRepository;
import com.gr4b.NutriTrack.user.User;
import com.gr4b.NutriTrack.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminPageService {

    private final UserRepository userRepository;
    private final NutritionRepository foodRepository;

    @Autowired
    public AdminPageService(UserRepository userRepository, NutritionRepository foodRepository) {
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
    }

    public AdminData fetchAdminDashboardData() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thisWeek = now.minusDays(7);
        LocalDateTime previousWeek = now.minusDays(14);

        int entriesThisWeek = foodRepository.countFoodsWithinPeriod(thisWeek, now);
        int entriesLastWeek = foodRepository.countFoodsWithinPeriod(previousWeek, previousWeek);

        List<HighSpendingUser> highSpendingUsers = fetchHighSpendingUsers();

        return new AdminData(
                entriesThisWeek,
                entriesLastWeek,
                highSpendingUsers
        );
    }

    public AdminData fetchAdminDashboardData(Long userId) {
        AdminData dashboardData = fetchAdminDashboardData();

        LocalDateTime lastWeek = LocalDateTime.now().minusDays(7);
        double avgCaloriesLastWeek = foodRepository.fetchAverageCaloriesForLastWeek(userId, lastWeek);

        // Extend functionality as needed using `avgCaloriesLastWeek`

        return dashboardData;
    }

    protected List<HighSpendingUser> fetchHighSpendingUsers() {
        BigDecimal spendingLimit = BigDecimal.valueOf(1000);
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();

        List<User> allUsers = fetchAllUsers();

        return allUsers.stream()
                .map(user -> {
                    BigDecimal monthlySpending = foodRepository.computeMonthlyExpenses(user.getId(), currentYear, currentMonth);
                    if (monthlySpending != null && monthlySpending.compareTo(spendingLimit) > 0) {
                        return new HighSpendingUser(
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                monthlySpending
                        );
                    }
                    return null;
                })
                .filter(user -> user != null)
                .collect(Collectors.toList());
    }

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public List<Food> fetchUserFoodEntries(Long userId) {
        return foodRepository.fetchFoodsByUserIdSortedByDate(userId);
    }

    @Transactional
    public void removeFoodEntry(Long entryId) {
        foodRepository.deleteById(entryId);
    }

    public User fetchUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public double fetchAverageCaloriesForUserLastWeek(Long userId) {
        LocalDateTime lastWeek = LocalDateTime.now().minusDays(7);
        Double avgCalories = foodRepository.fetchAverageCaloriesForLastWeek(userId, lastWeek);
        return avgCalories != null ? avgCalories : 0;
    }

    public Food fetchFoodEntry(Long entryId) {
        return foodRepository.findById(entryId).orElse(null);
    }

    public void updateFoodEntry(Long entryId, Food updatedEntry) {
        Food existingEntry = foodRepository.findById(entryId).orElse(null);
        if (existingEntry != null) {
            existingEntry.setName(updatedEntry.getName());
            existingEntry.setCals(updatedEntry.getCals());
            existingEntry.setPrice(updatedEntry.getPrice());
            foodRepository.save(existingEntry);
        }
    }

    public void saveFoodEntry(Food entry) {
        foodRepository.save(entry);
    }
}