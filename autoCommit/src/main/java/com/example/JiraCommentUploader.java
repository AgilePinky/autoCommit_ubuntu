package com.example;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import java.io.FileWriter;
import java.time.Duration;
import java.util.Set;import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class JiraCommentUploader {
    public static void sendInJira(String urlJira, String username, String password, String imagePath, String comment) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.manage().window().maximize();
            driver.get(urlJira);


            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Ввод логина и пароля
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys(username);
            driver.findElement(By.id("login-submit")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))).sendKeys(password);
            driver.findElement(By.id("login-submit")).click();


            // Найдите текстовое поле для комментариев и добавьте текст
            WebElement commentField = wait.until(ExpectedConditions.visibilityOfElementLocated
                    (By.xpath("//button[text()='Добавить комментарий...']"))); // Замените на правильный ID
            System.out.println("Найдено поле коммента\n");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", commentField);
            System.out.println("Кликнуто на поле коммента\n");
            WebElement commentFieldOpened = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ak-editor-textarea")));
            commentFieldOpened.sendKeys(comment);
            System.out.println("Оставлен коммент текстовый в поле коммента\n");

            // Найдите элемент для загрузки файла и кликните по нему
            WebElement uploadElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[aria-label='Добавить изображение, видео или файл']")));
            uploadElement.click();

            System.out.println("Элемент загрузки картинок найден и кликнут\n");

            Robot robot = new Robot();
            Thread.sleep(500); // Даем время, чтобы открылось окно выбора файла

            String targetFolder = "Изображения";
            boolean found = false;

// Предположим, что папка "Изображения" появится не раньше, чем после нескольких нажатий Tab
            for (int i = 0; i < 20; i++) { // нужно протестировать, чтобы точно узнать, сколько нажатий необходимо
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
                Thread.sleep(200); // Небольшая пауза между нажатиями

                // Поскольку мы не можем проверить отображаемый текст, мы просто будем программировать на удачу
                // Предположим, что после 5-10 нажатий будет папка "Изображения"
                if (i == 7) { // Замените 5 на нужное вам количество для нахождения папки "Изображения"
                    StringSelection stringSelection = new StringSelection(targetFolder);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

                    // Нажмите Enter, чтобы перейти в папку "Изображения"
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                    Thread.sleep(1000); // Даем время для загрузки содержимого папки
                    found = true; // Установим флаг, что папка найдена
                    break; // Выходим из цикла, так как мы уже перешли в нужную папку
                }
            }

            // Если папка не была найдена после 20 итераций
            if (!found) {
                System.out.println("Папка \"" + targetFolder + "\" не найдена");
            }

            // Вставьте имя файла, который вы хотите выбрать
            for (int i = 0; i < 20; i++) { // нужно протестировать, чтобы точно узнать, сколько нажатий необходимо
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
                Thread.sleep(200); // Небольшая пауза между нажатиями

                // Поскольку мы не можем проверить отображаемый текст, мы просто будем программировать на удачу
                // Предположим, что после 5-10 нажатий будет папка "Изображения"
                if (i == 4) { // Замените 5 на нужное вам количество для нахождения папки "Изображения"
                    StringSelection stringSelection = new StringSelection("1a_DRS.png"); // Замените на имя вашего файла
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

                    robot.keyPress(KeyEvent.VK_UP);
                    robot.keyRelease(KeyEvent.VK_UP);
                    robot.keyPress(KeyEvent.VK_DOWN);
                    robot.keyRelease(KeyEvent.VK_DOWN);

                    // Подтвердите выбор файла
                    robot.keyPress(KeyEvent.VK_ENTER);
                    robot.keyRelease(KeyEvent.VK_ENTER);
                    Thread.sleep(1000); // Даем время для загрузки содержимого папки
                    found = true; // Установим флаг, что папка найдена
                    break; // Выходим из цикла, так как мы уже перешли в нужную папку
                }
            }

            // После выбора файла нажмите кнопку для отправки комментария
            WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitButtonId"))); // Замените на правильный ID
            submitButton.click();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ошибка при открытии URL: " + urlJira);
        } finally {
            driver.quit();
        }
    }

    private static void loadCookies(WebDriver driver, String cookiesFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(cookiesFilePath))) {
            String line;
            Map<String, String> cookieMap = new HashMap<>();

            while ((line = br.readLine()) != null) {
                String[] cookieParts = line.split(";");
                if (cookieParts.length > 1) {
                    String[] keyValue = cookieParts[0].split("=");
                    if (keyValue.length == 2) {
                        cookieMap.put(keyValue[0].trim(), keyValue[1].trim());
                    }
                }
            }

            // Установка куков в драйвер
            for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                Cookie cookie = new Cookie(entry.getKey(), entry.getValue());
                driver.manage().addCookie(cookie);
            }
            System.out.println("Куки успешно загружены.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось загрузить куки из файла: " + cookiesFilePath);
        }
    }
}
//driver.findElement(By.id("loginField")).sendKeys("i.sharipov@lantan.info");
//driver.findElement(By.id("passwordField")).sendKeys("TretGall02");