package com.gr4b.NutriTrack.admin;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminData {

    private int entriesThisWeek;
    private int entriesLastWeek;
    private List<HighSpendingUser> highSpendingUser;

}