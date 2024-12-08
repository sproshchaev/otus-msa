package com.prosoft.msb.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ToUpperCaseServiceTest {

    /**
     * Пример создания мок для интерфейса TextTransformationService и задание его поведения для полученной строки "hello".
     * Такой подход используется, если необходимо протестировать логику, которая зависит от интерфейса, без использования
     * настоящей реализации.
     */
    @Test
    void testTransformWithMock() {
        // Создаем мок интерфейса
        TextTransformationService mockService = mock(TextTransformationService.class);

        // Настраиваем поведение мока
        when(mockService.transform("hello")).thenReturn("HELLO");

        // Вызываем метод
        String result = mockService.transform("hello");

        // Проверяем результат
        assertEquals("HELLO", result);

        // Проверяем, что метод был вызван с правильным аргументом
        verify(mockService).transform("hello");
    }


    /**
     * Стаб (spy) позволяет использовать реальную реализацию класса и подменять только определенные методы.
     * Метод transform возвращает "FAKE RESULT" для текста "hello", а для остальных случаев работает стандартная
     * логика.
     */
    @Test
    void testTransformWithSpy() {
        // Создаем объект-стаб на основе реальной реализации
        ToUpperCaseService spyService = Mockito.spy(new ToUpperCaseService());

        // Подменяем метод transform, чтобы он возвращал фиксированный результат
        doReturn("FAKE RESULT").when(spyService).transform("hello");

        // Вызываем метод
        String result = spyService.transform("hello");

        // Проверяем результат
        assertEquals("FAKE RESULT", result);

        // Проверяем, что метод transform был вызван с правильным аргументом
        verify(spyService).transform("hello");

        // Вызываем реальный метод для проверки его логики
        String realResult = spyService.transform("world");
        assertEquals("WORLD", realResult);
    }

}
