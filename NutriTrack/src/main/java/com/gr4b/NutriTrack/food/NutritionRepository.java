package com.gr4b.NutriTrack.food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface NutritionRepository extends JpaRepository<Food, Long> {

    List<Food> fetchFoodsByUserIdSortedByDate(Long userId);

    List<Food> fetchFoodsByUserIdWithinDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    Food fetchFoodDetailsByIdAndUserId(Long foodId, Long userId);

    @Query("SELECT COALESCE(SUM(f.cals), 0) FROM Food f WHERE f.user.id = ?1 AND f.createdAt BETWEEN ?2 AND ?3")
    Integer computeTotalCalories(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(f.price) FROM Food f " +
            "WHERE f.user.id = :userId " +
            "AND YEAR(f.createdAt) = :year " +
            "AND MONTH(f.createdAt) = :month")
    BigDecimal computeMonthlyExpenses(
            @Param("userId") Long userId,
            @Param("year") int year,
            @Param("month") int month);

    @Query("SELECT SUM(f.price) FROM Food f " +
            "WHERE f.user.id = :userId " +
            "AND f.createdAt BETWEEN :startOfWeek AND :endOfWeek")
    BigDecimal computeWeeklyExpenses(
            @Param("userId") Long userId,
            @Param("startOfWeek") LocalDateTime startOfWeek,
            @Param("endOfWeek") LocalDateTime endOfWeek);

    @Query("SELECT COUNT(f) FROM Food f WHERE f.createdAt BETWEEN :startDate AND :endDate")
    int countFoodsWithinPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(AVG(f.cals), 0) FROM Food f WHERE f.user.id = :userId AND f.createdAt >= :lastWeekStart")
    Double fetchAverageCaloriesForLastWeek(@Param("userId") Long userId, @Param("lastWeekStart") LocalDateTime lastWeekStart);
}