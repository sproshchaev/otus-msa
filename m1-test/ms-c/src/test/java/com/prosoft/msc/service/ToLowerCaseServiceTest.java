package com.prosoft.msc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * ToLowerCaseServiceTest - класс для мутационного тестирования.
 * Генерация тестов: mvn clean install
 * Запуск мутационного тестирования: mvn pitest:mutationCoverage
 * Файлы с результатами мутационного тестирования:
 * <pre>
 * target/
 * ├── classes/
 * ├── generated-sources/
 * ├── generated-test-sources/
 * ├── maven-archiver/
 * ├── maven-status/
 * └── pit-reports/
 *     ├── index.html                       - общий отчет
 *     ├── style.css
 *     └── com.prosoft.msc.service/
 *         ├── index.html                   - отчет по классу
 *         └── ToLowerCaseService.java.html - отчет по классу
 * </pre>
 */
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

