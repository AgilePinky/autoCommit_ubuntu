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
            JCheckBox checkTextInput = new JCheckBox("Text input");
            JCheckBox checkEmailField = new JCheckBox("Email field");
            JCheckBox checkPasswordField = new JCheckBox("Password field");
            JCheckBox checkRRSCommit = new JCheckBox("RRS");
            JCheckBox checkFCSCommit = new JCheckBox("FCS");

            JButton openButton = new JButton("Выполнить действия");

            openButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String url = urlField.getText();
                    WebDriverManagerUtil.openWebpage(url, checkTextInput.isSelected(),
                            checkEmailField.isSelected(), checkPasswordField.isSelected(),
                            checkRRSCommit.isSelected(), checkFCSCommit.isSelected());
                }
            });

            JPanel panel = new JPanel();
            panel.add(urlField);
            panel.add(checkTextInput);
            panel.add(checkEmailField);
            panel.add(checkPasswordField);
            panel.add(checkRRSCommit);
            panel.add(checkFCSCommit);
            panel.add(openButton);

            frame.getContentPane().add(panel);
            frame.setVisible(true);
        });
    }
}
