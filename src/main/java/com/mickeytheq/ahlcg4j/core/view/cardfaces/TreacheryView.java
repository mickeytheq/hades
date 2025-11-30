package com.mickeytheq.ahlcg4j.core.view.cardfaces;

import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.ahlcg4j.core.view.EditorContext;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Treachery;
import com.mickeytheq.ahlcg4j.core.view.BaseCardFaceView;
import com.mickeytheq.ahlcg4j.core.view.View;
import com.mickeytheq.ahlcg4j.core.view.common.CommonCardFieldsView;
import com.mickeytheq.ahlcg4j.core.view.common.NumberingView;
import com.mickeytheq.ahlcg4j.core.view.utils.*;
import com.mickeytheq.ahlcg4j.core.model.common.WeaknessType;
import com.mickeytheq.ahlcg4j.codegenerated.GameConstants;
import net.miginfocom.swing.MigLayout;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

@View(interfaceLanguageKey = InterfaceConstants.TREACHERY)
public class TreacheryView extends BaseCardFaceView<Treachery> {
    private static final URL DEFAULT_TEMPLATE_RESOURCE = Treachery.class.getResource("/templates/treachery/treachery.png");
    private static final URL WEAKNESS_TEMPLATE_RESOURCE = Treachery.class.getResource("/templates/treachery/weakness_treachery.png");
    private static final URL BASIC_WEAKNESS_OVERLAY_RESOURCE = Treachery.class.getResource("/overlays/encounter_asset.png");

    private JComboBox<WeaknessType> weaknessTypeEditor;
    private CommonCardFieldsView commonCardFieldsView;
    private NumberingView numberingView;

    // locations to draw portraits
    private static final Rectangle COLLECTION_PORTRAIT_DRAW_REGION = new Rectangle(640, 1020, 26, 26);
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(350, 508, 56, 56);
    private static final Rectangle ART_PORTRAIT_DRAW_REGION = new Rectangle(34, 0, 688, 596);

    // locations to draw other elements
    private static final Rectangle LABEL_DRAW_REGION = new Rectangle(274, 572, 208, 28);
    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(78, 614, 598, 58);
    private static final Rectangle BODY_NON_WEAKNESS_DRAW_REGION = new Rectangle(60, 680, 636, 320);
    private static final Rectangle BODY_WEAKNESS_DRAW_REGION = new Rectangle(60, 714, 636, 290);

    private static final Rectangle BASIC_WEAKNESS_OVERLAY_DRAW_REGION = new Rectangle(312, 486, 132, 82);
    private static final Rectangle BASIC_WEAKNESS_ICON_DRAW_REGION = new Rectangle(350, 506, 56, 56);
    private static final Rectangle WEAKNESS_SUBTYPE_DRAW_REGION = new Rectangle(176, 674, 400, 34);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        numberingView = new NumberingView(getModel().getNumberingModel());
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext, ART_PORTRAIT_DRAW_REGION);
        numberingView.createEditors(editorContext, COLLECTION_PORTRAIT_DRAW_REGION, ENCOUNTER_PORTRAIT_DRAW_REGION);

        weaknessTypeEditor = new JComboBox<>();
        weaknessTypeEditor.addItem(WeaknessType.None);
        weaknessTypeEditor.addItem(WeaknessType.Basic);
        weaknessTypeEditor.addItem(WeaknessType.Investigator);
        weaknessTypeEditor.addItem(WeaknessType.Story);

        EditorUtils.bindComboBox(weaknessTypeEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setWeaknessType(value)));

        JPanel generalPanel = MigLayoutUtils.createPanel("General");

        commonCardFieldsView.addTitleEditorsToPanel(generalPanel, false, false);

        generalPanel.add(new JLabel("Weakness type"));
        generalPanel.add(weaknessTypeEditor, "wrap, pushx, growx");

        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel);

        JPanel mainPanel = new JPanel(new MigLayout());

        mainPanel.add(generalPanel, "wrap, pushx, growx");

        mainPanel.add(commonCardFieldsView.createStandardArtPanel(), "wrap, pushx, growx");

        // add the panel to the main tab control
        editorContext.getTabbedPane().addTab(getCardFaceSide().name(), mainPanel);
        editorContext.getTabbedPane().addTab("Collection / encounter", numberingView.createStandardCollectionEncounterPanel());
    }

    public BufferedImage getTemplateImage() {
        URL templateUrl;
        if (getModel().getWeaknessType() != WeaknessType.None)
            templateUrl = WEAKNESS_TEMPLATE_RESOURCE;
        else
            templateUrl = DEFAULT_TEMPLATE_RESOURCE;

        return ImageUtils.loadImage(templateUrl);
    }

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        commonCardFieldsView.paintArtPortrait(paintContext);

        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // label
        PaintUtils.paintLabel(paintContext, LABEL_DRAW_REGION, Language.gstring(GameConstants.LABEL_TREACHERY).toUpperCase());

        // title
        commonCardFieldsView.paintTitle(paintContext, TITLE_DRAW_REGION);

        if (getModel().getWeaknessType() == WeaknessType.None)
            paintNonWeaknessContent(paintContext);
        else
            paintWeaknessContent(paintContext);
    }

    private void paintNonWeaknessContent(PaintContext paintContext) {
        numberingView.paintEncounterPortrait(paintContext);
        numberingView.paintEncounterNumbers(paintContext);
        numberingView.paintCollectionPortrait(paintContext, true);
        numberingView.paintCollectionNumber(paintContext);

        commonCardFieldsView.paintBodyCopyrightArtist(paintContext, BODY_NON_WEAKNESS_DRAW_REGION);
    }

    private void paintWeaknessContent(PaintContext paintContext) {
        // draw the weakness overlay
        // this is the circular area either the standard basic weakness icon goes in or the encounter icon
        // for story weaknesses
        WeaknessType weaknessType = getModel().getWeaknessType();

        if (weaknessType == WeaknessType.Basic || weaknessType == WeaknessType.Story) {
            ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImage(BASIC_WEAKNESS_OVERLAY_RESOURCE), BASIC_WEAKNESS_OVERLAY_DRAW_REGION);

            if (weaknessType == WeaknessType.Basic) {
                ImageUtils.drawImage(paintContext.getGraphics(), ImageUtils.loadImage(ImageUtils.BASIC_WEAKNESS_ICON_RESOURCE), BASIC_WEAKNESS_ICON_DRAW_REGION);
            }
            else {
                numberingView.paintEncounterPortrait(paintContext);
                numberingView.paintEncounterNumbers(paintContext);
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
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), WEAKNESS_SUBTYPE_DRAW_REGION);

        commonCardFieldsView.paintBodyCopyrightArtist(paintContext, BODY_WEAKNESS_DRAW_REGION);

        numberingView.paintCollectionPortrait(paintContext, true);
        numberingView.paintCollectionNumber(paintContext);
    }
}
