package com.prosoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TextController {

    @PostMapping("/toUpperCase")
    public String toUpperCase(@RequestParam("text") String text, Model model) {
        String transformedText = text.toUpperCase();
        model.addAttribute("text", transformedText);
        return "index";
    }

    @PostMapping("/toLowerCase")
    public String toLowerCase(@RequestParam("text") String text, Model model) {
        String transformedText = text.toLowerCase();
        model.addAttribute("text", transformedText);
        return "index";
    }
}
