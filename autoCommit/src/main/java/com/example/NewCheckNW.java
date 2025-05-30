package com.example;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.time.Duration;

public class NewCheckNW {
    private WebDriver driver;

    public NewCheckNW(WebDriver driver) {
        this.driver = driver;
    }

    public void execute(WebDriver driver, boolean[] namespaceArray) throws InterruptedException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            Actions actions = new Actions(driver);

            // Массив соответствия индексов namespace и их значений
            String[] namespaceValues = {"devtcn", "cat", "uz", "kg"};

            for(int i = 0; i < namespaceArray.length && i < namespaceValues.length; i++) {
                if (namespaceArray[i]) {
                    System.out.println("Обрабатываем ветку: " + namespaceValues[i]);

                    // 1. Проверяем и выбираем namespace
                    WebElement namespaceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("namespace")));

                    // Проверяем, активен ли уже нужный namespace
                    if (!isNamespaceActive(namespaceElement, namespaceValues[i])) {
                        namespaceElement.click();
                        System.out.println("Namespace list opened");
                        Thread.sleep(400);

                        // Выбираем соответствующую ветку
                        String xpath = String.format("//a[@class='variable-option pointer']//span[text()='%s']", namespaceValues[i]);
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).click();
                        System.out.println("Check-box " + namespaceValues[i] + " selected");
                        actions.sendKeys(Keys.ENTER).perform();
                        Thread.sleep(400);
                    } else {
                        System.out.println("Namespace " + namespaceValues[i] + " уже активен - пропускаем выбор");
                    }

                    // 2. Выбираем pod
                    Thread.sleep(400);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pod"))).click();
                    System.out.println("Pod list opened");
                    Thread.sleep(400);

                    // 3. Выбираем new-widget
                    wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//a[@class='variable-option pointer']//span[text()='new-widget']"))
                    ).click();
                    System.out.println("Check-box new-widget selected");
                    actions.sendKeys(Keys.ENTER).perform();
                    Thread.sleep(400);

                    // 4. Проверяем текст
                    WebElement textInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[contains(text(), 'new-widget')]")));
                    String actualTextInput = textInputElement.getAttribute("textContent");

                    if (actualTextInput != null && actualTextInput.contains("new-widget")) {
                        try {
                            // 5. Делаем скриншот
                            String screenshotName = "resources/Screenshot_NW_" + namespaceValues[i] + ".png";
                            ScreenshotUtilUbuntu.takeScreenshotUbuntu(screenshotName);
                            System.out.println("Скриншот успешно сохранен: " + screenshotName);
                            Thread.sleep(400);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Ошибка при создании скриншота: " + e.getMessage());
                        }
                    } else {
                        String message = "Текстовое поле не содержит нужный текст. Актуальный текст: " +
                                (actualTextInput != null ? actualTextInput : "null");
                        JOptionPane.showMessageDialog(null, message);
                    }

                    // 6. Возвращаемся к исходному состоянию
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pod"))).click();
                    System.out.println("Pod list reopened");
                    Thread.sleep(400);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//a[@class='variable-option pointer']//span[text()='All']"))
                    ).click();
                    System.out.println("Check-box All selected");
                    actions.sendKeys(Keys.ENTER).perform();
                    Thread.sleep(400);
                }
            }
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Элемент не найден: " + e.getMessage());
        }
    }

    // Метод для проверки активности namespace
    private boolean isNamespaceActive(WebElement namespaceElement, String namespaceValue) {
        try {
            // Проверяем по текущему значению элемента или его атрибутам
            String currentValue = namespaceElement.getAttribute("value");
            String text = namespaceElement.getText();
            String classAttribute = namespaceElement.getAttribute("class");

            return currentValue != null && currentValue.contains(namespaceValue) ||
                    text != null && text.contains(namespaceValue) ||
                    classAttribute != null && classAttribute.contains("active");
        } catch (Exception e) {
            System.out.println("Ошибка при проверке активности namespace: " + e.getMessage());
            return false;
        }
    }
}