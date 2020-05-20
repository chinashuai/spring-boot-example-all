package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActuatorController {
	
    @RequestMapping("/one")
    public String one() {
        return "ok";
    }
}