package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.PageShape;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.model.cardfaces.Event;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.core.view.utils.MarkupUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.PaintUtils;
import com.mickeytheq.hades.core.model.common.PlayerCardClass;
import com.mickeytheq.hades.core.model.common.PlayerCardType;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

@View(interfaceLanguageKey = InterfaceConstants.EVENT)
public class EventView extends BaseCardFaceView<Event> implements HasCollectionView, HasEncounterSetView {
    private CommonCardFieldsView commonCardFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private PlayerCardFieldsView playerCardFieldsView;
    private PortraitWithArtistView portraitWithArtistView;

    private static final Rectangle ART_PORTRAIT_DRAW_REGION = new Rectangle(0, 0, 750, 576);
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(348, 517, 56, 56);
    private static final Rectangle COLLECTION_PORTRAIT_DRAW_REGION = new Rectangle(640, 1016, 26, 26);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        playerCardFieldsView = new PlayerCardFieldsView(getModel().getPlayerCardFieldsModel(), true);
        portraitWithArtistView = new PortraitWithArtistView(getModel().getPortraitWithArtistModel(), ART_PORTRAIT_DRAW_REGION.getSize());
    }

    @Override
    public CollectionView getCollectionView() {
        return collectionView;
    }

    @Override
    public EncounterSetView getEncounterSetView() {
        return encounterSetView;
    }

    @Override
    public String getTitle() {
        return StringUtils.defaultIfEmpty(commonCardFieldsView.getModel().getTitle(), null);
    }

    @Override
    public BufferedImage getTemplateImage() {
        return ImageUtils.loadImage(getClass().getResource(getTemplateResource()));
    }

    private String getTemplateResource() {
        return "/templates/event/event_" + getTemplateName() + ".png";
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

        collectionView.createEditors(editorContext);
        encounterSetView.createEditors(editorContext);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    private void createTitleAndStatisticsEditors(EditorContext editorContext) {
        // title
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false);

        playerCardFieldsView.createEditors(editorContext);

        MigLayout migLayout = playerCardFieldsView.createTwoColumnLayout();

        JPanel statsPanel = new JPanel(migLayout);
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats")); // TODO: i18n

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();
        mainPanel.add(titlePanel, "wrap, growx, pushx");
        mainPanel.add(statsPanel, "wrap, growx, pushx");

        editorContext.addDisplayComponent("Stats", mainPanel); // TODO: i18n

        // layout
        playerCardFieldsView.layoutFirstColumnLabels(statsPanel);
        playerCardFieldsView.layoutSecondColumnEditors(statsPanel);
        playerCardFieldsView.layoutThirdColumnLabels(statsPanel);
        playerCardFieldsView.layoutFourthColumnEditors(statsPanel);
    }

    private void createRulesAndPortraitTab(EditorContext editorContext) {
        JPanel generalPanel = MigLayoutUtils.createTitledPanel("General"); // TODO: i18n
        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel, false);

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();

        mainPanel.add(generalPanel, "wrap, pushx, growx");
        mainPanel.add(portraitWithArtistView.createStandardArtPanel(editorContext), "wrap, pushx, growx");

        // add the panel to the main tab control
        editorContext.addDisplayComponent("Rules / portrait", mainPanel); // TODO: i18n
    }

    private static final Rectangle LABEL_DRAW_REGION = new Rectangle(42, 126, 72, 28);
    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(80, 608, 594, 58);
    private static final Rectangle BODY_DRAW_REGION = new Rectangle(88, 676, 576, 316);
    private static final Rectangle BODY_WEAKNESS_DRAW_REGION = new Rectangle(88, 704, 576, 286);
    private static final Rectangle WEAKNESS_LABEL_DRAW_REGION = new Rectangle(172, 666, 406, 30);
    private static final Rectangle BASIC_WEAKNESS_ICON_DRAW_REGION = new Rectangle(346, 516, 60, 60);
    private static final Rectangle BASIC_WEAKNESS_OVERLAY_DRAW_REGION = new Rectangle(324, 494, 108, 106);

    private static final PageShape BODY_PAGE_SHAPE = createBodyPageShape();

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitWithArtistView.paintArtPortrait(paintContext, ART_PORTRAIT_DRAW_REGION);

        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // label
        PaintUtils.paintLabel(paintContext, LABEL_DRAW_REGION, Language.gstring(GameConstants.LABEL_EVENT).toUpperCase());

        // title
        commonCardFieldsView.paintTitle(paintContext, TITLE_DRAW_REGION);

        Rectangle bodyDrawRegion = getBodyDrawRegion();
        commonCardFieldsView.paintBodyAndCopyright(paintContext, bodyDrawRegion, BODY_PAGE_SHAPE);

        collectionView.paintCollectionPortrait(paintContext, COLLECTION_PORTRAIT_DRAW_REGION, true);
        collectionView.paintCollectionNumber(paintContext);

        portraitWithArtistView.paintArtist(paintContext);

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

        encounterSetView.paintEncounterNumbers(paintContext);
        encounterSetView.paintEncounterPortrait(paintContext, ENCOUNTER_PORTRAIT_DRAW_REGION);
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
        List<Point2D> pathPoints = Lists.newArrayList(
                new Point2D.Double(0.0, 0.0),
                new Point2D.Double(-0.054, 0.333),
                new Point2D.Double(-0.004, 0.892),
                new Point2D.Double(0.179, 1.0)
        );

        List<Point2D> bezierPoints = Lists.newArrayList(
                new Point2D.Double(0.004, 0.047),
                new Point2D.Double(-0.060, 0.193),
                new Point2D.Double(-0.083, 0.513),
                new Point2D.Double(0.006, 0.674),
                new Point2D.Double(0.088, 0.873),
                new Point2D.Double(0.047, 0.993)
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

        // second reverse the X-axis on the mapper function and reverse the draw process
        // this has the effect or creating a left/right mirror region
        mapIntoRegionFunction = MarkupUtils.createRatioIntoDrawRegionMapperInvertX(BODY_DRAW_REGION);

        // skip the 'last' point in the list as we are already at that point
        // move across to the bottom right corner
        Point2D startSecondPassPoint = mapIntoRegionFunction.apply(pathPointIterator.previous());
        path2D.lineTo(startSecondPassPoint.getX(), startSecondPassPoint.getY());

        while (pathPointIterator.hasPrevious()) {
            Point2D nextPoint = mapIntoRegionFunction.apply(pathPointIterator.previous());

            Point2D firstBezierPoint = mapIntoRegionFunction.apply(bezierPointIterator.previous());
            Point2D secondBezierPoint = mapIntoRegionFunction.apply(bezierPointIterator.previous());

            path2D.curveTo(firstBezierPoint.getX(), firstBezierPoint.getY(), secondBezierPoint.getX(), secondBezierPoint.getY(), nextPoint.getX(), nextPoint.getY());
        }

        return new PageShape.GeometricShape(path2D, BODY_DRAW_REGION);
    }
}
