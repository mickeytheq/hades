package com.mickeytheq.ahlcg4j.core.view.cardfaces;

import com.google.common.collect.Lists;
import com.mickeytheq.ahlcg4j.core.view.EditorContext;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Event;
import com.mickeytheq.ahlcg4j.core.view.BaseCardFaceView;
import com.mickeytheq.ahlcg4j.core.view.View;
import com.mickeytheq.ahlcg4j.core.view.common.CommonCardFieldsView;
import com.mickeytheq.ahlcg4j.core.view.common.NumberingView;
import com.mickeytheq.ahlcg4j.core.view.common.PlayerCardFieldsView;
import com.mickeytheq.ahlcg4j.core.view.utils.ImageUtils;
import com.mickeytheq.ahlcg4j.core.view.utils.MigLayoutUtils;
import com.mickeytheq.ahlcg4j.core.view.utils.PaintUtils;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardClass;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardSkillIcon;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardType;
import com.mickeytheq.ahlcg4j.codegenerated.GameConstants;
import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

@View(interfaceLanguageKey = InterfaceConstants.EVENT)
public class EventView extends BaseCardFaceView<Event> {
    private CommonCardFieldsView commonCardFieldsView;
    private NumberingView numberingView;
    private PlayerCardFieldsView playerCardFieldsView;

