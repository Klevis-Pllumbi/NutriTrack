package com.gr4b.NutriTrack.food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface NutritionRepository extends JpaRepository<Food, Long> {

    @Query("SELECT f FROM Food f WHERE f.user.id = :userId ORDER BY f.createdAt DESC")
    List<Food> fetchFoodsByUserIdSortedByDate(@Param("userId") Long userId);

    @Query("SELECT f FROM Food f WHERE f.user.id = :userId AND f.createdAt BETWEEN :startDate AND :endDate")
    List<Food> fetchFoodsByUserIdWithinDateRange(@Param("userId") Long userId,
                                                 @Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f FROM Food f WHERE f.id = :foodId AND f.user.id = :userId")
    Food fetchFoodDetailsByIdAndUserId(@Param("foodId") Long foodId, @Param("userId") Long userId);

    // Compute total calories within a date range for a user
    @Query("SELECT COALESCE(SUM(f.cals), 0) FROM Food f WHERE f.user.id = :userId AND f.createdAt BETWEEN :startDate AND :endDate")
    Integer computeTotalCalories(@Param("userId") Long userId,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);

    // Compute monthly expenses for a user
    @Query("SELECT COALESCE(SUM(f.price), 0) FROM Food f " +
            "WHERE f.user.id = :userId " +
            "AND YEAR(f.createdAt) = :year " +
            "AND MONTH(f.createdAt) = :month")
    BigDecimal computeMonthlyExpenses(@Param("userId") Long userId,
                                      @Param("year") int year,
                                      @Param("month") int month);

    // Compute weekly expenses for a user
    @Query("SELECT COALESCE(SUM(f.price), 0) FROM Food f " +
            "WHERE f.user.id = :userId " +
            "AND f.createdAt BETWEEN :startOfWeek AND :endOfWeek")
    BigDecimal computeWeeklyExpenses(@Param("userId") Long userId,
                                     @Param("startOfWeek") LocalDateTime startOfWeek,
                                     @Param("endOfWeek") LocalDateTime endOfWeek);

    // Count foods within a specific time period
    @Query("SELECT COUNT(f) FROM Food f WHERE f.createdAt BETWEEN :startDate AND :endDate")
    int countFoodsWithinPeriod(@Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate);

    // Fetch average calories for the last week for a user
    @Query("SELECT COALESCE(AVG(f.cals), 0) FROM Food f WHERE f.user.id = :userId AND f.createdAt >= :lastWeekStart")
    Double fetchAverageCaloriesForLastWeek(@Param("userId") Long userId,
                                           @Param("lastWeekStart") LocalDateTime lastWeekStart);
}