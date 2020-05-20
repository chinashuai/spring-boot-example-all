package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ThymeleafController {

    @RequestMapping("/index")
    public String string(ModelMap map) {
        map.addAttribute("index", "http://www.chinaxieshuai.com");
        return "index";
    }

}