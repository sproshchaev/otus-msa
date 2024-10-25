package com.prosoft.service;

import org.springframework.stereotype.Service;

@Service
public class ToLowerCaseService implements TextTransformationService {

    @Override
    public String transform(String text) {
        return text.toLowerCase();
    }
}
