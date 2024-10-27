package com.prosoft.msa.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ToUpperCaseServiceImplTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ToUpperCaseService toUpperCaseService;

    @Value("${service.uppercase.url}")
    private String upperCaseUrl;

    @Test
    void transform_ShouldReturnUpperCaseString() {
        // Arrange
        String inputText = "hello world";
        String expectedOutput = "HELLO WORLD";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedOutput, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(upperCaseUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        // Act
        String result = toUpperCaseService.transform(inputText);

        // Assert
        assertEquals(expectedOutput, result);
        verify(restTemplate).exchange(
                eq(upperCaseUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test
    void transform_WithEmptyString_ShouldReturnEmptyString() {
        // Arrange
        String inputText = "";
        String expectedOutput = "";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedOutput, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(upperCaseUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        // Act
        String result = toUpperCaseService.transform(inputText);

        // Assert
        assertEquals(expectedOutput, result);
    }
}