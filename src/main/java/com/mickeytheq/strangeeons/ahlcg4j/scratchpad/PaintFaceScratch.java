package com.mickeytheq.strangeeons.ahlcg4j.scratchpad;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import ca.cgjennings.imageio.JPEG2000;
import ca.cgjennings.io.protocols.MappedURLHandler;
import com.mickeytheq.strangeeons.ahlcg4j.Card;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceSide;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery.Treachery;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery.TreacheryView;
import com.mickeytheq.strangeeons.ahlcg4j.plugin.Bootstrapper;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Locale;

public class PaintFaceScratch {
    public static void main(String[] args) {
        new PaintFaceScratch().run();
    }

    private void run() {
        JPEG2000.registerServiceProviders(true);
        Language.setGameLocale(Locale.ENGLISH);
        Language.setInterfaceLocale(Locale.ENGLISH);
        MappedURLHandler.install();

        Bootstrapper.initialise();

        Treachery treachery = new Treachery();
        treachery.getCommonCardFieldsModel().setTitle("Rat Swarm");
        treachery.getCommonCardFieldsModel().setRules("<rev> Do something with <t>A trait</t>.");

        TreacheryView treacheryView = new TreacheryView();

        Card card = new Card(treacheryView, treachery, null, null);
        treachery.initialiseModel(card, CardFaceSide.Front);
        treacheryView.initialiseView(treachery);

        Sheet<Card> sheet = treacheryView.createSheet();
        BufferedImage image = sheet.paint(RenderTarget.EXPORT, 300);

        displayImage(image);
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
