package com.upt.NutriTrack.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HighSpendingUser {
    private Long userId;
    private String fullName;
    private String contactEmail;
    private BigDecimal amountSpent;

    public Long fetchUserId() {
        return userId;
    }

    public void updateUserId(Long userId) {
        this.userId = userId;
    }

    public String fetchFullName() {
        return fullName;
    }

    public void updateFullName(String fullName) {
        this.fullName = fullName;
    }

    public String fetchContactEmail() {
        return contactEmail;
    }

    public void updateContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public BigDecimal fetchAmountSpent() {
        return amountSpent;
    }

    public void updateAmountSpent(BigDecimal amountSpent) {
        this.amountSpent = amountSpent;
    }
}