package com.example;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ScreenshotUtil {
    public static void takeScreenshot(String fileName) {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenCapture = robot.createScreenCapture(screenRect);

            File screenshotFile = new File(fileName);

            ImageIO.write(screenCapture, "png", screenshotFile);
            System.out.println("Скриншот сохранен: " + screenshotFile.getAbsolutePath());
        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        }
    }
}