    private static final Rectangle ART_PORTRAIT_DRAW_REGION = new Rectangle(0, 0, 750, 576);
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(348, 517, 56, 56);
    private static final Rectangle COLLECTION_PORTRAIT_DRAW_REGION = new Rectangle(640, 1016, 26, 26);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        numberingView = new NumberingView(getModel().getNumberingModel());
        playerCardFieldsView = new PlayerCardFieldsView(getModel().getPlayerCardFieldsModel());
    }

    @Override
    public BufferedImage getTemplateImage() {
        return ImageUtils.loadImage(getClass().getResource(getTemplateResource()));
    }

    private String getTemplateResource() {
        return "/templates/event/event_" + getTemplateName() + ".png";
    }

    private String getTemplateName() {
        if (getModel().getPlayerCardFieldsModel().getPlayerCardType() == PlayerCardType.Standard) {
            java.util.List<PlayerCardClass> cardClasses = getModel().getPlayerCardFieldsModel().getPlayerCardClasses();

            if (cardClasses.size() > 1)
                return "multi";
            else
                return cardClasses.get(0).name().toLowerCase();
        }

        switch (getModel().getPlayerCardFieldsModel().getPlayerCardType()) {
            case Neutral:
                return "neutral";

            case Specialist:
                return "specialist";

            case Story:
                return "story";

            case StoryWeakness:
            case Weakness:
            case BasicWeakness:
                return "weakness";

            default:
                throw new RuntimeException("Unsupported player card type " + getModel().getPlayerCardFieldsModel().getPlayerCardType().name());
        }
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        createTitleAndStatisticsEditors(editorContext);

        createRulesAndPortraitTab(editorContext);

        numberingView.createEditors(editorContext, COLLECTION_PORTRAIT_DRAW_REGION.getSize(), ENCOUNTER_PORTRAIT_DRAW_REGION.getSize());
        editorContext.getTabbedPane().addTab("Collection / encounter", numberingView.createStandardCollectionEncounterPanel(editorContext));
    }

    private void createTitleAndStatisticsEditors(EditorContext editorContext) {
        // title
        JPanel titlePanel = MigLayoutUtils.createPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false);

        playerCardFieldsView.createEditors(editorContext);

        // TODO: factor this layout construction out
        // layout
        //
        // use vertical flow layout to aid readability
        // each column of controls will be added/built one after another instead of row by row
        // we'll use 'newline' constraint on the component at the beginning of a new column
        MigLayout migLayout = new MigLayout(new LC().flowY());

        // this constraint
        // - makes the 2 control columns grow/fill all available space
        // - adds a small visual gap between the two sets of vertical labels/controls
        // - configure the last column to have a reasonable size
        migLayout.setColumnConstraints("[][grow, fill]10[][grow, fill, :200:]");

        JPanel statsPanel = new JPanel(migLayout);
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats")); // TODO: i18n

        JPanel mainPanel = MigLayoutUtils.createPanel();
        mainPanel.add(titlePanel, "wrap, growx, pushx");
        mainPanel.add(statsPanel, "wrap, growx, pushx");

        editorContext.getTabbedPane().addTab(Language.string(InterfaceConstants.EVENT) + " - " + "Stats", mainPanel); // TODO: i18n

        // layout
        playerCardFieldsView.layoutFirstColumnLabels(statsPanel);
        playerCardFieldsView.layoutSecondColumnEditors(statsPanel);
        playerCardFieldsView.layoutThirdColumnLabels(statsPanel);
        playerCardFieldsView.layoutFourthColumnEditors(statsPanel);
    }

    private void createRulesAndPortraitTab(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext, ART_PORTRAIT_DRAW_REGION.getSize());

        JPanel generalPanel = MigLayoutUtils.createPanel("General"); // TODO: i18n
        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel);

        JPanel mainPanel = new JPanel(new MigLayout());

        mainPanel.add(generalPanel, "wrap, pushx, growx");
        mainPanel.add(commonCardFieldsView.createStandardArtPanel(editorContext), "wrap, pushx, growx");

        // add the panel to the main tab control
        editorContext.getTabbedPane().addTab("Rules / portrait", mainPanel); // TODO: i18n
    }

    private static final Rectangle LABEL_DRAW_REGION = new Rectangle(42, 126, 72, 28);
    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(80, 608, 594, 58);
    private static final Rectangle BODY_DRAW_REGION = new Rectangle(88, 676, 576, 316);
    private static final Rectangle BODY_WEAKNESS_DRAW_REGION = new Rectangle(88, 704, 576, 286);
    private static final Rectangle WEAKNESS_LABEL_DRAW_REGION = new Rectangle(172, 666, 406, 30);
    private static final Rectangle BASIC_WEAKNESS_ICON_DRAW_REGION = new Rectangle(346, 516, 60, 60);
    private static final Rectangle BASIC_WEAKNESS_OVERLAY_DRAW_REGION = new Rectangle(324, 494, 108, 106);


    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        commonCardFieldsView.paintArtPortrait(paintContext, ART_PORTRAIT_DRAW_REGION);

        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // label
        PaintUtils.paintLabel(paintContext, LABEL_DRAW_REGION, Language.gstring(GameConstants.LABEL_EVENT).toUpperCase());

        // title
        // TODO: for multi-class cards the title position may need to be shifted left somewhat - see Bruiser as an example
        commonCardFieldsView.paintTitle(paintContext, TITLE_DRAW_REGION);

        Rectangle bodyDrawRegion = getBodyDrawRegion();
        commonCardFieldsView.paintBodyCopyrightArtist(paintContext, bodyDrawRegion);

        if (getModel().getPlayerCardFieldsModel().getPlayerCardType().isHasEncounterDetails()) {
            numberingView.paintEncounterNumbers(paintContext);
            numberingView.paintEncounterPortrait(paintContext, ENCOUNTER_PORTRAIT_DRAW_REGION);
        }

        numberingView.paintCollectionPortrait(paintContext, COLLECTION_PORTRAIT_DRAW_REGION, true);
        numberingView.paintCollectionNumber(paintContext);

        // player card icons
        paintClassSymbols(paintContext);

        paintEncounterContent(paintContext);

        // weakness labels
        paintWeaknessContent(paintContext);

        playerCardFieldsView.paintLevel(paintContext);

        playerCardFieldsView.paintCost(paintContext);

        playerCardFieldsView.paintSkillIcons(paintContext);
    }

    private Rectangle getBodyDrawRegion() {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();

        if (playerCardType.isWeakness())
            return BODY_WEAKNESS_DRAW_REGION;

        return BODY_DRAW_REGION;
    }

    // regions are from right to left
    private static final List<Rectangle> CLASS_SYMBOL_REGIONS_PAIR = Lists.newArrayList(
            new Rectangle(288, 512, 90, 90),
            new Rectangle(374, 512, 90, 90)
    );

    private static final List<Rectangle> CLASS_SYMBOL_REGIONS_TRIPLE = Lists.newArrayList(
            new Rectangle(246, 512, 90, 90),
            new Rectangle(332, 512, 90, 90),
            new Rectangle(418, 512, 90, 90)
    );

    private void paintClassSymbols(PaintContext paintContext) {
        java.util.List<PlayerCardClass> playerCardClasses = getModel().getPlayerCardFieldsModel().getPlayerCardClasses();

        // no class symbols if no classes
        if (playerCardClasses.isEmpty())
            return;

        // for a single class the symbol is integrated into the template
        if (playerCardClasses.size() == 1)
            return;

        // the layout of the icons is different for 2 vs 3
        List<Rectangle> drawRegions;

        if (playerCardClasses.size() == 2)
            drawRegions = CLASS_SYMBOL_REGIONS_PAIR;
        else
            drawRegions = CLASS_SYMBOL_REGIONS_TRIPLE;

        for (int i = 0; i < playerCardClasses.size(); i++) {
            PlayerCardClass playerCardClass = playerCardClasses.get(i);

            BufferedImage classSymbol = ImageUtils.loadImage(getClass().getResource("/overlays/class_symbol_" + playerCardClass.name().toLowerCase() + ".png"));

            Rectangle rectangle = drawRegions.get(i);

            PaintUtils.paintBufferedImage(paintContext.getGraphics(), classSymbol, rectangle);
        }
    }

    private void paintEncounterContent(PaintContext paintContext) {
        if (!getModel().getPlayerCardFieldsModel().getPlayerCardType().isHasEncounterDetails())
            return;

        paintEncounterOrBasicWeaknessOverlay(paintContext);

        numberingView.paintEncounterPortrait(paintContext, ENCOUNTER_PORTRAIT_DRAW_REGION);
    }

    private void paintEncounterOrBasicWeaknessOverlay(PaintContext paintContext) {
        ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImage(getClass().getResource("/overlays/encounter_event.png")), BASIC_WEAKNESS_OVERLAY_DRAW_REGION);
    }

    private void paintWeaknessContent(PaintContext paintContext) {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();

        if (playerCardType == PlayerCardType.Weakness || playerCardType == PlayerCardType.StoryWeakness) {
            PaintUtils.paintLabel(paintContext, WEAKNESS_LABEL_DRAW_REGION, Language.gstring(GameConstants.LABEL_WEAKNESS).toUpperCase());
        }
        else if (playerCardType == PlayerCardType.BasicWeakness) {
            PaintUtils.paintLabel(paintContext, WEAKNESS_LABEL_DRAW_REGION, Language.gstring(GameConstants.LABEL_BASICWEAKNESS).toUpperCase());
            paintEncounterOrBasicWeaknessOverlay(paintContext);
            ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImage(ImageUtils.BASIC_WEAKNESS_ICON_RESOURCE), BASIC_WEAKNESS_ICON_DRAW_REGION);
        }
    }
}
