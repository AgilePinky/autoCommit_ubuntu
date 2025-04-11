package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Config {
    String jiraUrl;
    String username;
    String password;
    String imagePathRRS;
    String imagePathDRS;
    String imagePathFCS;
    String TOKEN;

    // Геттеры и сеттеры (если необходимо)
}

public class UrlOpener {

    public static Config loadConfig(String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Config config = null;

        try (FileReader reader = new FileReader(filePath)) {
            // Чтение конфигурации из файла
            config = gson.fromJson(reader, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    public static void main(String[] args) {
        // Загрузка конфигурации из файла
        Config config = loadConfig("config.json");

        if (config == null) {
            System.err.println("Не удалось загрузить конфигурацию.");
            return; // Завершаем выполнение, если конфигурация не загружена
        }

        String imagePathRRS = config.imagePathRRS;
        String imagePathDRS = config.imagePathDRS;
        String imagePathFCS = config.imagePathFCS;
        String JIRA_URL = config.jiraUrl; // Обратите внимание на изменение имени переменной
        String USERNAME = config.username; // Обратите внимание на изменение имени переменной
        String TOKEN = config.TOKEN; // Обратите внимание на изменение имени переменной

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("URL Opener");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 150);

            JTextField taskField = new JTextField(30); // TDF-10118

            JCheckBox checkRRSCommit = new JCheckBox("RRS");
            JCheckBox checkFCSCommit = new JCheckBox("FCS");
            JCheckBox checkDRSCommit = new JCheckBox("DRS");

            JButton openButton = new JButton("Сделать скриншот");
            JButton sendInJiraButtonNew = new JButton("Отправить комментарий");

            JPanel checkBoxPanel = new JPanel(new GridLayout(0, 4)); // 0 строк, 4 столбца
            checkBoxPanel.add(checkDRSCommit);
            checkBoxPanel.add(checkFCSCommit);
            checkBoxPanel.add(checkRRSCommit);

            openButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String url = ("https://grafana.devtcn.tech/d/1CiDdN4Sk/stage-image-list?orgId=1&var-namespace=devtcn&var-pod=All&var-container=All&viewPanel=4&var-Filters=image%7C%3D%7Cnexus.devtcn.tech%2Freact-renderer-service:ca-TDF-12474-cat-6b2d9cae");
                    try {
                        WebDriverManagerUtil.openWebpage(url, checkRRSCommit.isSelected(), checkDRSCommit.isSelected(), checkFCSCommit.isSelected());
                    } catch (AWTException ex) {
                        System.err.println("Ошибка AWT: " + ex.getMessage());
                    } catch (InterruptedException ex) {
                        System.err.println("Ошибка Interrupted: " + ex.getMessage());
                    } catch (Exception ex) {
                        System.err.println("Произошла ошибка: " + ex.getMessage());
                    }
                }
            });

            sendInJiraButtonNew.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String comment = "Актуальное состояние коммита";
                    String ISSUE_ID = taskField.getText();

                    // Создайте экземпляр класса JiraCommentWithImage
                    try{
                        JiraCommentWithImage.addCommentWithImages(JIRA_URL, USERNAME, TOKEN, ISSUE_ID, comment, imagePathRRS, checkRRSCommit.isSelected(),
                                imagePathDRS, checkDRSCommit.isSelected(), imagePathFCS, checkFCSCommit.isSelected());
                    } catch (Exception ex) {
                        System.err.println("Произошла ошибка: " + ex.getMessage());
                    }
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Центрируем кнопки
            buttonPanel.add(openButton);
            buttonPanel.add(sendInJiraButtonNew);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Вертикальная компоновка
            mainPanel.add(taskField);
            mainPanel.add(checkBoxPanel);
            mainPanel.add(buttonPanel);

            frame.getContentPane().add(mainPanel);
            frame.setVisible(true);
        });
    }
}