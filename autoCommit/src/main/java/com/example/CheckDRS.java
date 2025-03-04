package com.example;
import java.lang.InterruptedException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;

import javax.swing.*;
import java.time.Duration;

public class CheckDRS {
    private WebDriver driver;

    public CheckDRS(WebDriver driver) {
        this.driver = driver;
    }

    public void execute() throws InterruptedException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement textInputElement = null;
            boolean elementFound = false;
            Thread.sleep(2000);

            System.out.println("Поиск элемента DRS");

            WebElement scrollableElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@style, 'overflow: hidden auto')]")));
            System.out.println("Найден на странице элемент списка логов");
            while (!elementFound) {
                try {
                    textInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'data-routing-scheduler-688c4666d4-n4wg8')]")));
                    elementFound = true;
                    System.out.println("Элемент DRS найден");

                } catch (TimeoutException e) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop += 800;", scrollableElement);
                    System.out.println("Скролл на 800px");
                    Thread.sleep(200);
                }
            }

            WebElement hoverElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(., 'nexus.devtcn.tech/data-routing-service')]")));
            System.out.println("Находим элемент, на который нужно навести курсор");

            Actions actions = new Actions(driver);
            actions.moveToElement(hoverElement).pause(Duration.ofMillis(100)).perform();
            System.out.println("Наводим курсор на элемент");

            WebElement button = (WebElement) js.executeScript("return arguments[0]",
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(), 'data-routing-scheduler-688c4666d4-n4wg8')]/../..//button[@aria-label='Filter for value']"))));
            System.out.println("Кнопка активна: " + button.isEnabled());
            js.executeScript("arguments[0].click();", button);
            System.out.println("Клик по кнопке");

            String actualTextInput = textInputElement.getText();
            if (actualTextInput.equals("data-routing-scheduler-688c4666d4-n4wg8")) {
                ScreenshotUtilUbuntu.takeScreenshotUbuntu("DRS.png");
            } else {
                JOptionPane.showMessageDialog(null, "Текстовое поле не совпадает: " + actualTextInput);
            }

            driver.navigate().refresh();
            System.out.println("Страница обновлена");

        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Элемент 'DRS' не найден.");
        }
    }
}