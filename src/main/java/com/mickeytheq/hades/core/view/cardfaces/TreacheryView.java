package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.MarkupRenderer;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.model.cardfaces.Treachery;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.core.model.common.WeaknessType;
import com.mickeytheq.hades.codegenerated.GameConstants;
import com.mickeytheq.hades.util.shape.RectangleEx;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.net.URL;
import java.util.List;

@View(interfaceLanguageKey = InterfaceConstants.TREACHERY)
public class TreacheryView extends BaseCardFaceView<Treachery> implements HasCollectionView, HasEncounterSetView {
    private static final String STANDARD_TEMPLATE_RESOURCE_PREFIX = "/templates/treachery/treachery";
    private static final String WEAKNESS_TEMPLATE_RESOURCE_PREFIX = "/templates/treachery/weakness_treachery";
    private static final URL BASIC_WEAKNESS_OVERLAY_RESOURCE = Treachery.class.getResource("/overlays/encounter_asset.png");

    private JComboBox<WeaknessType> weaknessTypeEditor;
    private CommonCardFieldsView commonCardFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private PortraitView portraitView;

    // locations to draw portraits
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(29.63, 43.01, 4.74, 4.74);
    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(2.88, 0.00, 58.25, 50.46);

    // locations to draw other elements
    private static final RectangleEx LABEL_DRAW_REGION = RectangleEx.millimetres(23.20, 48.43, 17.61, 2.37);
    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetres(6.60, 51.99, 50.63, 4.91);
    private static final RectangleEx BODY_NON_WEAKNESS_DRAW_REGION = RectangleEx.millimetres(5.08, 57.57, 53.85, 27.09);
    private static final RectangleEx BODY_WEAKNESS_DRAW_REGION = RectangleEx.millimetres(5.08, 60.45, 53.85, 24.55);

    private static final RectangleEx BASIC_WEAKNESS_OVERLAY_DRAW_REGION = RectangleEx.millimetres(26.42, 41.15, 11.18, 6.94);
    private static final RectangleEx BASIC_WEAKNESS_ICON_DRAW_REGION = RectangleEx.millimetres(29.63, 42.84, 4.74, 4.74);
    private static final RectangleEx WEAKNESS_SUBTYPE_DRAW_REGION = RectangleEx.millimetres(14.90, 57.07, 33.87, 2.88);

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

        weaknessTypeEditor = EditorUtils.createEnumComboBoxNullable(WeaknessType.class);

        EditorUtils.bindComboBox(weaknessTypeEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getTreacheryFieldsModel()::setWeaknessType));

        weaknessTypeEditor.setSelectedItem(getModel().getTreacheryFieldsModel().getWeaknessType());

        JPanel generalPanel = MigLayoutUtils.createTitledPanel("General");

        commonCardFieldsView.addTitleEditorsToPanel(generalPanel, false, false, false);

        MigLayoutUtils.addLabelledComponentWrapGrowPush(generalPanel, Language.string(InterfaceConstants.WEAKNESS_TYPE), weaknessTypeEditor);

        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel, true);

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(
                generalPanel,
                portraitView.createStandardArtPanel(editorContext)
        );

        // add the panel to the main tab control
        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    @Override
    protected List<TemplateInfo> getAvailableTemplateInfos() {
        String resourcePathPrefix;
        if (getModel().getTreacheryFieldsModel().getWeaknessType() != null) {
            resourcePathPrefix = WEAKNESS_TEMPLATE_RESOURCE_PREFIX;
        } else {
            resourcePathPrefix = STANDARD_TEMPLATE_RESOURCE_PREFIX;
        }

        return TemplateInfos.createStandard300And600(resourcePathPrefix, CardFaceOrientation.Portrait);
    }

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitView.paintArtPortrait(paintContext, paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION));

        // draw the template
        paintContext.paintTemplate();

        paintContext.setRenderingIncludeBleedRegion(false);

        // label
        PaintUtils.paintLabel(paintContext, paintContext.toPixelRect(LABEL_DRAW_REGION), Language.gstring(GameConstants.LABEL_TREACHERY).toUpperCase());

        // title
        commonCardFieldsView.paintTitle(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION));

        if (getModel().getTreacheryFieldsModel().getWeaknessType() == null)
            paintNonWeaknessContent(paintContext);
        else
            paintWeaknessContent(paintContext);

        portraitView.paintArtist(paintContext);
    }

    private void paintNonWeaknessContent(PaintContext paintContext) {
        encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));
        encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Portrait);
        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Portrait);

        commonCardFieldsView.paintBodyAndCopyright(paintContext, paintContext.toPixelRect(BODY_NON_WEAKNESS_DRAW_REGION));
    }

    private void paintWeaknessContent(PaintContext paintContext) {
        // draw the weakness overlay
        // this is the circular area either the standard basic weakness icon goes in or the encounter icon
        // for story weaknesses
        WeaknessType weaknessType = getModel().getTreacheryFieldsModel().getWeaknessType();

        if (weaknessType == WeaknessType.Basic || weaknessType == WeaknessType.Story) {
            ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImageReadOnly(BASIC_WEAKNESS_OVERLAY_RESOURCE), paintContext.toPixelRect(BASIC_WEAKNESS_OVERLAY_DRAW_REGION));

            if (weaknessType == WeaknessType.Basic) {
                ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImageReadOnly(ImageUtils.BASIC_WEAKNESS_ICON_RESOURCE), paintContext.toPixelRect(BASIC_WEAKNESS_ICON_DRAW_REGION));
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

        commonCardFieldsView.paintBodyAndCopyright(paintContext, paintContext.toPixelRect(BODY_WEAKNESS_DRAW_REGION));

        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Portrait, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Portrait);
    }
}
