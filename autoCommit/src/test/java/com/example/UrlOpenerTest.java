//package com.example;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.junit.jupiter.api.*;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class UrlOpenerTest {
//    private WebDriver driver;
//
//    @BeforeAll
//    public static void setupAll() {
//        WebDriverManager.chromedriver().setup();
//    }
//
//    @BeforeEach
//    public void setup() {
//        driver = new ChromeDriver();
//    }
//
//    @AfterEach
//    public void teardown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testOpenWebpageAndCheckTitle() throws IOException {
//        String url = "https://www.google.ru/"; // Замените на ваш URL
//        String expectedTitle = "Google"; // Замените на ваш ожидаемый заголовок
//
//        driver.get(url);
//        String actualTitle = driver.getTitle();
//
//        assertEquals(expectedTitle, actualTitle, "Page title does not match!");
//
//        // Сохранение скриншота, если заголовки совпадают
//        if (actualTitle.equals(expectedTitle)) {
//            takeScreenshot();
//        }
//    }
//
//    private void takeScreenshot() throws IOException {
//        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//        BufferedImage img = ImageIO.read(screenshot);
//        File outputfile = new File("screenshot1.png");
//        ImageIO.write(img, "png", outputfile);
//        System.out.println("Screenshot saved: " + outputfile.getAbsolutePath());
//    }
//}