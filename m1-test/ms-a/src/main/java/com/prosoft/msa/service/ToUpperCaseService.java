package com.prosoft.msa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class ToUpperCaseService implements TextTransformationService {

    private final RestTemplate restTemplate;

    @Autowired
    public ToUpperCaseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String transform(String text) {
        log.info("Sending text for UpperCase transformation: {}", text);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return restTemplate.exchange(
                "http://localhost:8081/transform",
                HttpMethod.POST,
                new HttpEntity<>(text, headers),
                String.class
        ).getBody();
    }
}
