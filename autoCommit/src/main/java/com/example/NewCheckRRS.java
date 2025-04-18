package com.example;
import java.lang.InterruptedException;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

import javax.swing.*;
import java.time.Duration;

public class NewCheckRRS {
    private WebDriver driver;

    public NewCheckRRS(WebDriver driver) {
        this.driver = driver;
    }

    public void execute() throws InterruptedException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofMillis(250));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement textInputElement = null;
            boolean elementFound = false;
            Thread.sleep(500);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pod"))).click();
            System.out.println("List found");
            Thread.sleep(250);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='variable-option pointer']//span[text()='react-renderer-service']"))).click();
            System.out.println("Check-box react-renderer-service found");
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ENTER).perform();

// 2. Получаем текст правильно (может потребоваться getAttribute)
            textInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'react-renderer-service')]")));
            String actualTextInput = textInputElement.getAttribute("textContent"); // или .getText()

// 3. Улучшенная проверка с обработкой null
            if (actualTextInput != null && actualTextInput.contains("react-renderer-service")) {
                // 4. Делаем скриншот с проверкой директории
                try {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'react-renderer-service')]")));
                    ScreenshotUtilUbuntu.takeScreenshotUbuntu("resources/ScreenshotRRS.png");
                    System.out.println("Скриншот успешно сохранен");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ошибка при создании скриншота: " + e.getMessage());
                }
            } else {
                String message = "Текстовое поле не содержит нужный текст. Актуальный текст: " +
                        (actualTextInput != null ? actualTextInput : "null");
                JOptionPane.showMessageDialog(null, message);
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pod"))).click();
            System.out.println("List found");
            Thread.sleep(250);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='variable-option pointer']//span[text()='All']"))).click();
            System.out.println("Check-box All found");
            actions.sendKeys(Keys.ENTER).perform();
            Thread.sleep(250);

        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Элемент 'RRS' не найден.");
        }
    }
}