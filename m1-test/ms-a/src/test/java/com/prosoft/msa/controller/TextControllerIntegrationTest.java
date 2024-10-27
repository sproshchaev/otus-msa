package com.prosoft.msa.controller;

import com.prosoft.msa.service.ToLowerCaseService;
import com.prosoft.msa.service.ToUpperCaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TextControllerIntegrationTest — это интеграционный тест, который тестирует работу контроллера TextController
 * в связке с сервисами ToUpperCaseService и ToLowerCaseService.
 * При этом Spring Boot поднимает весь контекст приложения и использует MockMvc для отправки HTTP-запросов к контроллеру.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TextControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToUpperCaseService toUpperCaseService;

    @MockBean
    private ToLowerCaseService toLowerCaseService;

    @BeforeEach
    void setUp() {
        given(toUpperCaseService.transform(anyString())).willReturn("UPPERCASE_TEXT");
        given(toLowerCaseService.transform(anyString())).willReturn("lowercase_text");
    }

    @Test
    void whenPostToUpperCase_thenReturnsTransformedText() throws Exception {
        mockMvc.perform(post("/toUpperCase")
                        .param("text", "test"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("text", "UPPERCASE_TEXT"))
                .andExpect(view().name("index"));
    }

    @Test
    void whenPostToLowerCase_thenReturnsTransformedText() throws Exception {
        mockMvc.perform(post("/toLowerCase")
                        .param("text", "TEST"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("text", "lowercase_text"))
                .andExpect(view().name("index"));
    }
}
