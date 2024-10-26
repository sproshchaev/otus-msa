package com.prosoft.msc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ToLowerCaseService implements TextTransformationService {

    @Override
    public String transform(String text) {
        log.info("ms-c: Received text for transformation: {}", text);
        String result = text.toLowerCase();
        log.info("ms-c: Transformed text to lowercase: {}", result);
        return text.toLowerCase();
    }


}