package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.PageShape;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Location;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.core.view.utils.MarkupUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.PaintUtils;
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

@View(interfaceLanguageKey = InterfaceConstants.LOCATION)
public class LocationView extends BaseCardFaceView<Location> implements HasLocationFieldsView, HasCollectionView, HasEncounterSetView {
    private CommonCardFieldsView commonCardFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private PortraitView portraitView;
    private LocationFieldsView locationFieldsView;

    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimeters(0.00, 7.11, 63.50, 46.57);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        portraitView = PortraitView.createWithDefaultImage(getModel().getPortraitModel(), ART_PORTRAIT_DRAW_REGION.toPixelRectangle(CardFaceViewUtils.HARDCODED_DPI).getSize());
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
    protected BufferedImage getTemplateImage() {
        String templatePath = "/templates/location/location";

        boolean hasSubtitle = !StringUtils.isEmpty(getModel().getCommonCardFieldsModel().getSubtitle());

        if (hasSubtitle) {
            templatePath = templatePath + "_subtitle";
        }

        templatePath = templatePath + ".png";

        return ImageUtils.loadImage(templatePath);
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
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, true);

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();
        mainPanel.add(titlePanel, "wrap, growx, pushx");
        mainPanel.add(locationFieldsView.createShroudCluesPanel(), "wrap, growx, pushx");
        mainPanel.add(locationFieldsView.createConnectionsPanel(), "wrap, growx, pushx");

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);
    }

    private static final RectangleEx LABEL_DRAW_REGION = RectangleEx.millimeters(23.37, 47.07, 16.93, 2.37);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimeters(11.01, 0.68, 40.98, 4.91);
    private static final RectangleEx SUBTITLE_DRAW_REGION = RectangleEx.millimeters(16.09, 6.60, 32.34, 3.56);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimeters(3.39, 50.80, 56.90, 24.21);
    private static final PageShape BODY_PAGE_SHAPE = createBodyPageShape(BODY_DRAW_REGION.toPixelRectangle(CardFaceViewUtils.HARDCODED_DPI));
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimeters(29.46, 41.49, 4.74, 4.74);

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION));

        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_LOCATION).toUpperCase());

        commonCardFieldsView.paintTitles(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION), paintContext.toPixelRect(SUBTITLE_DRAW_REGION));

        commonCardFieldsView.paintBodyAndCopyright(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION), BODY_PAGE_SHAPE);

        encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Portrait);
        encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));

        portraitView.paintArtist(paintContext);

        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Portrait);

        locationFieldsView.paintLocationIcons(paintContext);
        locationFieldsView.paintShroudAndClues(paintContext);
    }

    private static PageShape createBodyPageShape(Rectangle drawRegion) {
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
}

