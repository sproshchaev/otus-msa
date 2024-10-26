package com.prosoft.msa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${service.lowercase.url}")
    private String lowerCaseUrl;

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
                lowerCaseUrl,
                HttpMethod.POST,
                new HttpEntity<>(text, headers),
                String.class
        ).getBody();
    }
}
