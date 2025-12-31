package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.PageShape;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.LocationBack;
import com.mickeytheq.hades.core.view.BaseCardFaceView;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.View;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.core.view.utils.MarkupUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.PaintUtils;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.function.Function;

@View(interfaceLanguageKey = InterfaceConstants.LOCATION)
public class LocationBackView extends BaseCardFaceView<LocationBack> implements HasLocationFieldsView, HasEncounterSetView, HasCollectionView {
    private CommonCardFieldsView commonCardFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private PortraitWithArtistView portraitWithArtistView;
    private LocationFieldsView locationFieldsView;

    private static final Rectangle ART_PORTRAIT_DRAW_REGION = new Rectangle(0, 84, 750, 550);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        portraitWithArtistView = new PortraitWithArtistView(getModel().getPortraitWithArtistModel(), ART_PORTRAIT_DRAW_REGION.getSize());
        locationFieldsView = new LocationFieldsView(getModel().getLocationFieldsModel(), this);
    }

    @Override
    public LocationFieldsView getLocationsFieldView() {
        return locationFieldsView;
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

        portraitWithArtistView.createEditors(editorContext);

        locationFieldsView.createEditors(editorContext);

        layoutTitleAndConnectionsEditors(editorContext);
        createRulesAndPortraitTab(editorContext);

        collectionView.createEditors(editorContext);
        encounterSetView.createEditors(editorContext);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    private void createRulesAndPortraitTab(EditorContext editorContext) {
        JPanel generalPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.GENERAL));
        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel, false);

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();

        mainPanel.add(generalPanel, "wrap, pushx, growx");
        mainPanel.add(portraitWithArtistView.createStandardArtPanel(editorContext), "wrap, pushx, growx");

        // add the panel to the main tab control
        editorContext.addDisplayComponent("Rules / portrait", mainPanel); // TODO: i18n
    }

    private void layoutTitleAndConnectionsEditors(EditorContext editorContext) {
        // title
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, true);

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();
        mainPanel.add(titlePanel, "wrap, growx, pushx");
        mainPanel.add(locationFieldsView.createConnectionsPanel(), "wrap, growx, pushx");

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);
    }

    private static final Rectangle LABEL_DRAW_REGION = new Rectangle(276, 556, 200, 28);
    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(130, 8, 484, 58);
    private static final Rectangle SUBTITLE_DRAW_REGION = new Rectangle(190, 78, 382, 42);
    private static final Rectangle BODY_DRAW_REGION = new Rectangle(40, 600, 672, 286);
    private static final PageShape BODY_PAGE_SHAPE = MarkupUtils.createStraightLinePathingPageShape(BODY_DRAW_REGION,
            Lists.newArrayList(
                    new Point2D.Double(0.111, 0.000),
                    new Point2D.Double(0.0, 0.204),
                    new Point2D.Double(0.0, 1.0),
                    new Point2D.Double(1.0, 1.0),
                    new Point2D.Double(1.0, 0.204),
                    new Point2D.Double(0.889, 0.0)
            ));
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(348, 490, 56, 56);
    private static final Rectangle COLLECTION_PORTRAIT_DRAW_REGION = new Rectangle(640, 1020, 26, 26);

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitWithArtistView.paintArtPortrait(paintContext, ART_PORTRAIT_DRAW_REGION);

        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        PaintUtils.paintLabel(paintContext, LABEL_DRAW_REGION, Language.gstring(GameConstants.LABEL_LOCATION).toUpperCase());

        commonCardFieldsView.paintTitles(paintContext, TITLE_DRAW_REGION, SUBTITLE_DRAW_REGION);

        commonCardFieldsView.paintBodyAndCopyright(paintContext, BODY_DRAW_REGION, BODY_PAGE_SHAPE);

        encounterSetView.paintEncounterNumbers(paintContext);
        encounterSetView.paintEncounterPortrait(paintContext, ENCOUNTER_PORTRAIT_DRAW_REGION);

        portraitWithArtistView.paintArtist(paintContext);

        collectionView.paintCollectionPortrait(paintContext, COLLECTION_PORTRAIT_DRAW_REGION, true);
        collectionView.paintCollectionNumber(paintContext);

        locationFieldsView.paintLocationIcons(paintContext);
    }
}
