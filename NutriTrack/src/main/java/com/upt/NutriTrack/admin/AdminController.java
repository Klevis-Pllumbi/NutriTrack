package com.upt.NutriTrack.admin;

import com.upt.NutriTrack.food.Food;
import com.upt.NutriTrack.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String displayAdminDashboard(Model model) {
        AdminData dashboardData = adminService.fetchAdminDashboardData();
        List<User> users = adminService.fetchAllUsers();

        model.addAttribute("dashboardData", dashboardData);
        model.addAttribute("users", users);
        return "admin-dashboard";
    }

    @GetMapping("/users/{userId}/entries")
    public String displayUserEntries(@PathVariable Long userId, Model model) {
        User user = adminService.fetchUserById(userId);
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            model.addAttribute("error", "User not found");
        }

        List<Food> userEntries = adminService.fetchUserFoodEntries(userId);
        model.addAttribute("userEntries", userEntries);

        double avgCaloriesLastWeek = adminService.fetchAverageCaloriesForUserLastWeek(userId);
        model.addAttribute("avgCaloriesLastWeek", avgCaloriesLastWeek);

        return "user-entries";
    }

    @GetMapping("/entries/{entryId}/edit")
    public String displayEditEntryForm(@PathVariable Long entryId, Model model) {
        Food entry = adminService.fetchFoodEntry(entryId);
        if (entry == null) {
            return "redirect:/admin";
        }
        model.addAttribute("entry", entry);
        return "edit-entry";
    }

    @PostMapping("/entries/{entryId}/edit")
    public String editFoodEntry(@PathVariable Long entryId, @ModelAttribute Food updatedEntry) {
        Food existingEntry = adminService.fetchFoodEntry(entryId);
        if (existingEntry != null) {
            existingEntry.setName(updatedEntry.getName());
            existingEntry.setCals(updatedEntry.getCals());
            existingEntry.setPrice(updatedEntry.getPrice());

            adminService.updateFoodEntry(entryId, existingEntry);
            return "redirect:/admin/users/" + existingEntry.getUser().getId() + "/entries";
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/entries/{entryId}")
    public String removeFoodEntry(@PathVariable Long entryId) {
        adminService.removeFoodEntry(entryId);
        return "redirect:/admin";
    }

    @GetMapping("/users/{userId}/add-entry")
    public String displayAddEntryForm(@PathVariable Long userId, Model model) {
        User user = adminService.fetchUserById(userId);
        if (user == null) {
            return "redirect:/admin";
        }

        Food newEntry = new Food();
        newEntry.setUser(user);
        model.addAttribute("entry", newEntry);
        model.addAttribute("userId", userId);
        return "add-entry";
    }

    @PostMapping("/users/{userId}/add-entry")
    public String addFoodEntry(@PathVariable Long userId, @ModelAttribute Food entry) {
        User user = adminService.fetchUserById(userId);
        if (user != null) {
            entry.setUser(user);
            entry.setCreatedAt(LocalDateTime.now());
            adminService.saveFoodEntry(entry);
            return "redirect:/admin/users/" + userId + "/entries";
        }
        return "redirect:/admin";
    }
}