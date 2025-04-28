package com.example;
import java.awt.*;
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
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyEvent;

public class WebDriverManagerUtil  {

    public static void openWebpage(String usernameGrafana, String passwordGrafana, String url,
                                   boolean checkRRSCommit, boolean checkDRSCommit,
                                   boolean checkFCSCommit, boolean checkNWCommit,
                                   boolean checkBoxMapNamespaceDEVTCN, boolean checkBoxMapNamespaceCAT,
                                   boolean checkBoxMapNamespaceUZ, boolean checkBoxMapNamespaceKG) throws AWTException, InterruptedException{
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        boolean[] namespaceArray = {checkBoxMapNamespaceDEVTCN, checkBoxMapNamespaceCAT, checkBoxMapNamespaceUZ, checkBoxMapNamespaceKG};


        try {
            // Открытие веб-страницы
            driver.get(url);

            // Делать окно полным экраном
            driver.manage().window().maximize(); // или driver.manage().window().fullscreen(); для полного экрана
            for(int i=0; i < 4; i ++){
                if(namespaceArray[0] == true){
                    // Выполнение входа в систему
                    if (performLogin(driver, usernameGrafana, passwordGrafana)) {
                        // Проверяем RRS
                        if (checkRRSCommit) {
                            NewCheckRRS checkRRS = new NewCheckRRS(driver, namespaceArray);
                            checkRRS.execute();
                        }
                        // Проверяем DRS
                        if (checkDRSCommit) {
                            NewCheckDRS checkDRS = new NewCheckDRS(driver, namespaceArray);
                            checkDRS.execute();
                        }
                        // Проверяем FCS
                        if (checkFCSCommit) {
                            NewCheckFCS checkFCS = new NewCheckFCS(driver, namespaceArray);
                            checkFCS.execute();
                        }
                        // Проверяем NW
                        if (checkNWCommit) {
                            NewCheckNW checkNW = new NewCheckNW();
                            checkNW.execute(driver, namespaceArray);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при открытии URL: " + url);
        } finally {
            driver.quit();
        }
    }

    private static boolean performLogin(WebDriver driver, String usernameGrafana, String passwordGrafana) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='user']"))).sendKeys(usernameGrafana);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='current-password']"))).sendKeys(passwordGrafana);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Login button']"))).click();
            System.out.println("Вход успешный");
            return true;
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при входе в систему.");
            return false;
        }
    }
}