package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UrlOpener {
//    private static final String URL = "https://www.qa-practice.com/elements/input/simple";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("URL Opener");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            JTextField urlField = new JTextField(30);

            JCheckBox checkRRSCommit = new JCheckBox("RRS");
            JCheckBox checkFCSCommit = new JCheckBox("FCS");
            JCheckBox checkAnother1 = new JCheckBox("Option 1");
            JCheckBox checkAnother2 = new JCheckBox("Option 2");
            JCheckBox checkAnother3 = new JCheckBox("Option 3");
            JCheckBox checkAnother4 = new JCheckBox("Option 4");

            JButton openButton = new JButton("Выполнить действия");
            JButton sendInJiraButton = new JButton("Отправить коммент");

            JPanel checkBoxPanel = new JPanel(new GridLayout(0, 4)); // 0 строк, 4 столбца
            checkBoxPanel.add(checkRRSCommit);
            checkBoxPanel.add(checkFCSCommit);
            checkBoxPanel.add(checkAnother1);
            checkBoxPanel.add(checkAnother2);
            checkBoxPanel.add(checkAnother3);
            checkBoxPanel.add(checkAnother4);

            openButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //String url = urlField.getText();
                    String url = ("https://grafana.devtcn.tech/d/1CiDdN4Sk/stage-image-list?orgId=1&var-namespace=devtcn&var-pod=All&var-container=All&viewPanel=4&var-Filters=image%7C%3D%7Cnexus.devtcn.tech%2Freact-renderer-service:ca-TDF-12474-cat-6b2d9cae");
                    //String url = ("https://grafana.devtcn.tech/d/1CiDdN4Sk/stage-image-list?orgId=1&var-namespace=devtcn&var-pod=All&var-container=All&viewPanel=4&var-Filters=image%7C%3D%7Cnexus.devtcn.tech%2Ffront-content-service:ca-devstand5-3bbdf0c3");
                    WebDriverManagerUtil.openWebpage(url,
                            checkRRSCommit.isSelected(), checkFCSCommit.isSelected());
                }
            });
            sendInJiraButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //String jiraUrl = urlField.getText();
                    String jiraUrl = "https://ticketon.atlassian.net/browse/TDF-13803";
                    String username = "i.sharipov@lantan.info";
                    String password = "TretGall02";
                    String imagePath = "/home/ivan/Загрузки/Workspace/java/autoCommit_ubuntu/autoCommit/RRS.png";
                    String comment = "Ваш комментарий с изображением";
                    // Создайте экземпляр класса JiraCommentUploader
                    JiraCommentUploader.sendInJira(jiraUrl, username, password, imagePath, comment);
                }
            });
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Центрируем кнопки
            buttonPanel.add(openButton);
            buttonPanel.add(sendInJiraButton);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Вертикальная компоновка
            mainPanel.add(urlField);
            mainPanel.add(checkBoxPanel);
            mainPanel.add(buttonPanel);

            frame.getContentPane().add(mainPanel);
            frame.setVisible(true);
        });
    }
}
