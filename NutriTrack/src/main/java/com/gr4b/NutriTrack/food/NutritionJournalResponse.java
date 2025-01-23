package com.gr4b.NutriTrack.food;

import java.util.List;

public class NutritionJournalResponse {
    private List<NutritionResponse> foodEntries;
    private int calorieSum;

    public NutritionJournalResponse(List<NutritionResponse> foodEntries, int calorieSum) {
        this.foodEntries = foodEntries;
        this.calorieSum = calorieSum;
    }

    public List<NutritionResponse> getFoodEntries() {
        return foodEntries;
    }

    public void setFoodEntries(List<NutritionResponse> foodEntries) {
        this.foodEntries = foodEntries;
    }

    public int getCalorieSum() {
        return calorieSum;
    }

    public void setCalorieSum(int calorieSum) {
        this.calorieSum = calorieSum;
    }
}