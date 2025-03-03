package com.example;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class SaveCookies {
    public static void saveCookies () throws IOException {
        // Настройка WebDriverManager для Chrome
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        // Переход на страницу входа
        driver.get("https://example.com/login");

        // Введите свои учетные данные и выполните вход
        // Например, используйте driver.findElement(...) для ввода логина и пароля

        // Сохраните куки
        Set<Cookie> cookies = driver.manage().getCookies();
        try (FileWriter writer = new FileWriter("cookies.txt")) {
            for (Cookie cookie : cookies) {
                writer.write(cookie.getName() + ";" + cookie.getValue() + "\n");
            }
        }

        driver.quit();
    }
}