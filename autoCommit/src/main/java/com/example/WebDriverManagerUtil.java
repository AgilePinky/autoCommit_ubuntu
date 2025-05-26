package com.example;
import java.awt.*;
import java.lang.InterruptedException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.time.Duration;

public class WebDriverManagerUtil  {

    public static void openWebpage(String usernameGrafana, String passwordGrafana, String url,
                                   boolean checkRRSCommit, boolean checkDRSCommit,
                                   boolean checkFCSCommit, boolean checkNWCommit,
                                   boolean checkBoxMapNamespaceCAT, boolean checkBoxMapNamespaceDEVTCN,
                                   boolean checkBoxMapNamespaceDEVUZ, boolean checkBoxMapNamespaceKG,
                                   boolean checkBoxMapNamespaceMINIAPP, boolean checkBoxMapNamespaceTJ,
                                   boolean checkBoxMapNamespaceUZ) throws AWTException, InterruptedException{
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        boolean[] namespaceArray = {checkBoxMapNamespaceCAT, checkBoxMapNamespaceDEVTCN, checkBoxMapNamespaceDEVUZ,
                checkBoxMapNamespaceKG, checkBoxMapNamespaceMINIAPP, checkBoxMapNamespaceTJ,
                checkBoxMapNamespaceUZ,};


        try {
            // Открытие веб-страницы
            driver.get(url);

            // Делать окно полным экраном
            driver.manage().window().maximize(); // или driver.manage().window().fullscreen(); для полного экрана

                if (performLogin(driver, usernameGrafana, passwordGrafana)) {
                    // Проверяем RRS - старый вариант, до проверок веток
//                    if (checkRRSCommit) {
//                        NewCheckRRS checkRRS = new NewCheckRRS(driver, namespaceArray);
//                        checkRRS.execute();
//                    }
                    // Проверяем RRS
                    if (checkRRSCommit) {
                        CheckRRS_2 checkRRS = new CheckRRS_2(driver);
                        checkRRS.execute(driver, namespaceArray);
                    }
                    // Проверяем DRS
                    if (checkDRSCommit) {
                        CheckDRS_2 checkDRS = new CheckDRS_2(driver);
                        checkDRS.execute(driver, namespaceArray);
                    }
                    // Проверяем FCS
                    if (checkFCSCommit) {
                        CheckFCS_2 checkFCS = new CheckFCS_2(driver);
                        checkFCS.execute(driver, namespaceArray);
                    }
                    // Проверяем NW
                    if (checkNWCommit) {
                        CheckNW_2 checkNW = new CheckNW_2(driver);
                        checkNW.execute(driver, namespaceArray);
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