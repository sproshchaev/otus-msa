package com.prosoft.controller;

import com.prosoft.service.TextTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LowerCaseController {

    private final TextTransformationService toLowerCaseService;

    @Autowired
    public LowerCaseController(TextTransformationService toLowerCaseService) {
        this.toLowerCaseService = toLowerCaseService;
    }

    @PostMapping("/transform")
    public String toLowerCase(@RequestBody String request) {
        return toLowerCaseService.transform(request);
    }

}
