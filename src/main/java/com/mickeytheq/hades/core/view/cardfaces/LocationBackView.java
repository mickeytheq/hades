package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.PageShape;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.LocationBack;
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
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

@View(interfaceLanguageKey = InterfaceConstants.LOCATION_BACK)
public class LocationBackView extends BaseCardFaceView<LocationBack> implements HasLocationFieldsView {
    private CommonCardFieldsView commonCardFieldsView;
    private PortraitView portraitView;
    private LocationFieldsView locationFieldsView;

    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(0.00, 7.11, 63.50, 46.57);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), this);
        portraitView = PortraitView.createWithDefaultImage(getModel().getPortraitModel(), ART_PORTRAIT_DRAW_REGION.toPixelRectangle(CardFaceViewUtils.HARDCODED_DPI).getSize());
        locationFieldsView = new LocationFieldsView(getModel().getLocationFieldsModel(), this);
    }

    @Override
    public LocationFieldsView getLocationsFieldView() {
        return locationFieldsView;
    }

    @Override
    protected BufferedImage getTemplateImage() {
        String templatePath = "/templates/location/location_back";

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
    }

    private void createRulesAndPortraitTab(EditorContext editorContext) {
        JPanel generalPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.GENERAL));
        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel, false);

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(
                generalPanel,
                portraitView.createStandardArtPanel(editorContext));

        // add the panel to the main tab control
        editorContext.addDisplayComponent("Rules / portrait", mainPanel); // TODO: i18n
    }

    private void layoutTitleAndConnectionsEditors(EditorContext editorContext) {
        // title
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, true, true);

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();
        mainPanel.add(titlePanel, "wrap, growx, pushx");
        mainPanel.add(locationFieldsView.createConnectionsPanel(), "wrap, growx, pushx");

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);
    }

    private static final RectangleEx LABEL_DRAW_REGION = RectangleEx.millimetres(23.37, 47.07, 16.93, 2.37);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetres(11.01, 0.68, 40.98, 4.91);
    private static final RectangleEx SUBTITLE_DRAW_REGION = RectangleEx.millimetres(16.09, 6.60, 32.34, 3.56);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimetres(3.39, 50.80, 56.90, 24.21);
    private static final PageShape BODY_PAGE_SHAPE = MarkupUtils.createStraightLinePathingPageShape(BODY_DRAW_REGION.toPixelRectangle(CardFaceViewUtils.HARDCODED_DPI),
            Lists.newArrayList(
                    new Point2D.Double(0.111, 0.000),
                    new Point2D.Double(0.0, 0.204),
                    new Point2D.Double(0.0, 1.0),
                    new Point2D.Double(1.0, 1.0),
                    new Point2D.Double(1.0, 0.204),
                    new Point2D.Double(0.889, 0.0)
            ));
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = LocationView.ENCOUNTER_PORTRAIT_DRAW_REGION;

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION));

        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_LOCATION).toUpperCase());

        commonCardFieldsView.paintTitles(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION), paintContext.toPixelRect(SUBTITLE_DRAW_REGION));

        commonCardFieldsView.paintBodyAndCopyright(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION), BODY_PAGE_SHAPE);

        portraitView.paintArtist(paintContext);

        paintCollectionImage(paintContext);

        locationFieldsView.paintLocationIcons(paintContext);
    }

    private void paintCollectionImage(PaintContext paintContext) {
        // location backs don't have their own collection configuration and only the collection image is painted
        // paint the collection image on the other face
        getOtherFaceView()
                .filter(o -> o instanceof HasCollectionView)
                .map(o -> ((HasCollectionView) o).getCollectionView())
                .ifPresent(o -> o.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true));
    }
}
