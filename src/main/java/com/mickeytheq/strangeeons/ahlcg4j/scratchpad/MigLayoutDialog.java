package com.mickeytheq.strangeeons.ahlcg4j.scratchpad;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MigLayoutDialog extends JDialog {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        MigLayoutDialog migLayoutDialog = new MigLayoutDialog();
        migLayoutDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        migLayoutDialog.pack();
        migLayoutDialog.setVisible(true);
    }

    public MigLayoutDialog() {
        JPanel mainPanel = new JPanel(new MigLayout());

        // title
        JPanel titlePanel = new JPanel(new MigLayout());
        titlePanel.setBorder(BorderFactory.createTitledBorder("Title"));

        titlePanel.add(new JLabel("Title: "), "aligny center");

        JTextField  titleField = new JTextField(30);
        titlePanel.add(titleField, "aligny center, wrap, pushx, growx");

        mainPanel.add(titlePanel, "wrap, pushx, growx");

        titleField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                int i = 1;
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        // copyright
        JPanel copyrightPanel = new JPanel(new MigLayout());
        copyrightPanel.setBorder(BorderFactory.createTitledBorder("Copyright"));

        copyrightPanel.add(new JLabel("Copyright: "));

        JTextField copyrightField = new JTextField(30);
        copyrightPanel.add(copyrightField, "wrap, pushx, growx");

        mainPanel.add(copyrightPanel, "wrap, pushx, growx");

        getContentPane().add(mainPanel);
    }
}
