package com.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UrlOpener {
//    private static final String URL = "https://www.qa-practice.com/elements/input/simple";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("URL Opener");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(350, 250);

            JTextField urlField = new JTextField(30);
            JCheckBox checkRRSCommit = new JCheckBox("RRS");
            JCheckBox checkFCSCommit = new JCheckBox("FCS");

            JButton openButton = new JButton("Выполнить действия");
            JButton sendInJiraButton = new JButton("Отправить коммент");

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

            JPanel panel = new JPanel();
            panel.add(urlField);
            panel.add(checkRRSCommit);
            panel.add(checkFCSCommit);
            panel.add(openButton);
            panel.add(sendInJiraButton);

            frame.getContentPane().add(panel);
            frame.setVisible(true);
        });
    }
}
