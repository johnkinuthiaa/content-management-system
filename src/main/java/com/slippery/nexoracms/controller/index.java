package com.slippery.nexoracms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping
@CrossOrigin
public class index {
    @GetMapping("/")
    public String getIndex(){
        return "index.html";
    }
}
