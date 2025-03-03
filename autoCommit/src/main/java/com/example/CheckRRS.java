package com.example;
import java.lang.InterruptedException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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

public class CheckRRS {
    private static boolean screenshotTaken = false;

    public static void openWebpage(String url, boolean checkRRSCommit) {
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

    private static void checkRRS(WebDriver driver) throws InterruptedException {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement textInputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'data-routing-scheduler-688c4666d4-n4wg8')]")));
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
            actions.moveToElement(hoverElement).pause(Duration.ofMillis(500)).perform();
            System.out.println("Наводим курсор на элемент");

            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement button = (WebElement) js.executeScript("return arguments[0]",
                    wait.until(ExpectedConditions.presenceOfElementLocated
                            (By.xpath("//div[contains(text(), 'data-routing-scheduler-688c4666d4-n4wg8')]/../..//button[@aria-label='Filter for value']"))));
            System.out.println("Кнопка активна: " + button.isEnabled());

            js.executeScript("arguments[0].click();", button);
            System.out.println("Клик по кнопке");

            String actualTextInput = textInputElement.getText();
            if (actualTextInput.equals("data-routing-scheduler-688c4666d4-n4wg8")) {
                if (!screenshotTaken) {
//                    JOptionPane.showMessageDialog(null, "Совпадает RRS");
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
}