package com.mickeytheq.strangeeons.ahlcg4j.scratchpad;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import ca.cgjennings.imageio.JPEG2000;
import ca.cgjennings.io.protocols.MappedURLHandler;
import com.mickeytheq.strangeeons.ahlcg4j.Card;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceSide;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.asset.Asset;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.asset.AssetView;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery.Treachery;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery.TreacheryView;
import com.mickeytheq.strangeeons.ahlcg4j.plugin.Bootstrapper;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Locale;

public class EditCardScratch {
    public static void main(String[] args) {
        new EditCardScratch().run();
    }

    private void run() {

        Bootstrapper.initaliseOutsideStrangeEons();

        asset();
//        treachery();
    }

    private void asset() {
        Asset model = new Asset();
        model.getCommonCardFieldsModel().setTitle("Rat Swarm");
        model.getCommonCardFieldsModel().setRules("<rev> Do something with <t>A trait</t>.");

        AssetView view = new AssetView();

        Card card = new Card(view, model, null, null);
        model.initialiseModel(card, CardFaceSide.Front);
        view.initialiseView(model);

        JTabbedPane tabbedPane = new JTabbedPane();
        view.createEditors(tabbedPane);

        displayEditors(tabbedPane);
    }

    private void treachery() {
        Treachery model = new Treachery();
        model.getCommonCardFieldsModel().setTitle("Rat Swarm");
        model.getCommonCardFieldsModel().setRules("<rev> Do something with <t>A trait</t>.");

        TreacheryView view = new TreacheryView();

        Card card = new Card(view, model, null, null);
        model.initialiseModel(card, CardFaceSide.Front);
        view.initialiseView(model);

        JTabbedPane tabbedPane = new JTabbedPane();
        view.createEditors(tabbedPane);

        displayEditors(tabbedPane);
    }

    private static void displayEditors(JTabbedPane tabbedPane) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(tabbedPane);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
