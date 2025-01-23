package com.gr4b.NutriTrack.food;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class NutritionRequest {

    @NotBlank
    private String itemName;

    @NotNull
    @Min(0)
    private Integer calorieCount;

    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal cost;

    // Getters and Setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getCalorieCount() {
        return calorieCount;
    }

    public void setCalorieCount(Integer calorieCount) {
        this.calorieCount = calorieCount;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}