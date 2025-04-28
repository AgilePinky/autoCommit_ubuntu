package com.example;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.time.Duration;

public class NewCheckNW {
    private WebDriver driver;

//    public NewCheckNW(WebDriver driver) {
//        this.driver = driver;
//    }

    public void execute(WebDriver driver, boolean[] namespaceArray) throws InterruptedException {
//        this.driver = driver;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement textInputElement = null;
            Thread.sleep(500);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pod"))).click();
            System.out.println("List found");
            Thread.sleep(250);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='variable-option pointer']//span[text()='new-widget']"))).click();
            System.out.println("Check-box new-widget found");
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ENTER).perform();

// 2. Получаем текст правильно (может потребоваться getAttribute)
            textInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'new-widget')]")));
            String actualTextInput = textInputElement.getAttribute("textContent"); // или .getText()

// 3. Улучшенная проверка с обработкой null
            if (actualTextInput != null && actualTextInput.contains("new-widget")) {
                // 4. Делаем скриншот с проверкой директории
                try {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'new-widget')]")));

                    for(int i = 0; i < namespaceArray.length; i++){
                        if(namespaceArray[0] == true)
                            ScreenshotUtilUbuntu.takeScreenshotUbuntu("resources/ScreenshotNWdevtcn.png");
                        if(namespaceArray[1] == true)
                            ScreenshotUtilUbuntu.takeScreenshotUbuntu("resources/ScreenshotNWcat.png");
                        if(namespaceArray[2] == true)
                            ScreenshotUtilUbuntu.takeScreenshotUbuntu("resources/ScreenshotNWuz.png");
                        if(namespaceArray[3] == true)
                            ScreenshotUtilUbuntu.takeScreenshotUbuntu("resources/ScreenshotNWkg.png");
                    }
//                    ScreenshotUtilUbuntu.takeScreenshotUbuntu("resources/ScreenshotNW.png");
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
            JOptionPane.showMessageDialog(null, "Элемент 'NW' не найден.");
        }
    }
}