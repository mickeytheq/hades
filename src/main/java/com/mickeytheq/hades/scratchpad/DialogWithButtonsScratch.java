package com.mickeytheq.hades.scratchpad;

import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.ui.DialogWithButtons;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class DialogWithButtonsScratch {
    public static void main(String[] args) {
        Bootstrapper.initaliseOutsideStrangeEons();

        JPanel panel = new JPanel();
        panel.setName("Test container panel");

        JPanel panel1 = new JPanel();
        panel1.setName("Panel 1");
        panel1.add(new JLabel("sofjkfshjkf hdfjgdhfghdjkfghdf gfdhjgkdhgfdhjkgdf"));

        JPanel panel2 = new JPanel();
        panel2.setName("Panel 2");
        panel2.add(new JLabel("sofjkfshjkf hdfjgdhfghdjkfghdf gfdhjgkdhgfdhjkgdf"));

        panel.add(panel1);
        panel.add(panel2);

        DialogWithButtons dialogWithButtons = new DialogWithButtons((Frame)null, true);
        dialogWithButtons.setContentComponent(panel);
        dialogWithButtons.addDialogClosingButton("OK", 1, () -> Boolean.TRUE);
        dialogWithButtons.addDialogClosingButton("Cancel", 2, () -> Boolean.TRUE);
        dialogWithButtons.addButton("Help", e -> { JOptionPane.showMessageDialog(dialogWithButtons, "Help!"); } );
        dialogWithButtons.addButton("Change Label", e -> {
            panel2.removeAll();
            panel2.add(new JLabel(StringUtils.repeat("M", new Random().nextInt(100))));
            panel2.revalidate();
            panel2.repaint();
        } );

        int resultCode = dialogWithButtons.showDialog();
    }
}
