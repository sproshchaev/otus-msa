package com.prosoft.controller;

import com.prosoft.service.TextTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpperCaseController {

    private final TextTransformationService toUpperCaseService;

    @Autowired
    public UpperCaseController(TextTransformationService toUpperCaseService) {
        this.toUpperCaseService = toUpperCaseService;
    }

    @PostMapping("/transform")
    public String toUpperCase(@RequestBody String request) {
        return toUpperCaseService.transform(request);
    }

}
