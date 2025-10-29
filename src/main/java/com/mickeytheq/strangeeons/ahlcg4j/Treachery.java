package com.mickeytheq.strangeeons.ahlcg4j;

import ca.cgjennings.apps.arkham.PortraitPanel;
import ca.cgjennings.apps.arkham.component.DefaultPortrait;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.strangeeons.ahlcg4j.util.*;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import resources.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

@CardFaceType(settingsTypeCode = "Treachery")
public class Treachery extends BaseCardFace {
    // TODO: add some help to get rid of the boilerplate for each field that consists of:
    //
    // SETTINGS
    //
    //   - populating from the settings when loading
    //   - updating the settings when saving
    //
    // EDITOR & LAYOUT
    //
    //   - having an appropriate editor field (may be difficult in edge cases so need an out to do it manually)
    //   - binding the editor field to the variable so changes in the UI reflect immediately to the variable which in turns is used when repainting
    //   - populating the editor field from the variable after the editors are created
    //   - placing the editor in the correct layout/tab etc
    //
    //   the declarative/annotation approach to this will 'hide' the editor/control from regular code so it may be difficult to automate the first items and NOT automate the last item
    //   in those cases it may have to be manual for all
    //   for this to be feasible we need to be able to integrate manual editor creation/management/layout for certain fields with automation for others
    //
    // DRAWING
    //
    //   - extracting the value from the variable and drawing it in the correct place on the sheet
    //
    //
    // perhaps a small utility library to create an object per field to manage some/all of the above
    // could further drive creation and setup of the above via annotations to cut down further - perhaps good for simple cases

    // TODO: saving via Ctrl+S doesn't clear the saved status - the title of the card in the explorer window stays bold

    // TODO: i18n/l10n for visible text. leverage the existing plugin property files or copy from them

    private String title;
    private String traits;
    private String keywords;
    private String rules;
    private String flavourText;
    private String victory;

    private String artist;
    private String copyright;

    private String collectionNumber;
    private String encounterNumber;
    private String encounterTotal;

    private JTextField titleField;
    private JTextField copyrightField;
    private JTextField traitsField;
    private JTextArea keywordsField;
    private JTextArea rulesField;
    private JTextArea flavorTextField;
    private JTextArea victoryField;

    private JTextField collectionNumberEditor;
    private JTextField encounterNumberEditor;
    private JTextField encounterTotalEditor;

    private DefaultPortrait artPortrait;
    private DefaultPortrait collectionPortrait;
    private DefaultPortrait encounterPortrait;

    // locations to draw portraits
    private static final Rectangle COLLECTION_PORTRAIT_CLIP = new Rectangle(320, 510, 13, 13);
    private static final Rectangle ENCOUNTER_PORTRAIT_CLIP = new Rectangle(175, 254, 28, 28);
    private static final Rectangle ART_PORTRAIT_CLIP = new Rectangle(17, 0, 344, 298);

    // locations to draw other elements
    private static final Rectangle LABEL_CLIP = new Rectangle(137, 286, 104, 14);
    private static final Rectangle TITLE_CLIP = new Rectangle(39, 307, 299, 29);
    private static final Rectangle BODY_CLIP = new Rectangle(30, 340, 318, 160);
    private static final Rectangle COLLECTION_NUMBER_CLIP = new Rectangle(318,512,37,10);
    private static final Rectangle ENCOUNTER_NUMBERS_CLIP = new Rectangle(247,512,55,10);


    @Override
    public void initialiseCardFace() {
        super.initialiseCardFace();

        artPortrait = createPortrait(getCardFaceSide().name(), ART_PORTRAIT_CLIP, getClass().getResource("/portrait/DefaultTexture.jp2"), true);
        collectionPortrait = createPortrait("Collection", COLLECTION_PORTRAIT_CLIP, getClass().getResource("/resources/spacers/empty1x1.png"), false);
        encounterPortrait = createPortrait("Encounter", ENCOUNTER_PORTRAIT_CLIP, getClass().getResource("/resources/spacers/empty1x1.png"), false);
    }

