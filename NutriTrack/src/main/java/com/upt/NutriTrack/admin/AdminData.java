package com.upt.NutriTrack.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminData {

    private int entriesThisWeek;
    private int entriesLastWeek;
    private List<HighSpendingUser> highSpendingUser;

    public int getEntriesThisWeek() {
        return entriesThisWeek;
    }

    public void setEntriesThisWeek(int entriesThisWeek) {
        this.entriesThisWeek = entriesThisWeek;
    }

    public int getEntriesLastWeek() {
        return entriesLastWeek;
    }

    public void setEntriesLastWeek(int entriesLastWeek) {
        this.entriesLastWeek = entriesLastWeek;
    }


    public List<HighSpendingUser> gethighSpendingUser() {
        return highSpendingUser;
    }

    public void sethighSpendingUser(List<HighSpendingUser> highSpendingUser) {
        this.highSpendingUser = highSpendingUser;
    }
}

