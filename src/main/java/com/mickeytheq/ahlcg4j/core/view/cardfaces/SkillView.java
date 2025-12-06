package com.mickeytheq.ahlcg4j.core.view.cardfaces;

import ca.cgjennings.layout.PageShape;
import com.google.common.collect.Lists;
import com.mickeytheq.ahlcg4j.codegenerated.GameConstants;
import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Skill;
import com.mickeytheq.ahlcg4j.core.model.common.PlayerCardType;
import com.mickeytheq.ahlcg4j.core.view.BaseCardFaceView;
import com.mickeytheq.ahlcg4j.core.view.EditorContext;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;
import com.mickeytheq.ahlcg4j.core.view.View;
import com.mickeytheq.ahlcg4j.core.view.common.*;
import com.mickeytheq.ahlcg4j.core.view.utils.ImageUtils;
import com.mickeytheq.ahlcg4j.core.view.utils.MarkupUtils;
import com.mickeytheq.ahlcg4j.core.view.utils.MigLayoutUtils;
import com.mickeytheq.ahlcg4j.core.view.utils.PaintUtils;
import net.miginfocom.swing.MigLayout;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

@View(interfaceLanguageKey = InterfaceConstants.SKILL)
public class SkillView extends BaseCardFaceView<Skill> {
    private CommonCardFieldsView commonCardFieldsView;
    private NumberingView numberingView;
    private PlayerCardFieldsView playerCardFieldsView;
    private PortraitWithArtistView portraitWithArtistView;

