package com.prosoft.msa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.prosoft:ms-b:+:stubs:8100"}, stubsMode = StubRunnerProperties.StubsMode.LOCAL)
class ContractIntegrationTest {

    @Test
    void getUpperCaseString() {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(restTemplate.getForObject("http://localhost:8100//transform", String.class));
    }

}
