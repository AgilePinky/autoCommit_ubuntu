package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.time.Duration;

public class WDMU2 {
    private static boolean screenshotTaken = false;

    public static void openWebpage(String url, boolean checkTextInput, boolean checkEmailField,
                                   boolean checkPasswordField, boolean checkRRSCommit,
                                   boolean checkFCSCommit) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        String login = "sharipovi2002@mail.ru";
        String password = "Pantelis7!!7";
        String chatUrl = "https://vk.com/im/convo/325568350?entrypoint=list_all";
        String imagePath = "Text_input.png";

        try {
            // Открытие веб-страницы
            driver.get(url);

            // Проверка заголовка
            // Заполнение текстового поля
            if (checkRRSCommit) {
//                i.sharipov  m6JHWgSANhrLbGkta8QUdn
                try {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='user']"))).sendKeys("i.sharipov");
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='current-password']"))).sendKeys("m6JHWgSANhrLbGkta8QUdn");
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Login button']"))).click();
                    System.out.println("Вход успешный");

                    WebElement textInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'data-routing-scheduler-688c4666d4-n4wg8')]")));
                    System.out.println("Элемент найден");

                    String actualTextInput = textInputElement.getText();
                    if (actualTextInput.equals("data-routing-scheduler-688c4666d4-n4wg8")) {
                        if (!screenshotTaken){
                            JOptionPane.showMessageDialog(null, "Совпадает RRS");
//                          ScreenshotUtil.takeScreenshot("RRS.png"); // Скриншот после заполнения поля
                            ScreenshotUtilUbuntu.takeScreenshotUbuntu("RRS.png");
                            screenshotTaken = true; // Устанавливаем флаг в true, чтобы предотвратить повторный скриншот
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Текстовое поле не совпадает: " + actualTextInput);
                    }
                } catch (NoSuchElementException e) {
                    JOptionPane.showMessageDialog(null, "Элемент 'RRS' не найден.");
                }
            }

            if (checkFCSCommit) {
//                i.sharipov  m6JHWgSANhrLbGkta8QUdn
                try {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='user']"))).sendKeys("i.sharipov");
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='current-password']"))).sendKeys("m6JHWgSANhrLbGkta8QUdn");
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Login button']"))).click();
                    System.out.println("Вход успешный");

                    //WebElement textInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'front-content-scheduler-66f8db4bfb-l86hm')]")));
                    WebElement textInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'analytics-micro-scheduler-77bb488657-8sxnh')]")));
                    System.out.println("Элемент найден");

                    String actualTextInput = textInputElement.getText();
                    //if (actualTextInput.equals("front-content-scheduler-66f8db4bfb-l86hm")) {
                    if (actualTextInput.equals("analytics-micro-scheduler-77bb488657-8sxnh")) {
                        if (!screenshotTaken){
                            JOptionPane.showMessageDialog(null, "Совпадает FCS");
//                          ScreenshotUtil.takeScreenshot("RRS.png"); // Скриншот после заполнения поля
                            ScreenshotUtilUbuntu.takeScreenshotUbuntu("FCS.png");
                            screenshotTaken = true; // Устанавливаем флаг в true, чтобы предотвратить повторный скриншот
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Текстовое поле не совпадает: " + actualTextInput);
                    }
                } catch (NoSuchElementException e) {
                    JOptionPane.showMessageDialog(null, "Элемент 'FCS' не найден.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при открытии URL: " + url);
        } finally {
            driver.quit();
        }
    }

}