    private DefaultPortrait createPortrait(String portraitKey, Rectangle clipRegion, URL defaultImageResource, boolean fillBackground) {
        // the way portraits are managed in SE is somewhat weird
        // although many properties of the Portrait are embedded and serialised within the portrait class the clip stencil region
        // is always sourced from the settings
        // TODO: consider creating a new/modified Portrait implementation to get away from the Settings relationship
        getCard().getSettings().setRegion(portraitKey + "-portrait-clip", new Settings.Region(clipRegion));

        DefaultPortrait defaultPortrait = new DefaultPortrait(getCard(), portraitKey, false);
        defaultPortrait.setScaleUsesMinimum(false);
        defaultPortrait.setBackgroundFilled(fillBackground);
        defaultPortrait.installDefault();

        // the obvious thing to do here is call portrait.setSource() but that then shows the path to the
        // default image resource so instead we load the image ourselves and pass it in with a null source
        BufferedImage defaultImage = ImageUtils.loadImage(defaultImageResource);
        defaultPortrait.setImage(null, defaultImage);

        return defaultPortrait;
    }

    @Override
    public void createEditors(JTabbedPane tabbedPane) {
        JPanel mainPanel = new JPanel(new MigLayout());

        titleField = EditorUtils.createTextField(30);
        traitsField = EditorUtils.createTextField(30);
        keywordsField = EditorUtils.createTextArea(6, 30);
        rulesField = EditorUtils.createTextArea(6, 30);
        flavorTextField = EditorUtils.createTextArea(6, 30);
        victoryField = EditorUtils.createTextArea(2, 30);

        // TODO: this builder concept is kinda pointless right now
        EditorLayoutBuilder editorLayoutBuilder = EditorLayoutBuilder.create();

        JPanel generalPanel = editorLayoutBuilder.createGroupLayoutBuilder("General")
                .addLabelledEditor("Title", titleField)
                .addLabelledEditor("Traits", traitsField)
                .addLabelledEditor("Keywords", keywordsField)
                .addLabelledEditor("Rules", rulesField)
                .addLabelledEditor("Flavor", flavorTextField)
                .addLabelledEditor("Victory", victoryField)
                .getGroupPanel();

        mainPanel.add(generalPanel, "wrap, pushx, growx");

        PortraitPanel portraitPanel = new PortraitPanel();
        portraitPanel.setPanelTitle("Portrait");
        portraitPanel.setPortrait(artPortrait);

        mainPanel.add(portraitPanel, "wrap, pushx, growx");

        // add the panel to the main tab control
        tabbedPane.addTab(getCardFaceSide().name(), mainPanel);

        EditorUtils.bindTextComponent(titleField, wrapEditorBindingWithMarkedChanged(s -> title = s));
        EditorUtils.bindTextComponent(traitsField, wrapEditorBindingWithMarkedChanged(s -> traits = s));
        EditorUtils.bindTextComponent(keywordsField, wrapEditorBindingWithMarkedChanged(s -> keywords = s));
        EditorUtils.bindTextComponent(rulesField, wrapEditorBindingWithMarkedChanged(s -> rules = s));
        EditorUtils.bindTextComponent(flavorTextField, wrapEditorBindingWithMarkedChanged(s -> flavourText = s));
        EditorUtils.bindTextComponent(victoryField, wrapEditorBindingWithMarkedChanged(s -> victory = s));

        titleField.setText(title);
        traitsField.setText(traits);
        keywordsField.setText(keywords);
        rulesField.setText(rules);
        flavorTextField.setText(flavourText);
        victoryField.setText(victory);


        // collection
        PortraitPanel collectionPortraitPanel = new PortraitPanel();
        collectionPortraitPanel.setPanelTitle("Collection portrait");
        collectionPortraitPanel.setPortrait(collectionPortrait);

        collectionNumberEditor = EditorUtils.createTextField(8);
        collectionNumberEditor.setHorizontalAlignment(JTextField.RIGHT);

        JPanel collectionDetailPanel = new JPanel(new MigLayout());
        collectionDetailPanel.setBorder(BorderFactory.createTitledBorder("Collection"));
        collectionDetailPanel.add(new JLabel("Collection number: "), "aligny center");
        collectionDetailPanel.add(collectionNumberEditor, "wrap");

        // encounter
        PortraitPanel encounterPortraitPanel = new PortraitPanel();
        encounterPortraitPanel.setPanelTitle("Encounter portrait");
        encounterPortraitPanel.setPortrait(encounterPortrait);

        encounterNumberEditor = EditorUtils.createTextField(8);
        encounterNumberEditor.setHorizontalAlignment(JTextField.RIGHT);
        encounterTotalEditor = EditorUtils.createTextField(4);

        JPanel encounterDetailPanel = new JPanel(new MigLayout());
        encounterDetailPanel.setBorder(BorderFactory.createTitledBorder("Encounter"));
        encounterDetailPanel.add(new JLabel("Encounter number: "), "aligny center");
        encounterDetailPanel.add(encounterNumberEditor, "split");
        encounterDetailPanel.add(new JLabel(" / "), "split");
        encounterDetailPanel.add(encounterTotalEditor, "split, wrap");

        // merge collection and encounter into a single tab
        JPanel collectionEncounterPanel = new JPanel(new MigLayout());
        collectionEncounterPanel.add(collectionDetailPanel, "wrap, pushx, growx");
        collectionEncounterPanel.add(collectionPortraitPanel, "wrap, pushx, growx");
        collectionEncounterPanel.add(encounterDetailPanel, "wrap, pushx, growx");
        collectionEncounterPanel.add(encounterPortraitPanel, "wrap, pushx, growx");

        tabbedPane.addTab("Collection / encounter", collectionEncounterPanel);

        EditorUtils.bindTextComponent(collectionNumberEditor, wrapEditorBindingWithMarkedChanged(s -> collectionNumber = s));
        EditorUtils.bindTextComponent(encounterNumberEditor, wrapEditorBindingWithMarkedChanged(s -> encounterNumber = s));
        EditorUtils.bindTextComponent(encounterTotalEditor, wrapEditorBindingWithMarkedChanged(s -> encounterTotal = s));
    }

