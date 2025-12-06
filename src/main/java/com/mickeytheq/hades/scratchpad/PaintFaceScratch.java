package com.mickeytheq.hades.scratchpad;

import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.core.model.cardfaces.Treachery;
import com.mickeytheq.hades.core.view.cardfaces.TreacheryView;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PaintFaceScratch {
    public static void main(String[] args) {
        new PaintFaceScratch().run();
    }

    private void run() {
        Bootstrapper.initaliseOutsideStrangeEons();

        Treachery treachery = new Treachery();
        treachery.getCommonCardFieldsModel().setTitle("Rat Swarm");
        treachery.getCommonCardFieldsModel().setRules("<rev> Do something with <t>A trait</t>.");

        TreacheryView treacheryView = new TreacheryView();

    }

    private static void displayImage(BufferedImage image) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
