package com.mickeytheq.hades.scratchpad;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.StrangeEonsAppWindow;
import ca.cgjennings.apps.arkham.project.Member;
import ca.cgjennings.apps.arkham.project.Project;
import com.mickeytheq.hades.strangeeons.tasks.NewCard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;

public class KeyboardShortcutScratch {
    public static void main(String[] args) {
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Hello");

        KeyStroke ctrlN = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_DOWN_MASK);

        label.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlN, "HadesNewCard");
        label.getActionMap().put("HadesNewCard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = 1;
            }
        });

        dialog.getContentPane().add(label);

        dialog.pack();

        dialog.setVisible(true);
    }
}