    @Override
    public BufferedImage loadTemplateImage() {
        return ImageUtils.loadImage(getClass().getResource("/templates/AHLCG-Treachery.jp2"));
    }

    @Override
    protected void paint(Sheet<Card> sheet, RenderTarget renderTarget) {
        // template
        Graphics2D g = sheet.createGraphics();
        try {
            // the graphics object created is on top of the existing image
            // so we have to clear it first to paint onto a blank canvas
            g.clearRect(0, 0, sheet.getTemplateWidth(), sheet.getTemplateHeight());

            // paint the main/art portrait first as it sits behind the card template
            artPortrait.paint(g, renderTarget);

            // draw the template
            g.drawImage(loadTemplateImage(), 0, 0, null);

            // label
            MarkupRenderer markupRenderer = new MarkupRenderer(sheet.getTemplateResolution());
            markupRenderer.setDefaultStyle(TextStyleUtils.getLargeLabelTextStyle());
            markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
            markupRenderer.setMarkupText("TREACHERY");
            markupRenderer.drawAsSingleLine(g, LABEL_CLIP);

            // title
            markupRenderer = new MarkupRenderer(sheet.getTemplateResolution());
            markupRenderer.setDefaultStyle(TextStyleUtils.getTitleTextStyle());
            markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
            markupRenderer.setMarkupText(title);
            markupRenderer.drawAsSingleLine(g, TITLE_CLIP);

            // body
            drawBody(g);

            // collection and encounter
            collectionPortrait.paint(g, renderTarget);
            encounterPortrait.paint(g, renderTarget);

            if (!StringUtils.isEmpty(collectionNumber)) {
                markupRenderer = new MarkupRenderer(sheet.getTemplateResolution());
                markupRenderer.setDefaultStyle(TextStyleUtils.getCollectionNumberTextStyle());
                markupRenderer.setMarkupText(collectionNumber);
                markupRenderer.drawAsSingleLine(g, COLLECTION_NUMBER_CLIP);
            }

            if (!StringUtils.isEmpty(encounterNumber) || !StringUtils.isEmpty(encounterTotal)) {
                markupRenderer = new MarkupRenderer(sheet.getTemplateResolution());
                markupRenderer.setDefaultStyle(TextStyleUtils.getEncounterNumberTextStyle());

                String text = StringUtils.defaultIfEmpty(encounterNumber, "") + " / " + StringUtils.defaultIfEmpty(encounterTotal, "");

                markupRenderer.setMarkupText(text);
                markupRenderer.drawAsSingleLine(g, ENCOUNTER_NUMBERS_CLIP);
            }
        } finally {
            g.dispose();
        }
    }

