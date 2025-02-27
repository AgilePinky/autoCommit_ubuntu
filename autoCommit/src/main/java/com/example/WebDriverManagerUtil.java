package com.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

import javax.swing.*;
import java.time.Duration;

public class WebDriverManagerUtil {
    private static boolean screenshotTaken = false;

    public static void openWebpage(String url, boolean checkTextInput, boolean checkEmailField,
                                   boolean checkPasswordField, boolean checkRRSCommit,
                                   boolean checkFCSCommit) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            // Открытие веб-страницы
            driver.get(url);

            // Делать окно полным экраном
            driver.manage().window().maximize(); // или driver.manage().window().fullscreen(); для полного экрана

            // Выполнение входа в систему
            if (performLogin(driver)) {
                // Проверяем RRS
                if (checkRRSCommit) {
                    checkRRS(driver);
                }
                // Проверяем FCS
                if (checkFCSCommit) {
                    checkFCS(driver);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при открытии URL: " + url);
        } finally {
            driver.quit();
        }
    }

    private static boolean performLogin(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='user']"))).sendKeys("i.sharipov");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='current-password']"))).sendKeys("m6JHWgSANhrLbGkta8QUdn");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Login button']"))).click();
            System.out.println("Вход успешный");
            return true;
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при входе в систему.");
            return false;
        }
    }

    private static void checkRRS(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement textInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'data-routing-service-7599dbf988-z4bwb')]")));
            System.out.println("Элемент RRS найден");
            screenshotTaken = false;
            // Находим элемент, на который нужно навести курсор
            WebElement hoverElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(., 'nexus.devtcn.tech/data-routing-service')]")
            ));
            System.out.println("Находим элемент, на который нужно навести курсор");

            // Создаем объект Actions для выполнения действий
            Actions actions = new Actions(driver);

            // Наводим курсор на элемент
            actions.moveToElement(hoverElement).perform();
            System.out.println("Наводим курсор на элемент");

            // Ожидаем, пока кнопка станет доступной
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Filter for value']")));
            System.out.println("Ожидаем, пока кнопка станет доступной");

            // Кликаем на кнопку
            button.click();
            System.out.println("Кликаем на кнопку");


            String actualTextInput = textInputElement.getText();
            if (actualTextInput.equals("data-routing-service-7599dbf988-z4bwb")) {
                if (!screenshotTaken) {
                    JOptionPane.showMessageDialog(null, "Совпадает RRS");
                    ScreenshotUtilUbuntu.takeScreenshotUbuntu("RRS.png");
                    screenshotTaken = true; // Устанавливаем флаг в true
                }
            } else {
                JOptionPane.showMessageDialog(null, "Текстовое поле не совпадает: " + actualTextInput);
            }
            // Обновляем страницу после создания скриншота
            driver.navigate().refresh();
            System.out.println("Страница обновлена");

        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Элемент 'RRS' не найден.");
        }
    }

    private static void checkFCS(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement textInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'front-content-5cd994856b-rs6ng')]")));
            System.out.println("Элемент FCS найден");
            screenshotTaken = false;

            // Находим элемент, на который нужно навести курсор
            WebElement hoverElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(., 'nexus.devtcn.tech/front-content-service')]")
            ));
            System.out.println("Находим элемент, на который нужно навести курсор");

            // Создаем объект Actions для выполнения действий
            Actions actions = new Actions(driver);

            // Наводим курсор на элемент
            actions.moveToElement(hoverElement).perform();
            System.out.println("Наводим курсор на элемент");

            // Ожидаем, пока кнопка станет доступной
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Filter for value']")));
            System.out.println("Ожидаем, пока кнопка станет доступной");

            // Кликаем на кнопку
            button.click();
            System.out.println("Кликаем на кнопку");

            String actualTextInput = textInputElement.getText();
            if (actualTextInput.equals("front-content-5cd994856b-rs6ng")) {
                if (!screenshotTaken) {
                    JOptionPane.showMessageDialog(null, "Совпадает FCS");
                    ScreenshotUtilUbuntu.takeScreenshotUbuntu("FCS.png");
                    screenshotTaken = true; // Устанавливаем флаг в true
                }
            } else {
                JOptionPane.showMessageDialog(null, "Текстовое поле не совпадает: " + actualTextInput);
            }
            // Обновляем страницу после создания скриншота
            driver.navigate().refresh();
            System.out.println("Страница обновлена");

        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Элемент 'FCS' не найден.");
        }
    }
}