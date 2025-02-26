//package com.example;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//
//import java.util.concurrent.TimeUnit;
//
//public class sendImageToVk {
//public static void sendImageToVk(String login, String password, String chatUrl, String imagePath) {
//
//
//    WebDriver driver = new ChromeDriver();
//    try {
//        // Открываем страницу ВКонтакте
//        driver.get("https://vk.com/");
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//
//        // Вход в аккаунт
//        WebElement loginField = driver.findElement(By.id("index_email"));
//        WebElement passwordField = driver.findElement(By.id("index_pass"));
//        WebElement loginButton = driver.findElement(By.id("index_login_button"));
//
//        loginField.sendKeys(login);
//        passwordField.sendKeys(password);
//        loginButton.click();
//
//        // Переход к нужному чату
//        driver.get(chatUrl);
//
//        // Находим элемент для загрузки изображения
//        WebElement uploadButton = driver.findElement(By.xpath("//input[@type='file']"));
//        uploadButton.sendKeys(imagePath); // Укажите путь к изображению
//
//        // Отправка сообщения (если необходимо)
//        WebElement sendButton = driver.findElement(By.xpath("//button[contains(@class, 'send')]"));
//        sendButton.click();
//
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//    }
//}
//
