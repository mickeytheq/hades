package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.MarkupRenderer;
import ca.cgjennings.layout.PageShape;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Location;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

@View(interfaceLanguageKey = InterfaceConstants.LOCATION)
public class LocationView extends BaseCardFaceView<Location> implements HasLocationFieldsView, HasCollectionView, HasEncounterSetView, HasArtPortraitView {
    private CommonCardFieldsView commonCardFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private PortraitView portraitView;
    private LocationFieldsView locationFieldsView;

    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(0.00, 7.11, 63.50, 46.57);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), this);
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        portraitView = PortraitView.createWithDefaultImage(getModel().getPortraitModel(), this, ART_PORTRAIT_DRAW_REGION);
        locationFieldsView = new LocationFieldsView(getModel().getLocationFieldsModel(), this);
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
    public LocationFieldsView getLocationsFieldView() {
        return locationFieldsView;
    }

    @Override
    public PortraitView getArtPortraitView() {
        return portraitView;
    }

    @Override
    protected List<TemplateInfo> getAvailableTemplateInfos() {
        return TemplateInfos.createStandard300And600(getTemplateResourcePrefix(), CardFaceOrientation.Portrait);
    }

    private String getTemplateResourcePrefix() {
        String templatePath = "/templates/location/location";

        boolean hasSubtitle = !StringUtils.isEmpty(getModel().getCommonCardFieldsModel().getSubtitle());

        if (hasSubtitle) {
            templatePath = templatePath + "_subtitle";
        }

        return templatePath;
    }

    @Override
    public String getTitle() {
        return StringUtils.defaultIfEmpty(commonCardFieldsView.getModel().getTitle(), null);
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);

        portraitView.createEditors(editorContext);

        locationFieldsView.createEditors(editorContext);

        layoutTitleAndConnectionsEditors(editorContext);
        createRulesAndPortraitTab(editorContext);

        collectionView.createEditors(editorContext);
        encounterSetView.createEditors(editorContext);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    private void createRulesAndPortraitTab(EditorContext editorContext) {
        JPanel generalPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.GENERAL));
        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel, true);

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(
                generalPanel,
                portraitView.createStandardArtPanel(editorContext)
        );

        // add the panel to the main tab control
        editorContext.addDisplayComponent("Rules / portrait", mainPanel); // TODO: i18n
    }

    private void layoutTitleAndConnectionsEditors(EditorContext editorContext) {
        // title
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, true, false);

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();
        mainPanel.add(titlePanel, "wrap, growx, pushx");
        mainPanel.add(locationFieldsView.createShroudCluesPanel(), "wrap, growx, pushx");
        mainPanel.add(locationFieldsView.createConnectionsPanel(), "wrap, growx, pushx");

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);
    }

    private static final RectangleEx LABEL_DRAW_REGION = RectangleEx.millimetres(23.37, 47.07, 16.93, 2.37);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetres(11.01, 0.68, 40.98, 4.91);
    private static final RectangleEx SUBTITLE_DRAW_REGION = RectangleEx.millimetres(16.09, 6.60, 32.34, 3.56);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimetres(3.39, 50.80, 56.90, 24.21);

    private static final LoadingCache<Integer, PageShape> BODY_PAGE_CACHE = CacheBuilder.newBuilder().build(CacheLoader.from(LocationView::createBodyPageShape));
    private static final RectangleEx VICTORY_DRAW_REGION = RectangleEx.millimetres(49.00, 74.5, 13.00, 4.00);
    public static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(29.40, 41.69, 4.74, 4.74);

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION));

        paintContext.paintTemplate();

        paintContext.setRenderingIncludeBleedRegion(false);

        PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_LOCATION).toUpperCase());

        commonCardFieldsView.paintTitles(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION), paintContext.toPixelRect(SUBTITLE_DRAW_REGION));

        // victory is painted separate from the body on locations
        commonCardFieldsView.paintBody(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION), BODY_PAGE_CACHE.getUnchecked(paintContext.getResolutionInPixelsPerInch()), false);
        paintVictory(paintContext);
        commonCardFieldsView.paintCopyright(paintContext);

        encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Portrait);
        encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));

        portraitView.paintArtist(paintContext);

        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Portrait);

        locationFieldsView.paintLocationIcons(paintContext);
        locationFieldsView.paintShroudAndClues(paintContext);
    }

    private static PageShape createBodyPageShape(int ppi) {
        Rectangle drawRegion = BODY_DRAW_REGION.toPixelRectangle(ppi);

        MarkupUtils.PageShapeBuilder pageShapeBuilder = MarkupUtils.createPageShapeBuilder(drawRegion);

        pageShapeBuilder.moveTo(new Point2D.Double(0.074, 0.0));
        pageShapeBuilder.curveTo(new Point2D.Double(0.037, 0.153), new Point2D.Double(0.107, 0.139), new Point2D.Double(0.0, 0.174));
        pageShapeBuilder.lineTo(new Point2D.Double(0.0, 1.0));
        pageShapeBuilder.lineTo(new Point2D.Double(1.0, 1.0));
        pageShapeBuilder.lineTo(new Point2D.Double(1.0, 0.319));
        pageShapeBuilder.curveTo(new Point2D.Double(0.991, 0.278), new Point2D.Double(0.962, 0.167), new Point2D.Double(0.951, 0.125));
        pageShapeBuilder.curveTo(new Point2D.Double(0.936, 0.132), new Point2D.Double(0.970, 0.174), new Point2D.Double(0.926, 0.0));
        pageShapeBuilder.lineTo(new Point2D.Double(0.074, 0.0));

        return pageShapeBuilder.build();
    }

    private void paintVictory(PaintContext paintContext) {
        String victory = getModel().getCommonCardFieldsModel().getVictory();

        if (StringUtils.isEmpty(victory))
            return;

        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getVictoryTextStyle());
        markupRenderer.setLineTightness(0.7f);
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_RIGHT | MarkupRenderer.LAYOUT_MIDDLE);
        markupRenderer.setMarkupText(getModel().getCommonCardFieldsModel().getVictory());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), paintContext.toPixelRect(VICTORY_DRAW_REGION));
    }
}

