package com.vladdjuga.blogsite.controller;

import com.vladdjuga.blogsite.model.entity.UserEntity;
import com.vladdjuga.blogsite.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"", "/"})
    public String getAll(Model model) {
        model.addAttribute("users", userService.getAll());
        return "user/index"; // templates/user/index.html
    }

    @GetMapping("/create")
    public String createNewUserPage(Model model) {
        model.addAttribute("user", new UserEntity());
        return "user/create"; // templates/user/create.html
    }

    @PostMapping({"", "/"})
    public String createNewUser(UserEntity user) {
        userService.saveUser(user);
        return "redirect:/user/";
    }
}
