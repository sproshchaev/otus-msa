package com.prosoft.msa.ui;

import com.prosoft.msa.ServiceA;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Selenium UI-test
 * <p>
 * 1. Перед запуском теста запустить ServiceA, ServiceB, ServiceC.
 * 2. Установить зависимости для библиотеки selenium-java (dependency Maven/Gradle)
 * https://www.selenium.dev/documentation/webdriver/getting_started/install_library/
 * 3. Скачать в папку проекта resources архив с драйвером (Binary = chromedriver,
 * Platform = win64/mac-arm64 для M1/mac-x64 для Intel) https://googlechromelabs.github.io/chrome-for-testing/
 * <p>
 * Примечание:
 * 1) Если в MacOS возникает ошибка: "Exception in thread "main" org.openqa.selenium.SessionNotCreatedException:
 * Could not start a new session. Possible causes are invalid address of the remote server or browser start-up failure".
 * То перейти в настройки Конфиденциальность и безопасность и нажать "Все равно разрешить" для "Приложение chromedriver
 * заблокировано, так как его автор не является установленным разработчиком".
 * 2) JUnit пропустит классы с @Disabled при запуске тестов.
 */
@Disabled
class SeleniumTest {

    private static final String TEXT_FIELD_NAME = "text";
    private static final String ATTRIBUTE_VALUE = "value";
    private static final String INPUT_TEXT = "Hello World";
    private static final String EXPECTED_UPPERCASE_RESULT = "HELLO WORLD";
    private static final String EXPECTED_LOWERCASE_RESULT = "hello world";

    private WebDriver driver = new ChromeDriver();

    @BeforeEach
    void setUp() {
        String osName = System.getProperty("os.name").toLowerCase();
        String pathChromeDriver;
        if (osName.contains("mac")) {
            pathChromeDriver = ServiceA.class.getClassLoader().getResource("chromedriver/macos/chromedriver").getPath();
        } else {
            pathChromeDriver = ServiceA.class.getClassLoader().getResource("chromedriver/win/chromedriver").getPath();
        }
        System.setProperty("webdriver.chrome.driver", pathChromeDriver);
        driver.get("http://localhost:8080");
    }

    @Test
    void testToUpperCase() {
        WebElement inputField = driver.findElement(By.name(TEXT_FIELD_NAME));
        WebElement upperCaseButton = driver.findElement(By.cssSelector("button[formaction='/toUpperCase']"));
        // Ввод текста и нажатие кнопки
        inputField.sendKeys(INPUT_TEXT);
        upperCaseButton.click();
        // Ожидание и проверка результата
        WebElement result = driver.findElement(By.name(TEXT_FIELD_NAME));
        String resultText = result.getAttribute(ATTRIBUTE_VALUE);
        assertEquals(EXPECTED_UPPERCASE_RESULT, resultText);
    }

    @Test
    void testToLowerCase() {
        WebElement inputField = driver.findElement(By.name(TEXT_FIELD_NAME));
        WebElement upperCaseButton = driver.findElement(By.cssSelector("button[formaction='/toLowerCase']"));
        // Ввод текста и нажатие кнопки
        inputField.sendKeys(INPUT_TEXT);
        upperCaseButton.click();
        // Ожидание и проверка результата
        WebElement result = driver.findElement(By.name(TEXT_FIELD_NAME));
        String resultText = result.getAttribute(ATTRIBUTE_VALUE);
        assertEquals(EXPECTED_LOWERCASE_RESULT, resultText);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

}
