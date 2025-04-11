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

public class NewCheckDRS {
    private WebDriver driver;

    public NewCheckDRS(WebDriver driver) {
        this.driver = driver;
    }

    public void execute() throws InterruptedException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofMillis(250));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement textInputElement = null;
            boolean elementFound = false;
            Thread.sleep(2000);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pod"))).click();
            System.out.println("List found");
            Thread.sleep(2000);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[role='checkbox' and contains(., 'All')]"))).click();
            System.out.println("Check-box All found");

            String actualTextInput = textInputElement.getText();
            if (actualTextInput.contains("data-routing-scheduler")) {
                ScreenshotUtilUbuntu.takeScreenshotUbuntu("resources/ScreenshotDRS.png");
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