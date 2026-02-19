package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.PageShape;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.model.cardfaces.Event;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.core.model.common.PlayerCardClass;
import com.mickeytheq.hades.core.model.common.PlayerCardType;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.util.shape.RectangleEx;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ListIterator;

@View(interfaceLanguageKey = InterfaceConstants.EVENT)
public class EventView extends BaseCardFaceView<Event> implements HasCollectionView, HasEncounterSetView {
    private CommonCardFieldsView commonCardFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private PlayerCardFieldsView playerCardFieldsView;
    private PortraitView portraitView;

    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(0.00, 0.00, 69.50, 54.77);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), this);
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        playerCardFieldsView = new PlayerCardFieldsView(getModel().getPlayerCardFieldsModel(), true);
        portraitView = PortraitView.createWithDefaultImage(getModel().getPortraitModel(), this, ART_PORTRAIT_DRAW_REGION);
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
    protected List<TemplateInfo> getAvailableTemplateInfos() {
        return TemplateInfos.createStandard300And600(getTemplateResourcePrefix(), CardFaceOrientation.Portrait);
    }

    private String getTemplateResourcePrefix() {
        return "/templates/event/event_" + playerCardFieldsView.getDefaultResourcePrefix();
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);

        portraitView.createEditors(editorContext);

        createTitleAndStatisticsEditors(editorContext);

        createRulesAndPortraitTab(editorContext);

        collectionView.createEditors(editorContext);
        encounterSetView.createEditors(editorContext);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    private void createTitleAndStatisticsEditors(EditorContext editorContext) {
        // title
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false, false);

        playerCardFieldsView.createEditors(editorContext);

        MigLayout migLayout = MigLayoutUtils.createTwoColumnLayout();

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

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(
                generalPanel,
                portraitView.createStandardArtPanel(editorContext)
        );

        // add the panel to the main tab control
        editorContext.addDisplayComponent("Rules / portrait", mainPanel); // TODO: i18n
    }

    private static final RectangleEx LABEL_DRAW_REGION = RectangleEx.millimetres(3.56, 10.67, 6.10, 2.37);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetresHorizontallyCentred(51.18, 50.29, 4.91);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimetres(7.00, 57.23, 48.77, 26.75);
    private static final RectangleEx BODY_WEAKNESS_DRAW_REGION = RectangleEx.millimetres(7.00, 59.61, 48.77, 24.21);

    private static final RectangleEx WEAKNESS_LABEL_DRAW_REGION = RectangleEx.millimetresHorizontallyCentred(56.39, 34.37, 2.54);
    private static final RectangleEx ENCOUNTER_SET_CIRCLE_WEAKNESS_DRAW_REGION = RectangleEx.millimetresHorizontallyCentred(41.03, PaintConstants.ENCOUNTER_SET_CIRCLE_OVERLAY_SIZE);
    private static final RectangleEx BASIC_WEAKNESS_ICON_DRAW_REGION = ENCOUNTER_SET_CIRCLE_WEAKNESS_DRAW_REGION.centreOn(PaintConstants.BASIC_WEAKNESS_ICON_SIZE);
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = ENCOUNTER_SET_CIRCLE_WEAKNESS_DRAW_REGION.centreOn(PaintConstants.ENCOUNTER_SET_ICON_SIZE);

    private static final LoadingCache<Integer, PageShape> BODY_PAGE_CACHE = CacheBuilder.newBuilder().build(CacheLoader.from(EventView::createBodyPageShape));

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION));

        // draw the template
        paintContext.paintTemplate();

        paintContext.setRenderingIncludeBleedRegion(false);

        // label
        PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_EVENT).toUpperCase());

        // title
        commonCardFieldsView.paintTitle(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION));

        Rectangle bodyDrawRegion = getBodyDrawRegion(paintContext);
        commonCardFieldsView.paintBodyAndCopyright(paintContext, bodyDrawRegion, BODY_PAGE_CACHE.getUnchecked(paintContext.getResolutionInPixelsPerInch()));

        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Portrait);

        portraitView.paintArtist(paintContext);

        // player card icons
        paintClassSymbols(paintContext);

        playerCardFieldsView.paintEncounterSetIconCircle(paintContext, paintContext.toPixelRect(ENCOUNTER_SET_CIRCLE_WEAKNESS_DRAW_REGION));

        paintEncounterContent(paintContext);

        // weakness labels
        paintWeaknessContent(paintContext);

        playerCardFieldsView.paintLevel(paintContext);

        playerCardFieldsView.paintCost(paintContext);

        playerCardFieldsView.paintSkillIcons(paintContext);
    }

    private Rectangle getBodyDrawRegion(PaintContext paintContext) {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getCardType();

        if (playerCardType.isWeakness())
            return paintContext.toPixelRect(BODY_WEAKNESS_DRAW_REGION);

        return paintContext.toPixelRect(BODY_DRAW_REGION);
    }

    // regions are from right to left
    private static final List<RectangleEx> CLASS_SYMBOL_REGIONS_PAIR = Lists.newArrayList(
            RectangleEx.millimetres(24.38, 43.35, 7.62, 7.62),
            RectangleEx.millimetres(31.67, 43.35, 7.62, 7.62)
    );

    private static final List<RectangleEx> CLASS_SYMBOL_REGIONS_TRIPLE = Lists.newArrayList(
            RectangleEx.millimetres(20.83, 43.35, 7.62, 7.62),
            RectangleEx.millimetres(28.11, 43.35, 7.62, 7.62),
            RectangleEx.millimetres(35.39, 43.35, 7.62, 7.62)
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
        List<RectangleEx> drawRegions;

        if (playerCardClasses.size() == 2)
            drawRegions = CLASS_SYMBOL_REGIONS_PAIR;
        else
            drawRegions = CLASS_SYMBOL_REGIONS_TRIPLE;

        for (int i = 0; i < playerCardClasses.size(); i++) {
            PlayerCardClass playerCardClass = playerCardClasses.get(i);

            BufferedImage classSymbol = ImageUtils.loadImageReadOnly(getClass().getResource("/overlays/class_symbol_" + playerCardClass.name().toLowerCase() + ".png"));

            Rectangle rectangle = paintContext.toPixelRect(drawRegions.get(i));

            PaintUtils.paintBufferedImage(paintContext.getGraphics(), classSymbol, rectangle);
        }
    }

    private void paintEncounterContent(PaintContext paintContext) {
        if (!getModel().getPlayerCardFieldsModel().getCardType().isHasEncounterDetails())
            return;

        encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Portrait);
        encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));
    }

    private void paintWeaknessContent(PaintContext paintContext) {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getCardType();

        if (playerCardType == PlayerCardType.Weakness || playerCardType == PlayerCardType.StoryWeakness) {
            PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(WEAKNESS_LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_WEAKNESS).toUpperCase());
        } else if (playerCardType == PlayerCardType.BasicWeakness) {
            PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(WEAKNESS_LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_BASICWEAKNESS).toUpperCase());
            ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImageReadOnly(ImageUtils.BASIC_WEAKNESS_ICON_RESOURCE), paintContext.toPixelRect(BASIC_WEAKNESS_ICON_DRAW_REGION));
        }
    }

    private static PageShape createBodyPageShape(int resolutionPpi) {
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

        MarkupUtils.PageShapeBuilder pageShapeBuilder = new MarkupUtils.PageShapeBuilder(BODY_DRAW_REGION.toPixelRectangle(resolutionPpi));

        ListIterator<Point2D> pathPointIterator = pathPoints.listIterator();
        ListIterator<Point2D> bezierPointIterator = bezierPoints.listIterator();

        pageShapeBuilder.moveTo(pathPointIterator.next());

        // first draw the curves in one direction
        while (pathPointIterator.hasNext()) {
            pageShapeBuilder.curveTo(bezierPointIterator.next(), bezierPointIterator.next(), pathPointIterator.next());
        }

        // second reverse the X-axis on the mapper function and reverse the draw process
        // this has the effect or creating a left/right mirror region
        pageShapeBuilder.setMapToDrawRegionCoordinatesFunction(MarkupUtils.createRatioIntoDrawRegionMapperInvertX(BODY_DRAW_REGION.toPixelRectangle(resolutionPpi)));

        // move across to the bottom right corner
        pageShapeBuilder.lineTo(pathPointIterator.previous());

        while (pathPointIterator.hasPrevious()) {
            pageShapeBuilder.curveTo(bezierPointIterator.previous(), bezierPointIterator.previous(), pathPointIterator.previous());
        }

        return pageShapeBuilder.build();
    }
}
