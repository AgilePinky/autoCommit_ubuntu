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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

class Config {
    String jiraUrl;
    String usernameJira;
    String usernameGrafana;
    String passwordGrafana;
    String imagePathRRS;
    String imagePathDRS;
    String imagePathFCS;
    String imagePathNW;
    String TOKEN;

    // Геттеры и сеттеры (если необходимо)
}

public class UrlOpener {

public static Config loadConfig() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    try (InputStream is = UrlOpener.class.getResourceAsStream("/config.json");
         Reader reader = new InputStreamReader(is)) {
        return gson.fromJson(reader, Config.class);
    } catch (Exception e) {
        System.err.println("Ошибка загрузки конфигурации:");
        e.printStackTrace();
        return null;
    }
}

    public static void main(String[] args) {




        // Загрузка конфигурации из файла
        Config config = loadConfig();

        if (config == null) {
            System.err.println("Не удалось загрузить конфигурацию.");
            return; // Завершаем выполнение, если конфигурация не загружена
        }

        String imagePathRRS = config.imagePathRRS;
        String imagePathDRS = config.imagePathDRS;
        String imagePathFCS = config.imagePathFCS;
        String imagePathNW = config.imagePathNW;
        String JIRA_URL = config.jiraUrl; // Обратите внимание на изменение имени переменной
        String usernameJira = config.usernameJira; // Обратите внимание на изменение имени переменной
        String usernameGrafana = config.usernameGrafana; // Обратите внимание на изменение имени переменной
        String passwordGrafana = config.passwordGrafana; // Обратите внимание на изменение имени переменной
        String TOKEN = config.TOKEN; // Обратите внимание на изменение имени переменной

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("URL Opener");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);

            JTextField taskField = new JTextField();
            taskField.setPreferredSize(new Dimension(300, 30));

            // Создаем модель для выпадающего списка с чекбоксами
            String[] options = {"RRS", "FCS", "DRS", "NW"};
            JComboBox<String> comboBox = new JComboBox<>(options);

            // Создаем кнопку для отображения/скрытия списка
            JButton toggleButton = new JButton("Выбрать сервисы ▼");

            // Панель для чекбоксов, которая будет скрываться/показываться
            JPanel checkBoxPanel = new JPanel(new GridLayout(0, 1));
            checkBoxPanel.setVisible(false);

            // Создаем чекбоксы и добавляем их на панель
            Map<String, JCheckBox> checkBoxMap = new HashMap<>();
            for (String option : options) {
                JCheckBox checkBox = new JCheckBox(option);
                checkBoxMap.put(option, checkBox);
                checkBoxPanel.add(checkBox);
            }

            // Обработчик для кнопки переключения
            toggleButton.addActionListener(e -> {
                checkBoxPanel.setVisible(!checkBoxPanel.isVisible());
                toggleButton.setText(checkBoxPanel.isVisible() ?
                        "Выбрать сервисы ▲" : "Выбрать сервисы ▼");
                frame.pack();
            });

            // Создаем модель для выпадающего списка с чекбоксами
            String[] namespaces = {"devtcn", "cat", "uz", "kg"};
            JComboBox<String> comboBoxNamespace = new JComboBox<>(options);

            // Создаем кнопку для отображения/скрытия списка
            JButton toggleButtonNamespace = new JButton("Выбрать ветки ▼");

            // Панель для чекбоксов, которая будет скрываться/показываться
            JPanel checkBoxPanelNamespace = new JPanel(new GridLayout(0, 1));
            checkBoxPanelNamespace.setVisible(false);

            // Создаем чекбоксы и добавляем их на панель
            Map<String, JCheckBox> checkBoxMapNamespace = new HashMap<>();
            for (String namespace : namespaces) {
                JCheckBox checkBoxNamespace = new JCheckBox(namespace);
                checkBoxMap.put(namespace, checkBoxNamespace);
                checkBoxPanelNamespace.add(checkBoxNamespace);
            }

            // Обработчик для кнопки переключения
            toggleButtonNamespace.addActionListener(e -> {
                checkBoxPanelNamespace.setVisible(!checkBoxPanelNamespace.isVisible());
                toggleButtonNamespace.setText(checkBoxPanelNamespace.isVisible() ?
                        "Выбрать ветки ▲" : "Выбрать ветки ▼");
                frame.pack();
            });

            JButton openButton = new JButton("Сделать скриншот");
            JButton sendInJiraButtonNew = new JButton("Отправить комментарий");

            openButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String url = ("https://grafana.devtcn.tech/d/1CiDdN4Sk/stage-image-list?orgId=1&var-namespace=devtcn&var-pod=All&var-container=All&viewPanel=4&var-Filters=image%7C%3D%7Cnexus.devtcn.tech%2Freact-renderer-service:ca-TDF-12474-cat-6b2d9cae");
                    try {
                        WebDriverManagerUtil.openWebpage(usernameGrafana, passwordGrafana, url,
                                checkBoxMap.get("RRS").isSelected(),
                                checkBoxMap.get("DRS").isSelected(),
                                checkBoxMap.get("FCS").isSelected(),
                                checkBoxMap.get("NW").isSelected(),
                                checkBoxMapNamespace.get("devtcn").isSelected(),
                                checkBoxMapNamespace.get("cat").isSelected(),
                                checkBoxMapNamespace.get("uz").isSelected(),
                                checkBoxMapNamespace.get("kg").isSelected());
                    } catch (Exception ex) {
                        System.err.println("Произошла ошибка: " + ex.getMessage());
                    }
                }
            });

            sendInJiraButtonNew.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String comment = "Актуальное состояние коммитов";
                    String ISSUE_ID = taskField.getText();

                    try {
                        JiraCommentWithImage.addCommentWithImages(JIRA_URL, usernameJira, TOKEN, ISSUE_ID, comment,
                                imagePathRRS, checkBoxMap.get("RRS").isSelected(),
                                imagePathDRS, checkBoxMap.get("DRS").isSelected(),
                                imagePathFCS, checkBoxMap.get("FCS").isSelected(),
                                imagePathNW, checkBoxMap.get("NW").isSelected());
                    } catch (Exception ex) {
                        System.err.println("Произошла ошибка: " + ex.getMessage());
                    }
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(openButton);
            buttonPanel.add(sendInJiraButtonNew);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            // Добавляем текстовое поле с выравниванием
            mainPanel.add(Box.createVerticalStrut(5)); // Небольшой отступ сверху

            mainPanel.add(taskField);
            mainPanel.add(Box.createVerticalStrut(20));
            mainPanel.add(toggleButtonNamespace);
            mainPanel.add(checkBoxPanelNamespace);
            mainPanel.add(Box.createVerticalStrut(20)); // Небольшой отступ снизу
            mainPanel.add(toggleButton);
            mainPanel.add(checkBoxPanel);
            mainPanel.add(Box.createVerticalStrut(20));
            mainPanel.add(buttonPanel);

            frame.getContentPane().add(mainPanel);
            frame.pack();
            frame.setVisible(true);

            frame.getContentPane().add(mainPanel);
            frame.setVisible(true);
        });
    }
}