    private void drawBody(Graphics2D g) {
        MarkupRenderer markupRenderer = MarkupUtils.getBodyMarkupRenderer(getSheet());

        // TODO: spacing

        StringBuilder sb = new StringBuilder();

        if (StringUtils.isEmpty(traits)) {
            // if there's no traits some space is added instead
            sb.append(MarkupUtils.getSpacerMarkup(1, 6));
        } else {
            sb.append("<center>");
            sb.append("<ts>");
            sb.append(traits);
            sb.append("</ts>");

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 0.5));
        }

        if (!StringUtils.isEmpty(keywords)) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<left>");
            sb.append(keywords);

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        if (!StringUtils.isEmpty(rules)) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<left>");
            sb.append(rules);

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        if (!StringUtils.isEmpty(flavourText)) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<center>");
            sb.append("<fs>");
            sb.append(flavourText);
            sb.append("</fs>");

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        if (!StringUtils.isEmpty(victory)) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<center>");
            sb.append("<vic>");
            sb.append(victory);
            sb.append("</vic>");
        }

        markupRenderer.setMarkupText(sb.toString());

        // new tag that is always for the face's title
        if (!StringUtils.isEmpty(title))
            markupRenderer.setReplacementForTag("title", title);

        // TODO: add tag values for name, fullname and fullnameb

        markupRenderer.draw(g, BODY_CLIP);
    }

    // TODO: consider how to serialise Portraits - do we follow the same as SE and embed the image or make it a much lighter concept
    // TODO: and instead just serialise the image path and pan/scale settings
    @Override
    public void afterSettingsRead(Settings settings, ObjectInputStream objectInputStream) {
        title = settings.get("Front.Title");
        rules = settings.get("Front.Rules");
        flavourText = settings.get("Front.FlavorText");
        victory = settings.get("Front.Victory");
        keywords = settings.get("Front.Keywords");
        traits = settings.get("Front.Traits");

        try {
            artPortrait = (DefaultPortrait)objectInputStream.readObject();
            collectionPortrait = (DefaultPortrait)objectInputStream.readObject();
            encounterPortrait = (DefaultPortrait)objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeSettingsWrite(Settings settings) {
        settings.set("Front.Title", title);
        settings.set("Front.Rules", rules);
        settings.set("Front.FlavorText", flavourText);
        settings.set("Front.Victory", victory);
        settings.set("Front.Keywords", keywords);
        settings.set("Front.Traits", traits);
    }

    @Override
    public void afterSettingsWrite(ObjectOutputStream objectOutputStream) {
        try {
            objectOutputStream.writeObject(artPortrait);
            objectOutputStream.writeObject(collectionPortrait);
            objectOutputStream.writeObject(encounterPortrait);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
