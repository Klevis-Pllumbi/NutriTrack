package com.upt.NutriTrack.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String showHomepage() {
        return "homepage";
    }

    @GetMapping("/login")
    public String displayLoginPage(@RequestParam(value = "error", required = false) String error,
                                   @RequestParam(value = "logout", required = false) String logout,
                                   Model model) {
        model.addAttribute("error", error != null ? "Invalid email or password" : "");
        model.addAttribute("message", logout != null ? "You have been logged out successfully" : "");
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String handleUserRegistration(@ModelAttribute User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/user/login?registered=true";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}