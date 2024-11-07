package com.prosoft.msa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.prosoft:ms-b:+:stubs:8100"}, stubsMode = StubRunnerProperties.StubsMode.LOCAL)
class ContractIntegrationTest {

    @Test
    void getUpperCaseString() {

        // Arrange
        RestTemplate restTemplate = new RestTemplate();
        String request = "hello!";
        String expectedResponse = "HELLO!";

        // Act
        String result = restTemplate.postForObject("http://localhost:8100//transform", request, String.class);

        // Assert
        assertEquals(expectedResponse, result);
    }

}
