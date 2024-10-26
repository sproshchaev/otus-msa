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
public class ToLowerCaseServiceImpl implements ToLowerCaseService {

    private final RestTemplate restTemplate;

    @Autowired
    public ToLowerCaseServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String transform(String text) {
        log.info("Sending text for LowerCase transformation: {}", text);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return restTemplate.exchange(
                "http://localhost:8082/transform",
                HttpMethod.POST,
                new HttpEntity<>(text, headers),
                String.class
        ).getBody();
    }
}
