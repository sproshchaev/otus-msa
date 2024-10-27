package com.prosoft.msa.ui;

import com.prosoft.msa.ServiceA;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumTest {

    public static void main(String[] args) {

        // Укажите путь к драйверу Chrome
        System.setProperty("webdriver.chrome.driver", ServiceA.class.getClassLoader().getResource("chromedriver").getPath());
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080"); // Укажите URL вашего приложения

    }

}
