package com.prosoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ServiceA: MainController
 */
@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

}
