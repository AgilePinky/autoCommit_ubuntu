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

public class JiraCommentUploader {
    public static void sendInJira(String urlJira, String username, String password, String imagePath, String comment) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.manage().window().maximize();
            driver.get(urlJira);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Ввод логина
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys(username);
            driver.findElement(By.id("login-submit")).click();

            // Ввод пароля
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(password);
            driver.findElement(By.id("login-submit")).click();

            // Найдите элемент для загрузки файла
            WebElement uploadElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='file']")));
            uploadElement.sendKeys(imagePath);

            // Найдите текстовое поле для комментариев и добавьте текст
            WebElement commentField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("commentFieldId"))); // Замените на правильный ID
            commentField.sendKeys(comment);

            // Найдите кнопку для отправки комментария и нажмите на нее
            WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitButtonId"))); // Замените на правильный ID
            submitButton.click();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при открытии URL: " + urlJira);
        } finally {
            driver.quit();
        }
    }
}
//driver.findElement(By.id("loginField")).sendKeys("i.sharipov@lantan.info");
//driver.findElement(By.id("passwordField")).sendKeys("TretGall02");