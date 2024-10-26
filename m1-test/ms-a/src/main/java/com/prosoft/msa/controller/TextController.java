package com.prosoft.msa.controller;

import com.prosoft.msa.service.TextTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ServiceA: TextController
 */
@Controller
public class TextController {

    private final TextTransformationService toUpperCaseService;
    private final TextTransformationService toLowerCaseService;

    private static final String INDEX_VIEW = "index";
    private static final String TEXT_ATTRIBUTE = "text";

    @Autowired
    public TextController(TextTransformationService toUpperCaseService, TextTransformationService toLowerCaseService) {
        this.toUpperCaseService = toUpperCaseService;
        this.toLowerCaseService = toLowerCaseService;
    }

    @GetMapping("/")
    public String index() {
        return INDEX_VIEW;
    }

    @PostMapping("/toUpperCase")
    public String toUpperCase(@RequestParam("text") String text, Model model) {
        String transformedText = toUpperCaseService.transform(text);
        model.addAttribute(TEXT_ATTRIBUTE, transformedText);
        return INDEX_VIEW;
    }

    @PostMapping("/toLowerCase")
    public String toLowerCase(@RequestParam("text") String text, Model model) {
        String transformedText = toLowerCaseService.transform(text);
        model.addAttribute(TEXT_ATTRIBUTE, transformedText);
        return INDEX_VIEW;
    }
}
