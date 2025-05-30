package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Класс конфигурации для хранения параметров из JSON
 */
class Config {
    // Параметры подключения к Jira
    String jiraUrl;
    String usernameJira;
    String TOKEN;

    // Параметры подключения к Grafana
    String usernameGrafana;
    String passwordGrafana;

    // Пути к изображениям для разных сервисов
    String imagePathRRS;
    String imagePathDRS;
    String imagePathFCS;
    String imagePathNW;
}

/**
 * Главный класс приложения для открытия URL и работы с Jira/Grafana
 */
public class UrlOpener {
    // Константы для меток сервисов и веток
    public static final String[] IMAGE_LABELS = {"RRS", "DRS", "FCS", "NW"};
    public static final String[] IMAGE_BRANCHES = {"cat", "devtcn", "devuz", "kg", "miniapp", "tj", "uz"};
    public static final String IMAGE_TEMPLATE = "\\n branch:%s, service:%s \\n!%s|width=800,height=450!";

    /**
     * Загрузка конфигурации из файла config.json
     * @return объект Config с параметрами
     */
    public static Config loadConfig() {
        // Инициализация парсера JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (InputStream is = UrlOpener.class.getResourceAsStream("/config.json");
             Reader reader = new InputStreamReader(is)) {
            // Чтение и парсинг JSON
            return gson.fromJson(reader, Config.class);
        } catch (Exception e) {
            System.err.println("Ошибка загрузки конфигурации:");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Создание группы чекбоксов
     * @param items массив названий для чекбоксов
     * @return Map с чекбоксами
     */
    private static Map<String, JCheckBox> createCheckboxes(String[] items) {
        Map<String, JCheckBox> map = new HashMap<>();
        for (String item : items) {
            JCheckBox checkBox = new JCheckBox(item);
            checkBox.setOpaque(false); // Прозрачный фон
            map.put(item, checkBox);
        }
        return map;
    }

    /**
     * Создание стилизованной кнопки
     * @param text текст кнопки
     * @param color цвет фона
     * @return готовая кнопка
     */
    private static JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false); // Убираем рамку фокуса
        button.setFont(button.getFont().deriveFont(Font.BOLD, 14));
        button.setPreferredSize(new Dimension(220, 40));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    /**
     * Создание панели с закругленными углами и стилизованной рамкой
     * @param title заголовок панели
     * @return готовая панель
     */
    private static JPanel createStyledPanel(String title) {
        // Кастомная панель с закругленным фоном
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                // Включение сглаживания для закругленных углов
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Заливка фона панели
                g2.setColor(new Color(0x97E1D0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false); // Прозрачность для отображения фона

        // Создание стилизованной рамки с заголовком
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0x45C7AA), 1, true), // Закругленная рамка
                title,
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(0x45C7AA)); // Цвет заголовка

        panel.setBorder(border);
        return panel;
    }

    /**
     * Точка входа в приложение
     */
    public static void main(String[] args) {
        // 1. Загрузка конфигурации
        Config config = loadConfig();
        if (config == null) {
            System.err.println("Не удалось загрузить конфигурацию.");
            return;
        }

        // 2. Создание GUI в потоке обработки событий
        SwingUtilities.invokeLater(() -> {
            // 2.1. Настройка внешнего вида (Look and Feel)
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
                // Настройка закругления элементов
                UIManager.put("Button.arc", 8);
                UIManager.put("Component.arc", 8);
                UIManager.put("TextComponent.arc", 5);
            } catch (Exception ex) {
                System.err.println("Failed to initialize LaF");
            }

            // 2.2. Создание главного окна
            JFrame frame = new JFrame("URL Opener");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // 2.3. Создание основной панели с кастомным фоном
            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(new Color(0xEDFAF8)); // Основной цвет фона
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // 2.4. Панель ввода задачи
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
            inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            inputPanel.setOpaque(false);

            // Заголовок поля ввода
            JLabel inputLabel = new JLabel("Введите идентификатор и номер задачи:");
            inputLabel.setFont(inputLabel.getFont().deriveFont(Font.BOLD, 14));
            inputLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            inputLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            inputPanel.add(inputLabel);

            // Поле ввода задачи
            JTextField taskField = new JTextField();
            taskField.setPreferredSize(new Dimension(400, 35));
            taskField.setMaximumSize(new Dimension(400, 35));
            taskField.setAlignmentX(Component.CENTER_ALIGNMENT);
            taskField.setFont(taskField.getFont().deriveFont(14f));
            taskField.setBackground(Color.WHITE);
            inputPanel.add(taskField);

            // 2.5. Панель с колонками (ветки и сервисы)
            JPanel columnsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
            columnsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            columnsPanel.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
            columnsPanel.setOpaque(false);

            // Создание чекбоксов для веток и сервисов
            Map<String, JCheckBox> checkBoxMapNamespace = createCheckboxes(IMAGE_BRANCHES);
            Map<String, JCheckBox> checkBoxMap = createCheckboxes(IMAGE_LABELS);

            // 2.6. Панель веток
            JPanel branchesPanel = createStyledPanel("Ветки");
            for (JCheckBox cb : checkBoxMapNamespace.values()) {
                cb.setFont(cb.getFont().deriveFont(Font.PLAIN, 13));
                branchesPanel.add(cb);
                branchesPanel.add(Box.createVerticalStrut(7)); // Отступ между элементами
            }

            // 2.7. Панель сервисов
            JPanel servicesPanel = createStyledPanel("Сервисы");
            for (JCheckBox cb : checkBoxMap.values()) {
                cb.setFont(cb.getFont().deriveFont(Font.PLAIN, 13));
                servicesPanel.add(cb);
                servicesPanel.add(Box.createVerticalStrut(7));
            }

            // Добавление панелей в columnsPanel
            columnsPanel.add(branchesPanel);
            columnsPanel.add(servicesPanel);

            // 2.8. Создание кнопок
            JButton openButton = createStyledButton("Сделать скриншот", new Color(0x45C7AA));
            JButton sendInJiraButtonNew = createStyledButton("Отправить комментарий", new Color(0x45C7AA));

            // 2.9. Обработчики событий для кнопок
            openButton.addActionListener(e -> {
                String url = "https://grafana.devtcn.tech/d/1CiDdN4Sk/stage-image-list?orgId=1&var-namespace=devtcn&var-pod=All&var-container=All&viewPanel=4&var-Filters=image%7C%3D%7Cnexus.devtcn.tech%2Freact-renderer-service:ca-TDF-12474-cat-6b2d9cae";
                try {
                    WebDriverManagerUtil.openWebpage(config.usernameGrafana, config.passwordGrafana, url,
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
            });

            sendInJiraButtonNew.addActionListener(e -> {
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
                String[] imagePathArray = {config.imagePathRRS, config.imagePathDRS, config.imagePathFCS, config.imagePathNW};

                try {
                    JiraCommentWithImage.addCommentWithImages(config.jiraUrl, config.usernameJira, config.TOKEN, ISSUE_ID, comment,
                            servicesArray, branchesArray, imagePathArray, IMAGE_LABELS, IMAGE_TEMPLATE, IMAGE_BRANCHES);
                } catch (Exception ex) {
                    System.err.println("Произошла ошибка: " + ex.getMessage());
                }
            });

            // 2.10. Панель для кнопок
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
            buttonPanel.add(openButton);
            buttonPanel.add(sendInJiraButtonNew);
            buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.setOpaque(false);

            // 2.11. Компоновка всех элементов на основной панели
            mainPanel.add(inputPanel);
            mainPanel.add(columnsPanel);
            mainPanel.add(Box.createVerticalStrut(15));
            mainPanel.add(buttonPanel);

            // 2.12. Завершающая настройка окна
            frame.getContentPane().add(mainPanel);
            frame.pack();
            frame.setLocationRelativeTo(null); // Центрирование окна
            frame.setVisible(true);
        });
    }
}