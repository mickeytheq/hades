package com.mickeytheq.strangeeons.ahlcg4j;

import ca.cgjennings.apps.arkham.PortraitPanel;
import ca.cgjennings.apps.arkham.component.DefaultPortrait;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import ca.cgjennings.graphics.filters.InversionFilter;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.strangeeons.ahlcg4j.util.*;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import resources.Language;
import resources.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

@CardFaceType(typeCode = "Treachery", interfaceLanguageKey = InterfaceConstants.TREACHERY)
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

    // TODO: i18n/l10n for visible text. leverage the existing plugin property files or copy from them

    private static final URL DEFAULT_TEMPLATE_RESOURCE;
    private static final URL WEAKNESS_TEMPLATE_RESOURCE;

    private static final URL BASIC_WEAKNESS_OVERLAY_RESOURCE;
    private static final URL BASIC_WEAKNESS_ICON_RESOURCE;

    static {
        DEFAULT_TEMPLATE_RESOURCE = Treachery.class.getResource("/templates/AHLCG-Treachery.jp2");
        WEAKNESS_TEMPLATE_RESOURCE = Treachery.class.getResource("/templates/AHLCG-WeaknessTreachery.jp2");

        BASIC_WEAKNESS_OVERLAY_RESOURCE = Treachery.class.getResource("/overlays/AHLCG-BasicWeakness.png");
        BASIC_WEAKNESS_ICON_RESOURCE = Treachery.class.getResource("/icons/AHLCG-BasicWeakness.png");
    }

    private WeaknessType weaknessType;

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

    private JComboBox<WeaknessType> weaknessTypeEditor;

    private JTextField titleEditor;
    private JTextField copyrightEditor;
    private JTextField traitsEditor;
    private JTextArea keywordsEditor;
    private JTextArea rulesEditor;
    private JTextArea flavorTextEditor;
    private JTextArea victoryEditor;
    private JTextField artistEditor;

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
    private static final Rectangle BODY_WEAKNESS_CLIP = new Rectangle(30, 357, 318, 145);
    private static final Rectangle COLLECTION_NUMBER_CLIP = new Rectangle(318, 512, 37, 10);
    private static final Rectangle ENCOUNTER_NUMBERS_CLIP = new Rectangle(247, 512, 55, 10);

    private static final Rectangle ARTIST_CLIP = new Rectangle(14, 512, 121, 10);
    private static final Rectangle COPYRIGHT_CLIP = new Rectangle(137, 512, 101, 10);

    private static final Rectangle BASIC_WEAKNESS_OVERLAY_CLIP = new Rectangle(156, 243, 66, 41);
    private static final Rectangle BASIC_WEAKNESS_ICON_CLIP = new Rectangle(175, 253, 28, 28);
    private static final Rectangle WEAKNESS_SUBTYPE_CLIP = new Rectangle(88, 335, 200, 17);

    @Override
    public void initialiseCardFace() {
        super.initialiseCardFace();

        artPortrait = createPortrait(getCardFaceSide().name(), ART_PORTRAIT_CLIP, getClass().getResource("/portrait/DefaultTexture.jp2"), true);
        collectionPortrait = createPortrait("Collection", COLLECTION_PORTRAIT_CLIP, getClass().getResource("/resources/spacers/empty1x1.png"), false);
        encounterPortrait = createPortrait("Encounter", ENCOUNTER_PORTRAIT_CLIP, getClass().getResource("/resources/spacers/empty1x1.png"), false);

        weaknessType = WeaknessType.None;
    }

    private DefaultPortrait createPortrait(String portraitKey, Rectangle clipRegion, URL defaultImageResource, boolean fillBackground) {
        // the way portraits are managed in SE is somewhat weird
        // although many properties of the Portrait are embedded and serialised within the portrait class the clip stencil region
        // is always sourced from the settings
        // TODO: consider creating a new/modified Portrait implementation to get away from the Settings relationship

        // TODO: also installDefault() tries to read a default template from the settings as well which wants to be in Strange Eons resource path format rather than a URL
        // TODO: another reason to re-implement the Portrait to remove this annoyance
        getCard().getSettings().setRegion(portraitKey + "-portrait-clip", new Settings.Region(clipRegion));
        getCard().getSettings().set(portraitKey + "-portrait-template", defaultImageResource.toExternalForm());

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
        weaknessTypeEditor = new JComboBox<>();
        weaknessTypeEditor.addItem(WeaknessType.None);
        weaknessTypeEditor.addItem(WeaknessType.Basic);
        weaknessTypeEditor.addItem(WeaknessType.Investigator);
        weaknessTypeEditor.addItem(WeaknessType.Story);

        EditorUtils.bindComboBox(weaknessTypeEditor, wrapEditorBindingWithMarkedChanged(value -> weaknessType = value));

        titleEditor = EditorUtils.createTextField(30);
        traitsEditor = EditorUtils.createTextField(30);
        keywordsEditor = EditorUtils.createTextArea(6, 30);
        rulesEditor = EditorUtils.createTextArea(6, 30);
        flavorTextEditor = EditorUtils.createTextArea(6, 30);
        victoryEditor = EditorUtils.createTextArea(2, 30);
        copyrightEditor = EditorUtils.createTextField(30);

        // TODO: this builder concept is kinda pointless right now - maybe replace with a simpler utility method
        // TODO: to build a two column layout
        EditorLayoutBuilder editorLayoutBuilder = EditorLayoutBuilder.create();

        // TODO: what about the helper tooltips for the legal traits etc
        JPanel generalPanel = editorLayoutBuilder.createGroupLayoutBuilder("General")
                .addLabelledEditor("Title", titleEditor)
                .addLabelledEditor("Weakness type", weaknessTypeEditor)
                .addLabelledEditor("Traits", traitsEditor)
                .addLabelledEditor("Keywords", keywordsEditor)
                .addLabelledEditor("Rules", rulesEditor)
                .addLabelledEditor("Flavor", flavorTextEditor)
                .addLabelledEditor("Victory", victoryEditor)
                .addLabelledEditor("Copyright", copyrightEditor)
                .getGroupPanel();

        JPanel mainPanel = new JPanel(new MigLayout());

        mainPanel.add(generalPanel, "wrap, pushx, growx");

        PortraitPanel portraitPanel = new PortraitPanel();
        portraitPanel.setPanelTitle("Portrait");
        portraitPanel.setPortrait(artPortrait);

        artistEditor = EditorUtils.createTextField(30);

        mainPanel.add(portraitPanel, "wrap, pushx, growx");

        JPanel artistPanel = editorLayoutBuilder.createGroupLayoutBuilder("Artist")
                .addLabelledEditor("Artist", artistEditor)
                .getGroupPanel();

        mainPanel.add(artistPanel, "wrap, pushx, growx");

        // add the panel to the main tab control
        tabbedPane.addTab(getCardFaceSide().name(), mainPanel);

        EditorUtils.bindTextComponent(titleEditor, wrapEditorBindingWithMarkedChanged(s -> {
            title = s;
            getCard().setName(title);
        }));
        EditorUtils.bindTextComponent(traitsEditor, wrapEditorBindingWithMarkedChanged(s -> traits = s));
        EditorUtils.bindTextComponent(keywordsEditor, wrapEditorBindingWithMarkedChanged(s -> keywords = s));
        EditorUtils.bindTextComponent(rulesEditor, wrapEditorBindingWithMarkedChanged(s -> rules = s));
        EditorUtils.bindTextComponent(flavorTextEditor, wrapEditorBindingWithMarkedChanged(s -> flavourText = s));
        EditorUtils.bindTextComponent(victoryEditor, wrapEditorBindingWithMarkedChanged(s -> victory = s));
        EditorUtils.bindTextComponent(copyrightEditor, wrapEditorBindingWithMarkedChanged(s -> copyright = s));
        EditorUtils.bindTextComponent(artistEditor, wrapEditorBindingWithMarkedChanged(s -> artist = s));

        titleEditor.setText(title);
        traitsEditor.setText(traits);
        keywordsEditor.setText(keywords);
        rulesEditor.setText(rules);
        flavorTextEditor.setText(flavourText);
        victoryEditor.setText(victory);
        copyrightEditor.setText(copyright);
        artistEditor.setText(artist);


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
        URL templateUrl;
        if (weaknessType != WeaknessType.None)
            templateUrl = WEAKNESS_TEMPLATE_RESOURCE;
        else
            templateUrl = DEFAULT_TEMPLATE_RESOURCE;

        return ImageUtils.loadImage(templateUrl);
    }

    @Override
    protected void paint(Sheet<Card> sheet, Graphics2D g, RenderTarget renderTarget) {
        // paint the main/art portrait first as it sits behind the card template
        artPortrait.paint(g, renderTarget);

        // draw the template
        g.drawImage(loadTemplateImage(), 0, 0, null);

        MarkupRenderer markupRenderer;

        // label
        markupRenderer = new MarkupRenderer(getSheet().getTemplateResolution());
        markupRenderer.setDefaultStyle(TextStyleUtils.getLargeLabelTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText("TREACHERY");
        markupRenderer.drawAsSingleLine(g, LABEL_CLIP);

        // title
        markupRenderer = new MarkupRenderer(getSheet().getTemplateResolution());
        markupRenderer.setDefaultStyle(TextStyleUtils.getTitleTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(title);
        markupRenderer.drawAsSingleLine(g, TITLE_CLIP);

        if (weaknessType == WeaknessType.None)
            drawNonWeaknessContent(g, renderTarget);
        else
            drawWeaknessContent(g, renderTarget);

        // the 'footer' content that is at the bottom of the card
        // TODO: hopefully footer is consistent enough across most/all card types that the below code can be made a utility/library function and re-used

        // artist
        markupRenderer = new MarkupRenderer(sheet.getTemplateResolution());
        markupRenderer.setDefaultStyle(TextStyleUtils.getArtistTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_LEFT);
        markupRenderer.setMarkupText(artist);
        markupRenderer.drawAsSingleLine(g, ARTIST_CLIP);

        // copyright
        markupRenderer = new MarkupRenderer(sheet.getTemplateResolution());
        markupRenderer.setDefaultStyle(TextStyleUtils.getCopyrightTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(copyright);
        markupRenderer.drawAsSingleLine(g, COPYRIGHT_CLIP);

        // collection icon needs inverting as the source should be black but the background on most cards is white
        BufferedImageOp inversionOp = new InversionFilter();
        BufferedImage inverted = inversionOp.filter(collectionPortrait.getImage(), null);
        g.drawImage(inverted, (int) COLLECTION_PORTRAIT_CLIP.getX(), (int) COLLECTION_PORTRAIT_CLIP.getY(), (int) COLLECTION_PORTRAIT_CLIP.getWidth(), (int) COLLECTION_PORTRAIT_CLIP.getHeight(), null);

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
    }

    private void drawNonWeaknessContent(Graphics2D g, RenderTarget renderTarget) {
        String bodyText = composeBodyString();

        // body
        drawBody(g, bodyText, BODY_CLIP);

        encounterPortrait.paint(g, renderTarget);
    }

    private void drawWeaknessContent(Graphics2D g, RenderTarget renderTarget) {
        // draw the weakness overlay
        // this is the circular area either the standard basic weakness icon goes in or the encounter icon
        // for story weaknesses
        if (weaknessType == WeaknessType.Basic || weaknessType == WeaknessType.Story) {
            ImageUtils.drawImage(g, ImageUtils.loadImage(BASIC_WEAKNESS_OVERLAY_RESOURCE), BASIC_WEAKNESS_OVERLAY_CLIP);

            if (weaknessType == WeaknessType.Basic)
                ImageUtils.drawImage(g, ImageUtils.loadImage(BASIC_WEAKNESS_ICON_RESOURCE), BASIC_WEAKNESS_ICON_CLIP);
            else if (weaknessType == WeaknessType.Story)
                encounterPortrait.paint(g, renderTarget);
        }

        // the weakness/basic weakness fixed text
        String subTypeText = Language.gstring("WEAKNESS");

        if (weaknessType == WeaknessType.Basic)
            subTypeText = "BASIC WEAKNESS";

        MarkupRenderer markupRenderer = new MarkupRenderer(getSheet().getTemplateResolution());
        markupRenderer.setDefaultStyle(TextStyleUtils.getSubTypeTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(subTypeText);
        markupRenderer.drawAsSingleLine(g, WEAKNESS_SUBTYPE_CLIP);

        // body
        String bodyText = composeBodyString();

        drawBody(g, bodyText, BODY_WEAKNESS_CLIP);
    }

    private String composeBodyString() {
        // TODO: optional spacing

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

        return sb.toString();
    }

    private void drawBody(Graphics2D g, String markupText, Rectangle bodyDrawRegion) {
        MarkupRenderer markupRenderer = MarkupUtils.getBodyMarkupRenderer(getSheet());
        markupRenderer.setMarkupText(markupText);

        // new tag that is always for the face's title
        if (!StringUtils.isEmpty(title))
            markupRenderer.setReplacementForTag("title", title);

        // TODO: add tag values for name, fullname and fullnameb

        markupRenderer.draw(g, bodyDrawRegion);
    }

    // TODO: consider how to serialise Portraits - do we follow the same as SE and embed the image or make it a much lighter concept
    // TODO: and instead just serialise the image path and pan/scale settings

    // TODO: add annotation support to do bindings - hides the front/back business. can a library/function called from here to allow
    // anything a basic annotation bind won't handle. could do it for portraits too but need to make sure order is predictable

    // TODO: need a wrapper to manage settings prefixes (Front/Back) that is somewhat invisible to this code
    @Override
    public void afterSettingsRead(Settings settings, ObjectInputStream objectInputStream) {
        title = settings.get("Front.Title");
        rules = settings.get("Front.Rules");
        flavourText = settings.get("Front.FlavorText");
        victory = settings.get("Front.Victory");
        keywords = settings.get("Front.Keywords");
        traits = settings.get("Front.Traits");
        copyright = settings.get("Front.Copyright");
        artist = settings.get("Front.Artist");
        weaknessType = Enum.valueOf(WeaknessType.class, settings.get("Front.WeaknessType"));

        try {
            artPortrait = (DefaultPortrait) objectInputStream.readObject();
            collectionPortrait = (DefaultPortrait) objectInputStream.readObject();
            encounterPortrait = (DefaultPortrait) objectInputStream.readObject();
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
        settings.set("Front.Copyright", copyright);
        settings.set("Front.Artist", artist);
        settings.set("Front.WeaknessType", weaknessType.name());
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
