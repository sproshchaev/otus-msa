package com.prosoft.msb.contract;

import com.prosoft.msb.ServiceB;
import com.prosoft.msb.controller.UpperCaseController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ServiceB.class)
public class BaseClass {

    @Autowired
    UpperCaseController upperCaseController;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(upperCaseController);
    }

}
