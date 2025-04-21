package com.monitoring.finance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController { // will be login controller

    @GetMapping("/")
    public String home() {
        return "index"; // index.html
    }

}
