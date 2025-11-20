package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.asset;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.strangeeons.ahlcg4j.Card;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceSide;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.BaseCardFaceView;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.CommonCardFieldsView;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.NumberingView;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.PaintContext;
import com.mickeytheq.strangeeons.ahlcg4j.util.EditorLayoutBuilder;
import com.mickeytheq.strangeeons.ahlcg4j.util.EditorUtils;
import com.mickeytheq.strangeeons.ahlcg4j.util.ImageUtils;
import com.mickeytheq.strangeeons.ahlcg4j.util.MigLayoutUtils;
import net.miginfocom.swing.MigLayout;
import org.checkerframework.checker.units.qual.N;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AssetView extends BaseCardFaceView<Asset> {
    private JComboBox<Asset.AssetType> assetTypeEditor;

    private CommonCardFieldsView commonCardFieldsView;
    private NumberingView numberingView;

    @Override
    public void initialiseView() {
        super.initialiseView();

//        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), getViewContext(), xxx);
//        numberingView = new NumberingView(getModel().getNumberingModel(), getViewContext(), xxx, xxx);
    }

    @Override
    public BufferedImage loadTemplateImage() {
        String templateResource = "/templates/asset/AHLCG-Asset-" + getModel().getAssetType().name() + ".jp2";

        return ImageUtils.loadImage(getClass().getResource(templateResource));
    }

    @Override
    protected void paint(PaintContext paintContext) {
        // draw the template
        paintContext.getGraphics().drawImage(loadTemplateImage(), 0, 0, null);
    }

    @Override
    public void createEditors(JTabbedPane tabbedPane) {
        assetTypeEditor = new JComboBox<>();
        assetTypeEditor.addItem(Asset.AssetType.Guardian);
        assetTypeEditor.addItem(Asset.AssetType.Seeker);
        assetTypeEditor.addItem(Asset.AssetType.Mystic);
        assetTypeEditor.addItem(Asset.AssetType.Rogue);
        assetTypeEditor.addItem(Asset.AssetType.Survivor);
        assetTypeEditor.addItem(Asset.AssetType.Neutral);

        EditorUtils.bindComboBox(assetTypeEditor, getViewContext().wrapConsumerWithMarkedChanged(s -> getModel().setAssetType((Asset.AssetType) assetTypeEditor.getSelectedItem())));

        JPanel generalPanel = MigLayoutUtils.createPanel("General");

        commonCardFieldsView.addTitleEditorToPanel(generalPanel);

        generalPanel.add(new JLabel("Class type"));
        generalPanel.add(assetTypeEditor, "wrap, pushx, growx");

        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel);

        JPanel mainPanel = new JPanel(new MigLayout());

        mainPanel.add(generalPanel, "wrap, pushx, growx");

        mainPanel.add(commonCardFieldsView.createStandardArtPanel(), "wrap, pushx, growx");

        // add the panel to the main tab control
        tabbedPane.addTab(getModel().getCardFaceSide().name(), mainPanel);
        tabbedPane.addTab("Collection / encounter", numberingView.createStandardCollectionEncounterPanel());
    }
}
