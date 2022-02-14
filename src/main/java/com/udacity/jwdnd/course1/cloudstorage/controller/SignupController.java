package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {
    UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    // Display sign up page
    @GetMapping()
    public String displaySignUpPage() {
        return "signup";
    }
    // Sign up process
    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model) {
        String error = null;
        if (!userService.isUserNameAvailable(user.getUserName())) {
            error = "This user is already in the file.";
        }
        if (error == null) {
            int createUser = userService.createUser(user);
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError", error);
        }
        return "signup";
    }
}
