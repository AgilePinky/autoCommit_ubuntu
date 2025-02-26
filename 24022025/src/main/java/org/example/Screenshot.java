package org.example;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.JOptionPane;

public class Screenshot {
    public static void main(String[] args) {
        System.out.println("Запуск программы...");

        try {
            // Получаем доступные экраны
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            var screens = ge.getScreenDevices();

            // Создаем массив для выбора экрана
            String[] screenNames = new String[screens.length];
            for (int i = 0; i < screens.length; i++) {
                screenNames[i] = "Экран " + (i + 1);
            }

            // Позволяем пользователю выбрать экран
            int screenIndex = JOptionPane.showOptionDialog(null, "Выберите экран для скриншота:",
                    "Выбор экрана", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, screenNames, screenNames[0]);

            if (screenIndex < 0) {
                System.out.println("Выбор экрана отменен.");
                return;
            }

            // Получаем размер выбранного экрана
            Rectangle screenRect = screens[screenIndex].getDefaultConfiguration().getBounds();
            System.out.println("Получаем размер экрана: " + screenRect);

            // Создаем объект Robot
            Robot robot = new Robot();
            System.out.println("Создаем объект Robot");

            // Делаем скриншот
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            System.out.println("Делаем скриншот");

            // Сохраняем скриншот в файл
            File outputFile = new File("screenshot.png");
            ImageIO.write(screenFullImage, "png", outputFile);
            System.out.println("Скриншот сохранен: " + outputFile.getAbsolutePath());
        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        }

        System.out.println("Завершение программы.");
    }
}