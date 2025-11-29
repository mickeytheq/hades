package com.mickeytheq.strangeeons.ahlcg4j.scratchpad;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.strangeeons.ahlcg4j.CardGameComponent;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceSide;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery.Treachery;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery.TreacheryView;
import com.mickeytheq.strangeeons.ahlcg4j.plugin.Bootstrapper;

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
