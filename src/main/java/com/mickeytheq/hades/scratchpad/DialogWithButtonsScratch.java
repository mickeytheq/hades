package com.mickeytheq.hades.scratchpad;

import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.ui.DialogEx;
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

        DialogEx dialogEx = new DialogEx((Frame)null, true);
        dialogEx.setContentComponent(panel);
        dialogEx.addCommitChangesButton("OK", 1, () -> Boolean.TRUE);
        dialogEx.addDiscardChangesButton("Cancel", 2);
        dialogEx.addHelpButton("Help", e -> { JOptionPane.showMessageDialog(dialogEx, "Help!"); } );
        dialogEx.addOtherButton("Change Label", e -> {
            panel2.removeAll();
            panel2.add(new JLabel(StringUtils.repeat("M", new Random().nextInt(100))));
            panel2.revalidate();
            panel2.repaint();
        } );

        int resultCode = dialogEx.showDialog();
    }
}
