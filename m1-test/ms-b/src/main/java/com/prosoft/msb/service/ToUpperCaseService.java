package com.prosoft.msb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ToUpperCaseService implements TextTransformationService {

    @Override
    public String transform(String text) {
        log.info("ms-b: Received text for transformation: {}", text);
        String result = text.toUpperCase();
        log.info("ms-b: Transformed text to uppercase: {}", result);
        return result;
    }
}