    private static final Rectangle ART_PORTRAIT_DRAW_REGION = new Rectangle(28, 80, 696, 596);
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(348, 517, 56, 56);
    private static final Rectangle COLLECTION_PORTRAIT_DRAW_REGION = new Rectangle(640, 1016, 26, 26);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        numberingView = new NumberingView(getModel().getNumberingModel(), COLLECTION_PORTRAIT_DRAW_REGION.getSize(), ENCOUNTER_PORTRAIT_DRAW_REGION.getSize());
        playerCardFieldsView = new PlayerCardFieldsView(getModel().getPlayerCardFieldsModel(), false);
        portraitWithArtistView = new PortraitWithArtistView(getModel().getPortraitWithArtistModel(), ART_PORTRAIT_DRAW_REGION.getSize());
    }

    @Override
    public BufferedImage getTemplateImage() {
        return ImageUtils.loadImage(getClass().getResource(getTemplateResource()));
    }

    // TODO: story_skill template is missing
    private String getTemplateResource() {
        return "/templates/skill/skill_" + getTemplateName() + ".png";
    }

    private String getTemplateName() {
        return playerCardFieldsView.getTemplateName();
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);

        portraitWithArtistView.createEditors(editorContext);

        createTitleAndStatisticsEditors(editorContext);

        createRulesAndPortraitTab(editorContext);

        numberingView.createEditors(editorContext);
        editorContext.addDisplayComponent("Collection / encounter", numberingView.createStandardCollectionEncounterPanel(editorContext));
    }

    private void createTitleAndStatisticsEditors(EditorContext editorContext) {
        // title
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false);

        playerCardFieldsView.createEditors(editorContext);

        MigLayout migLayout = playerCardFieldsView.createTwoColumnLayout();

        JPanel statsPanel = new JPanel(migLayout);
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats")); // TODO: i18n

        JPanel mainPanel = MigLayoutUtils.createEmbeddedPanel();
        mainPanel.add(titlePanel, "wrap, growx, pushx");
        mainPanel.add(statsPanel, "wrap, growx, pushx");

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.SKILL) + " - " + "Stats", mainPanel); // TODO: i18n

        // layout
        playerCardFieldsView.layoutFirstColumnLabels(statsPanel);
        playerCardFieldsView.layoutSecondColumnEditors(statsPanel);
        playerCardFieldsView.layoutThirdColumnLabels(statsPanel);
        playerCardFieldsView.layoutFourthColumnEditors(statsPanel);
    }

    private void createRulesAndPortraitTab(EditorContext editorContext) {
        JPanel generalPanel = MigLayoutUtils.createTitledPanel("General"); // TODO: i18n
        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel, false);

        JPanel mainPanel = MigLayoutUtils.createEmbeddedPanel();

        mainPanel.add(generalPanel, "wrap, pushx, growx");
        mainPanel.add(portraitWithArtistView.createStandardArtPanel(editorContext), "wrap, pushx, growx");

        // add the panel to the main tab control
        editorContext.addDisplayComponent("Rules / portrait", mainPanel); // TODO: i18n
    }

    private static final Rectangle LABEL_DRAW_REGION = new Rectangle(42, 124, 76, 28);
    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(146, 32, 588, 58);
    private static final Rectangle BODY_DRAW_REGION = new Rectangle(74, 702, 602, 290);
    private static final Rectangle BODY_WEAKNESS_DRAW_REGION = new Rectangle(74, 740, 602,250);
    private static final Rectangle WEAKNESS_LABEL_DRAW_REGION = new Rectangle(172, 696, 406, 30);
    private static final Rectangle BASIC_WEAKNESS_ICON_DRAW_REGION = new Rectangle(648, 26, 60, 60);
    private static final Rectangle BASIC_WEAKNESS_OVERLAY_DRAW_REGION = new Rectangle(624, 6, 108, 106);

    private static final PageShape BODY_PAGE_SHAPE = createBodyPageShape();

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitWithArtistView.paintArtPortrait(paintContext, ART_PORTRAIT_DRAW_REGION);

        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // label
        PaintUtils.paintLabel(paintContext, LABEL_DRAW_REGION, Language.gstring(GameConstants.LABEL_SKILL).toUpperCase());

        // title - skill titles are left aligned
        PaintUtils.paintTitleLeftAlign(paintContext, TITLE_DRAW_REGION, getModel().getCommonCardFieldsModel().getTitle(), getModel().getCommonCardFieldsModel().isUnique());

        Rectangle bodyDrawRegion = getBodyDrawRegion();
        commonCardFieldsView.paintBodyAndCopyright(paintContext, bodyDrawRegion, BODY_PAGE_SHAPE);

        if (getModel().getPlayerCardFieldsModel().getPlayerCardType().isHasEncounterDetails()) {
            numberingView.paintEncounterNumbers(paintContext);
            numberingView.paintEncounterPortrait(paintContext, ENCOUNTER_PORTRAIT_DRAW_REGION);
        }

        numberingView.paintCollectionPortrait(paintContext, COLLECTION_PORTRAIT_DRAW_REGION, true);
        numberingView.paintCollectionNumber(paintContext);

        portraitWithArtistView.paintArtist(paintContext);

        paintEncounterContent(paintContext);

        // weakness labels
        paintWeaknessContent(paintContext);

        playerCardFieldsView.paintLevel(paintContext);

        playerCardFieldsView.paintSkillIcons(paintContext);
    }

    private Rectangle getBodyDrawRegion() {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();

        if (playerCardType.isWeakness())
            return BODY_WEAKNESS_DRAW_REGION;

        return BODY_DRAW_REGION;
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
        } else if (playerCardType == PlayerCardType.BasicWeakness) {
            PaintUtils.paintLabel(paintContext, WEAKNESS_LABEL_DRAW_REGION, Language.gstring(GameConstants.LABEL_BASICWEAKNESS).toUpperCase());
            paintEncounterOrBasicWeaknessOverlay(paintContext);
            ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImage(ImageUtils.BASIC_WEAKNESS_ICON_RESOURCE), BASIC_WEAKNESS_ICON_DRAW_REGION);
        }
    }

    private static PageShape createBodyPageShape() {
        java.util.List<Point2D> pathPoints = Lists.newArrayList(
                new Point2D.Double(0.0, 0.0),
                new Point2D.Double(0.015, 1.0)
        );

        List<Point2D> bezierPoints = Lists.newArrayList(
                new Point2D.Double(0.053, 0.307),
                new Point2D.Double(0.088, 0.600)
        );

        Function<Point2D, Point2D> mapIntoRegionFunction = MarkupUtils.createRatioIntoDrawRegionMapper(BODY_DRAW_REGION);

        ListIterator<Point2D> pathPointIterator = pathPoints.listIterator();
        ListIterator<Point2D> bezierPointIterator = bezierPoints.listIterator();

        Path2D path2D = new Path2D.Double();

        Point2D firstPathPoint = mapIntoRegionFunction.apply(pathPointIterator.next());

        path2D.moveTo(firstPathPoint.getX(), firstPathPoint.getY());

        // first draw the curves in one direction
        while (pathPointIterator.hasNext()) {
            Point2D nextPoint = mapIntoRegionFunction.apply(pathPointIterator.next());

            Point2D firstBezierPoint = mapIntoRegionFunction.apply(bezierPointIterator.next());
            Point2D secondBezierPoint = mapIntoRegionFunction.apply(bezierPointIterator.next());

            path2D.curveTo(firstBezierPoint.getX(), firstBezierPoint.getY(), secondBezierPoint.getX(), secondBezierPoint.getY(), nextPoint.getX(), nextPoint.getY());
        }

        // second pass - the skill area is shaped like a scroll. translate the X coordinate to the other side
        // of the body region and repeat the curve there - remember 1.0d is a ratio so this is the entire region's width
        mapIntoRegionFunction = MarkupUtils.createRatioIntoDrawRegionMapperTranslateX(BODY_DRAW_REGION, 1.0d);

        // move across to the bottom right corner
        Point2D startSecondPassPoint = mapIntoRegionFunction.apply(pathPointIterator.previous());
        path2D.lineTo(startSecondPassPoint.getX(), startSecondPassPoint.getY());

        // draw the curve
        while (pathPointIterator.hasPrevious()) {
            Point2D nextPoint = mapIntoRegionFunction.apply(pathPointIterator.previous());

            Point2D firstBezierPoint = mapIntoRegionFunction.apply(bezierPointIterator.previous());
            Point2D secondBezierPoint = mapIntoRegionFunction.apply(bezierPointIterator.previous());

            path2D.curveTo(firstBezierPoint.getX(), firstBezierPoint.getY(), secondBezierPoint.getX(), secondBezierPoint.getY(), nextPoint.getX(), nextPoint.getY());
        }

        return new PageShape.GeometricShape(path2D, BODY_DRAW_REGION);
    }
}
