package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.PageShape;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Skill;
import com.mickeytheq.hades.core.model.common.PlayerCardType;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.core.view.utils.MarkupUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.PaintUtils;
import com.mickeytheq.hades.util.shape.RectangleEx;
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

@View(interfaceLanguageKey = InterfaceConstants.SKILL)
public class SkillView extends BaseCardFaceView<Skill> implements HasCollectionView, HasEncounterSetView {
    private CommonCardFieldsView commonCardFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private PlayerCardFieldsView playerCardFieldsView;
    private PortraitView portraitView;

    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimeters(2.37, 6.77, 58.93, 50.46);
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimeters(29.46, 43.77, 4.74, 4.74);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
        playerCardFieldsView = new PlayerCardFieldsView(getModel().getPlayerCardFieldsModel(), false);
        portraitView = PortraitView.createWithDefaultImage(getModel().getPortraitModel(), ART_PORTRAIT_DRAW_REGION.toPixelRectangle(CardFaceViewUtils.HARDCODED_DPI).getSize());
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
        return StringUtils.defaultIfEmpty(getModel().getCommonCardFieldsModel().getTitle(), null);
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
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, false, false);

        playerCardFieldsView.createEditors(editorContext);

        MigLayout migLayout = MigLayoutUtils.createTwoColumnLayout();

        JPanel statsPanel = new JPanel(migLayout);
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats")); // TODO: i18n

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(
                titlePanel,
                statsPanel
        );

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

    private static final RectangleEx LABEL_DRAW_REGION = RectangleEx.millimeters(3.56, 10.67, 6.43, 2.37);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimeters(12.36, 2.71, 49.78, 4.91);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimeters(6.27, 59.44, 50.97, 24.55);
    private static final RectangleEx BODY_WEAKNESS_DRAW_REGION = RectangleEx.millimeters(6.27, 62.65, 50.97, 21.17);
    private static final RectangleEx WEAKNESS_LABEL_DRAW_REGION = RectangleEx.millimeters(14.56, 58.93, 34.37, 2.54);
    private static final RectangleEx BASIC_WEAKNESS_ICON_DRAW_REGION = RectangleEx.millimeters(54.86, 2.20, 5.08, 5.08);
    private static final RectangleEx BASIC_WEAKNESS_OVERLAY_DRAW_REGION = RectangleEx.millimeters(52.83, 0.51, 9.14, 8.97);

    private static final PageShape BODY_PAGE_SHAPE = createBodyPageShape();

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION));

        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // label
        PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_SKILL).toUpperCase());

        // title - skill titles are left aligned
        PaintUtils.paintTitleLeftAlign(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION), getModel().getCommonCardFieldsModel().getTitle(), getModel().getCommonCardFieldsModel().isUnique());

        Rectangle bodyDrawRegion = getBodyDrawRegion(paintContext);
        commonCardFieldsView.paintBodyAndCopyright(paintContext, bodyDrawRegion, BODY_PAGE_SHAPE);

        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Portrait);

        portraitView.paintArtist(paintContext);

        paintEncounterContent(paintContext);

        // weakness labels
        paintWeaknessContent(paintContext);

        playerCardFieldsView.paintLevel(paintContext);

        playerCardFieldsView.paintSkillIcons(paintContext);
    }

    private Rectangle getBodyDrawRegion(PaintContext paintContext) {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();

        if (playerCardType.isWeakness())
            return paintContext.toPixelRect(BODY_WEAKNESS_DRAW_REGION);

        return paintContext.toPixelRect(BODY_DRAW_REGION);
    }

    private void paintEncounterContent(PaintContext paintContext) {
        if (!getModel().getPlayerCardFieldsModel().getPlayerCardType().isHasEncounterDetails())
            return;

        paintEncounterOrBasicWeaknessOverlay(paintContext);

        encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Portrait);
        encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));
    }

    private void paintEncounterOrBasicWeaknessOverlay(PaintContext paintContext) {
        ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImage(getClass().getResource("/overlays/encounter_event.png")), paintContext.toPixelRect(BASIC_WEAKNESS_OVERLAY_DRAW_REGION));
    }

    private void paintWeaknessContent(PaintContext paintContext) {
        PlayerCardType playerCardType = getModel().getPlayerCardFieldsModel().getPlayerCardType();

        if (playerCardType == PlayerCardType.Weakness || playerCardType == PlayerCardType.StoryWeakness) {
            PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(WEAKNESS_LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_WEAKNESS).toUpperCase());
        } else if (playerCardType == PlayerCardType.BasicWeakness) {
            PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(WEAKNESS_LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_BASICWEAKNESS).toUpperCase());
            paintEncounterOrBasicWeaknessOverlay(paintContext);
            ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImage(ImageUtils.BASIC_WEAKNESS_ICON_RESOURCE), paintContext.toPixelRect(BASIC_WEAKNESS_ICON_DRAW_REGION));
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

        Rectangle bodyRectangle = BODY_DRAW_REGION.toPixelRectangle(CardFaceViewUtils.HARDCODED_DPI);

        MarkupUtils.PageShapeBuilder pageShapeBuilder = MarkupUtils.createPageShapeBuilder(bodyRectangle);

        ListIterator<Point2D> pathPointIterator = pathPoints.listIterator();
        ListIterator<Point2D> bezierPointIterator = bezierPoints.listIterator();

        pageShapeBuilder.moveTo(pathPointIterator.next());

        // first draw the curves in one direction
        while (pathPointIterator.hasNext()) {
            pageShapeBuilder.curveTo(bezierPointIterator.next(), bezierPointIterator.next(), pathPointIterator.next());
        }

        // second pass - the skill area is shaped like a scroll. translate the X coordinate to the other side
        // of the body region and repeat the curve there - remember 1.0d is a ratio so this is the entire region's width
        pageShapeBuilder.setMapToDrawRegionCoordinatesFunction(MarkupUtils.createRatioIntoDrawRegionMapperTranslateX(bodyRectangle, 1.0d));

        // move across to the bottom right corner
        pageShapeBuilder.lineTo(pathPointIterator.previous());

        // draw the curve
        while (pathPointIterator.hasPrevious()) {
            pageShapeBuilder.curveTo(bezierPointIterator.previous(), bezierPointIterator.previous(), pathPointIterator.previous());
        }

        return pageShapeBuilder.build();
    }
}
