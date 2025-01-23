package com.gr4b.NutriTrack.food;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NutritionResponse {

    private Long entryId;
    private String itemName;
    private Integer calorieCount;
    private BigDecimal cost;
    private LocalDateTime timestamp;
    private String formattedTimestamp;

    public NutritionResponse(Long entryId, String itemName, Integer calorieCount, BigDecimal cost, LocalDateTime timestamp) {
        this.entryId = entryId;
        this.itemName = itemName;
        this.calorieCount = calorieCount;
        this.cost = cost;
        this.timestamp = timestamp;
        this.formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormattedTimestamp() {
        return formattedTimestamp;
    }

    public void setFormattedTimestamp(String formattedTimestamp) {
        this.formattedTimestamp = formattedTimestamp;
    }

    public String extractTime() {
        if (this.timestamp == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return timestamp.format(formatter);
    }
}
