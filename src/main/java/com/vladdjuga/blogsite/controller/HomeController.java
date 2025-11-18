package com.vladdjuga.blogsite.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
class HomeController {
    @RequestMapping("/home")
    public String home(){
        return "home";
    }
}
