package com.mickeytheq.strangeeons.ahlcg4j;

import ca.cgjennings.apps.arkham.PortraitPanel;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.strangeeons.ahlcg4j.util.EditorLayoutBuilder;
import com.mickeytheq.strangeeons.ahlcg4j.util.EditorUtils;
import com.mickeytheq.strangeeons.ahlcg4j.util.ImageUtils;
import net.miginfocom.swing.MigLayout;
import resources.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@CardFaceType(typeCode = "Asset", interfaceLanguageKey = InterfaceConstants.ASSET)
public class Asset extends BaseCardFace {
    private enum AssetType {
        Guardian, Seeker, Mystic, Rogue, Survivor, Neutral
    }

    private AssetType assetType;

    private JComboBox<AssetType> assetTypeEditor;

    @Override
    public void initialiseCardFace() {
        super.initialiseCardFace();

        // default to Guardian
        assetType = AssetType.Guardian;
    }

    @Override
    public BufferedImage loadTemplateImage() {
        String templateResource = "/templates/asset/AHLCG-Asset-" + assetType.name() + ".jp2";

        return ImageUtils.loadImage(getClass().getResource(templateResource));
    }

    @Override
    protected void paint(Sheet<Card> sheet, Graphics2D g, RenderTarget renderTarget) {
        // draw the template
        g.drawImage(loadTemplateImage(), 0, 0, null);
    }

    @Override
    public void createEditors(JTabbedPane tabbedPane) {
        assetTypeEditor = new JComboBox<>();
        assetTypeEditor.addItem(AssetType.Guardian);
        assetTypeEditor.addItem(AssetType.Seeker);
        assetTypeEditor.addItem(AssetType.Mystic);
        assetTypeEditor.addItem(AssetType.Rogue);
        assetTypeEditor.addItem(AssetType.Survivor);
        assetTypeEditor.addItem(AssetType.Neutral);

        EditorUtils.bindComboBox(assetTypeEditor, wrapEditorBindingWithMarkedChanged(s -> assetType = (AssetType) assetTypeEditor.getSelectedItem()));

        EditorLayoutBuilder editorLayoutBuilder = EditorLayoutBuilder.create();

        JPanel generalPanel = editorLayoutBuilder.createGroupLayoutBuilder("General")
                .addLabelledEditor("Class", assetTypeEditor)
                .getGroupPanel();

        JPanel mainPanel = new JPanel(new MigLayout());

        mainPanel.add(generalPanel, "wrap, pushx, growx");

        // add the panel to the main tab control
        tabbedPane.addTab(getCardFaceSide().name(), mainPanel);
    }

    @Override
    public void afterSettingsRead(Settings settings, ObjectInputStream objectInputStream) {

    }

    @Override
    public void beforeSettingsWrite(Settings settings) {

    }

    @Override
    public void afterSettingsWrite(ObjectOutputStream objectOutputStream) {

    }
}
