package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public static final String[] IMAGE_LABELS = {"RRS", "DRS", "FCS", "NW"};
    public static final String[] IMAGE_BRANCHES = {"cat", "devtcn", "devuz", "kg", "miniapp", "tj", "uz"};
    public static final String IMAGE_TEMPLATE = "\\n branch:%s, service:%s \\n!%s|width=800,height=450!";

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
        String JIRA_URL = config.jiraUrl;
        String usernameJira = config.usernameJira;
        String usernameGrafana = config.usernameGrafana;
        String passwordGrafana = config.passwordGrafana;
        String TOKEN = config.TOKEN;

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("URL Opener");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            // Создаем панель для поля ввода с заголовком
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

            // Добавляем заголовок
            JLabel inputLabel = new JLabel("Введите идентификатор и номер задачи:");
            inputLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            inputPanel.add(inputLabel);

            // Добавляем поле ввода
            JTextField taskField = new JTextField();
            taskField.setPreferredSize(new Dimension(300, 30));
            taskField.setMaximumSize(new Dimension(300, 30));
            taskField.setAlignmentX(Component.CENTER_ALIGNMENT);
            inputPanel.add(taskField);

            // Создаем панель для двух колонок
            JPanel columnsPanel = new JPanel(new GridLayout(1, 2, 10, 0));

            // Колонка веток
            JPanel branchesPanel = new JPanel();
            branchesPanel.setLayout(new BoxLayout(branchesPanel, BoxLayout.Y_AXIS));
            branchesPanel.setBorder(BorderFactory.createTitledBorder("Ветки"));

            // Чекбоксы веток
            Map<String, JCheckBox> checkBoxMapNamespace = new HashMap<>();
            for (String branch : IMAGE_BRANCHES) {
                JCheckBox checkBox = new JCheckBox(branch);
                checkBoxMapNamespace.put(branch, checkBox);
                branchesPanel.add(checkBox);
                branchesPanel.add(Box.createVerticalStrut(5));
            }

            // Колонка сервисов
            JPanel servicesPanel = new JPanel();
            servicesPanel.setLayout(new BoxLayout(servicesPanel, BoxLayout.Y_AXIS));
            servicesPanel.setBorder(BorderFactory.createTitledBorder("Сервисы"));

            // Чекбоксы сервисов
            Map<String, JCheckBox> checkBoxMap = new HashMap<>();
            for (String service : IMAGE_LABELS) {
                JCheckBox checkBox = new JCheckBox(service);
                checkBoxMap.put(service, checkBox);
                servicesPanel.add(checkBox);
                servicesPanel.add(Box.createVerticalStrut(5));
            }

            // Добавляем колонки на панель
            columnsPanel.add(branchesPanel);
            columnsPanel.add(servicesPanel);

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
                                checkBoxMapNamespace.get("cat").isSelected(),
                                checkBoxMapNamespace.get("devtcn").isSelected(),
                                checkBoxMapNamespace.get("devuz").isSelected(),
                                checkBoxMapNamespace.get("kg").isSelected(),
                                checkBoxMapNamespace.get("miniapp").isSelected(),
                                checkBoxMapNamespace.get("tj").isSelected(),
                                checkBoxMapNamespace.get("uz").isSelected());
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
                    boolean[] servicesArray = {
                            checkBoxMap.get("RRS").isSelected(),
                            checkBoxMap.get("DRS").isSelected(),
                            checkBoxMap.get("FCS").isSelected(),
                            checkBoxMap.get("NW").isSelected()
                    };
                    boolean[] branchesArray = {
                            checkBoxMapNamespace.get("cat").isSelected(),
                            checkBoxMapNamespace.get("devtcn").isSelected(),
                            checkBoxMapNamespace.get("devuz").isSelected(),
                            checkBoxMapNamespace.get("kg").isSelected(),
                            checkBoxMapNamespace.get("miniapp").isSelected(),
                            checkBoxMapNamespace.get("tj").isSelected(),
                            checkBoxMapNamespace.get("uz").isSelected()
                    };
                    String[] imagePathArray = {imagePathRRS, imagePathDRS, imagePathFCS, imagePathNW};

                    try {
                        JiraCommentWithImage.addCommentWithImages(JIRA_URL, usernameJira, TOKEN, ISSUE_ID, comment,
                                servicesArray, branchesArray, imagePathArray, IMAGE_LABELS, IMAGE_TEMPLATE, IMAGE_BRANCHES);
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
            mainPanel.add(Box.createVerticalStrut(5));
            mainPanel.add(inputPanel); // Добавляем панель с заголовком и полем ввода
            mainPanel.add(Box.createVerticalStrut(20));
            mainPanel.add(columnsPanel);
            mainPanel.add(Box.createVerticalStrut(20));
            mainPanel.add(buttonPanel);

            frame.getContentPane().add(mainPanel);
            frame.pack();
            frame.setVisible(true);
        });
    }
}