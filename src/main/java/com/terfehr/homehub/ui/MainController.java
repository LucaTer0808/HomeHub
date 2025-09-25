package com.terfehr.homehub.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome to HomeHub!";
    }
}
