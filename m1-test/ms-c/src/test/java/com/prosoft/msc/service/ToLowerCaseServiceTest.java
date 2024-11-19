package com.prosoft.msc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ToLowerCaseServiceTest {

    @Autowired
    private ToLowerCaseService toLowerCaseService;

    @Test
    void whenInputIsValid_thenConvertToLowerCase() {
        String result = toLowerCaseService.transform("HELLO");
        assertEquals("hello", result);
    }

    @Test
    void whenInputIsNull_thenReturnNull() {
        String result = toLowerCaseService.transform(null);
        assertNull(result);
    }

    @Test
    void whenInputIsEmpty_thenReturnEmptyString() {
        String result = toLowerCaseService.transform("");
        assertEquals("", result);
    }
}

