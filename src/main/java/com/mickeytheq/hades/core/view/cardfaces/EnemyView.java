package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.MarkupRenderer;
import ca.cgjennings.layout.PageShape;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Enemy;
import com.mickeytheq.hades.core.model.common.WeaknessType;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.component.StatisticComponent;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.util.shape.DimensionEx;
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
import java.util.stream.IntStream;

@View(interfaceLanguageKey = InterfaceConstants.ENEMY)
public class EnemyView extends BaseCardFaceView<Enemy> implements HasCollectionView, HasEncounterSetView {
    private CommonCardFieldsView commonCardFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private PortraitView portraitView;

    private JComboBox<WeaknessType> weaknessTypeEditor;
    private StatisticComponent combatEditor;
    private StatisticComponent healthEditor;
    private StatisticComponent evadeEditor;
    private JComboBox<Integer> damageEditor;
    private JComboBox<Integer> horrorEditor;

    // locations to draw portraits
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(29.38, 46.40, 4.74, 4.74);
    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(0.00, 40.81, 63.50, 48.09);

    // locations to draw other elements
    private static final RectangleEx LABEL_DRAW_REGION = RectangleEx.millimetres(26.29, 51.90, 10.33, 2.37);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetres(10.50, 0.85, 42.67, 4.57);
    private static final RectangleEx SUBTITLE_DRAW_REGION = RectangleEx.millimetres(6.77, 5.84, 49.61, 3.39);
    private static final RectangleEx WEAKNESS_SUBTYPE_DRAW_REGION = RectangleEx.millimetres(6.77, 5.84, 49.61, 3.39);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimetres(3.39, 17.61, 57.23, 27.94);
    private static final PageShape BODY_PAGE_SHAPE = createBodyPageShape();

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel(), this);
        collectionView = new CollectionView(getModel().getCollectionModel(), this);
        encounterSetView = new EncounterSetView(getModel().getEncounterSetModel(), this);
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
        return StringUtils.defaultIfEmpty(getModel().getCommonCardFieldsModel().getTitle(), null);
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);
        collectionView.createEditors(editorContext);
        encounterSetView.createEditors(editorContext);
        portraitView.createEditors(editorContext);

        combatEditor = new StatisticComponent();
        healthEditor = new StatisticComponent();
        evadeEditor = new StatisticComponent();
        damageEditor = createEnemyAttackComboBox();
        horrorEditor = createEnemyAttackComboBox();

        weaknessTypeEditor = EditorUtils.createEnumComboBoxNullable(WeaknessType.class);

        EditorUtils.bindStatisticComponent(combatEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getEnemyFieldsModel()::setCombat));
        EditorUtils.bindStatisticComponent(healthEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getEnemyFieldsModel()::setHealth));
        EditorUtils.bindStatisticComponent(evadeEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getEnemyFieldsModel()::setEvade));
        EditorUtils.bindComboBox(damageEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getEnemyFieldsModel()::setDamage));
        EditorUtils.bindComboBox(horrorEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getEnemyFieldsModel()::setHorror));
        EditorUtils.bindComboBox(weaknessTypeEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getEnemyFieldsModel()::setWeaknessType));

        combatEditor.setStatistic(getModel().getEnemyFieldsModel().getCombat());
        healthEditor.setStatistic(getModel().getEnemyFieldsModel().getHealth());
        evadeEditor.setStatistic(getModel().getEnemyFieldsModel().getEvade());
        damageEditor.setSelectedItem(getModel().getEnemyFieldsModel().getDamage());
        horrorEditor.setSelectedItem(getModel().getEnemyFieldsModel().getHorror());
        weaknessTypeEditor.setSelectedItem(getModel().getEnemyFieldsModel().getWeaknessType());

        // title
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, true, true, false);

        // stats
        MigLayout migLayout = MigLayoutUtils.createTwoColumnLayout();

        JPanel statsPanel = new JPanel(migLayout);
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats"));

        // first column
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.WEAKNESS_TYPE));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.COMBAT));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.HEALTH));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.EVADE));

        // second column
        MigLayoutUtils.addComponentGrowXPushX(statsPanel, weaknessTypeEditor, "newline");
        MigLayoutUtils.addComponentGrowXPushX(statsPanel, combatEditor);
        MigLayoutUtils.addComponentGrowXPushX(statsPanel, healthEditor);
        MigLayoutUtils.addComponentGrowXPushX(statsPanel, evadeEditor);

        // third column
        statsPanel.add(new JPanel(), "newline"); // spacer
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.DAMAGE));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.HORROR));

        // fourth column
        statsPanel.add(new JPanel(), "newline"); // spacer
        MigLayoutUtils.addComponentGrowXPushX(statsPanel, damageEditor);
        MigLayoutUtils.addComponentGrowXPushX(statsPanel, horrorEditor);

        JPanel rulesPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.RULES));
        commonCardFieldsView.addNonTitleEditorsToPanel(rulesPanel, true);

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(
                titlePanel,
                statsPanel,
                rulesPanel,
                portraitView.createStandardArtPanel(editorContext)
        );

        // add the panel to the main tab control
        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    private JComboBox<Integer> createEnemyAttackComboBox() {
        JComboBox<Integer> comboBox = new JComboBox<>();
        IntStream.rangeClosed(0, 5).forEach(comboBox::addItem);
        return comboBox;
    }

    @Override
    protected List<TemplateInfo> getAvailableTemplateInfos() {
        return TemplateInfos.createStandard300And600(getTemplateResourcePrefix(), CardFaceOrientation.Portrait);
    }

    private String getTemplateResourcePrefix() {
        StringBuilder sb = new StringBuilder();
        sb.append("/templates/enemy/enemy");

        WeaknessType weaknessType = getModel().getEnemyFieldsModel().getWeaknessType();
        if (weaknessType != null) {
            sb.append("_weakness");

            switch (weaknessType) {
                case Basic:
                case Story:
                    sb.append("_encounter");
                    break;

                case Investigator:
                    sb.append("_investigator");
                    break;

                default:
                    break;
            }
        } else if (!StringUtils.isEmpty(getModel().getCommonCardFieldsModel().getSubtitle())) {
            sb.append("_subtitle");
        }

        return sb.toString();
    }

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION));

        // draw the template
        paintContext.paintTemplate();

        paintContext.setRenderingIncludeBleedRegion(false);

        // label
        PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_ENEMY).toUpperCase());

        // title
        commonCardFieldsView.paintTitle(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION));

        if (getModel().getEnemyFieldsModel().getWeaknessType() == null)
            paintNonWeaknessContent(paintContext);
        else
            paintWeaknessContent(paintContext);

        paintStatistics(paintContext);

        portraitView.paintArtist(paintContext);
    }

    private void paintNonWeaknessContent(PaintContext paintContext) {
        encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));
        encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Portrait);
        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Portrait);

        commonCardFieldsView.paintSubtitle(paintContext, paintContext.toPixelRect(SUBTITLE_DRAW_REGION));

        commonCardFieldsView.paintBodyAndCopyright(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION), BODY_PAGE_SHAPE);
    }

    private void paintWeaknessContent(PaintContext paintContext) {
        // draw the weakness overlay
        // this is the circular area either the standard basic weakness icon goes in or the encounter icon
        // for story weaknesses
        WeaknessType weaknessType = getModel().getEnemyFieldsModel().getWeaknessType();

        if (weaknessType == WeaknessType.Basic || weaknessType == WeaknessType.Story) {
            if (weaknessType == WeaknessType.Basic) {
                ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImageReadOnly(ImageUtils.BASIC_WEAKNESS_ICON_RESOURCE), paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));
            } else {
                encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));
                encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Portrait);
            }
        }

        // the weakness/basic weakness fixed text
        String subTypeText = Language.gstring(GameConstants.LABEL_WEAKNESS);

        if (weaknessType == WeaknessType.Basic)
            subTypeText = Language.gstring(GameConstants.LABEL_BASICWEAKNESS);

        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getSubTypeTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(subTypeText.toUpperCase());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), paintContext.toPixelRect(WEAKNESS_SUBTYPE_DRAW_REGION));

        commonCardFieldsView.paintBodyAndCopyright(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION), BODY_PAGE_SHAPE);

        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Portrait);
    }

    private static final RectangleEx COMBAT_DRAW_REGION = RectangleEx.millimetres(20.15, 9.82, 0.00, 2.96);
    private static final RectangleEx HEALTH_DRAW_REGION = RectangleEx.millimetres(31.83, 9.82, 0.00, 2.96);
    private static final RectangleEx EVADE_DRAW_REGION = RectangleEx.millimetres(43.77, 9.82, 0.00, 2.96);

    private static final DimensionEx DAMAGE_ICON_SIZE = DimensionEx.millimetres(3.22, 3.29);
    private static final DimensionEx HORROR_ICON_SIZE = DAMAGE_ICON_SIZE;

    private static final List<RectangleEx> DAMAGE_DRAW_REGIONS = Lists.newArrayList(
            RectangleEx.millimetres(22.35, 49.00, DAMAGE_ICON_SIZE),
            RectangleEx.millimetres(18.46, 47.91, DAMAGE_ICON_SIZE),
            RectangleEx.millimetres(14.56, 46.38, DAMAGE_ICON_SIZE),
            RectangleEx.millimetres(11.01, 44.35, DAMAGE_ICON_SIZE),
            RectangleEx.millimetres(7.62, 41.98, DAMAGE_ICON_SIZE)
    );

    private static final List<RectangleEx> HORROR_DRAW_REGIONS = Lists.newArrayList(
            RectangleEx.millimetres(37.42, 49.00, HORROR_ICON_SIZE),
            RectangleEx.millimetres(41.32, 47.91, HORROR_ICON_SIZE),
            RectangleEx.millimetres(45.21, 46.38, HORROR_ICON_SIZE),
            RectangleEx.millimetres(48.77, 44.35, HORROR_ICON_SIZE),
            RectangleEx.millimetres(52.15, 41.98, HORROR_ICON_SIZE)
    );

    private void paintStatistics(PaintContext paintContext) {
        PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(COMBAT_DRAW_REGION), getModel().getEnemyFieldsModel().getCombat(), Color.BLACK, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);
        PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(HEALTH_DRAW_REGION), getModel().getEnemyFieldsModel().getHealth(), Color.BLACK, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);
        PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(EVADE_DRAW_REGION), getModel().getEnemyFieldsModel().getEvade(), Color.BLACK, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);

        BufferedImage damageIcon = ImageUtils.loadImageReadOnly("/overlays/damage.png");
        BufferedImage horrorIcon = ImageUtils.loadImageReadOnly("/overlays/horror.png");

        IntStream.rangeClosed(1, getModel().getEnemyFieldsModel().getDamage()).forEach(o -> {
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), damageIcon, paintContext.toPixelRect(DAMAGE_DRAW_REGIONS.get(o - 1)));
        });

        IntStream.rangeClosed(1, getModel().getEnemyFieldsModel().getHorror()).forEach(o -> {
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), horrorIcon, paintContext.toPixelRect(HORROR_DRAW_REGIONS.get(o - 1)));
        });
    }

    private static PageShape createBodyPageShape() {
        Rectangle bodyRectangle = BODY_DRAW_REGION.toPixelRectangle(CardFaceViewUtils.HARDCODED_DPI);

        MarkupUtils.PageShapeBuilder pageShapeBuilder = MarkupUtils.createPageShapeBuilder(bodyRectangle);

        List<Point2D> pathPoints = Lists.newArrayList(
                new Point2D.Double(0.086, 0.0),
                new Point2D.Double(0.086, 0.189),
                new Point2D.Double(0, 0.189),
                new Point2D.Double(0, 0.693),
                new Point2D.Double(0.039, 0.800),
                new Point2D.Double(0.078, 1.000)
        );

        ListIterator<Point2D> pathPointIterator = pathPoints.listIterator();

        pageShapeBuilder.moveTo(pathPointIterator.next());

        // first draw the left side
        while (pathPointIterator.hasNext()) {
            pageShapeBuilder.lineTo(pathPointIterator.next());
        }

        // second reverse the X-axis on the mapper function and reverse the draw process
        // this has the effect or creating a left/right mirror region
        pageShapeBuilder.setMapToDrawRegionCoordinatesFunction(MarkupUtils.createRatioIntoDrawRegionMapperInvertX(bodyRectangle));

        // the first previous() will return the same point as the last call to next() but with the mapper inverted it will
        // draw across to the other side of the desired shape to begin the mirroring
        while (pathPointIterator.hasPrevious()) {
            pageShapeBuilder.lineTo(pathPointIterator.previous());
        }

        return pageShapeBuilder.build();
    }
}
