package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.graphics.ImageUtilities;
import ca.cgjennings.layout.MarkupRenderer;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Investigator;
import com.mickeytheq.hades.core.model.common.InvestigatorClass;
import com.mickeytheq.hades.core.model.common.PortraitModel;
import com.mickeytheq.hades.core.model.common.Statistic;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.common.*;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.util.shape.DimensionEx;
import com.mickeytheq.hades.util.shape.RectangleEx;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

@View(interfaceLanguageKey = InterfaceConstants.INVESTIGATOR)
public class InvestigatorView extends BaseCardFaceView<Investigator> implements HasCollectionView, HasEncounterSetView {
    private CommonCardFieldsView commonCardFieldsView;
    private EncounterSetView encounterSetView;
    private CollectionView collectionView;
    private PortraitView portraitView;

    private JComboBox<InvestigatorClass> investigatorClassEditor;
    private JTextField healthEditor;
    private JTextField sanityEditor;
    private JTextField willpowerEditor;
    private JTextField intellectEditor;
    private JTextField combatEditor;
    private JTextField agilityEditor;

    private static final RectangleEx ART_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(0.00, 10.16, 47.07, 53.34);
    private static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimetres(1.86, 2.03, 4.74, 4.74);

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
    protected List<TemplateInfo> getAvailableTemplateInfos() {
        return Lists.newArrayList(TemplateInfos.createStandard300(getTemplateResource(), CardFaceOrientation.Landscape));
    }

    // TODO: story template image is half-resolution
    private String getTemplateResource() {
        return "/templates/investigator/investigator_" + getModel().getInvestigatorFieldsModel().getInvestigatorClass().name().toLowerCase() + ".png";
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        createTitleAndStatsEditors(editorContext);
        createRulesEditors(editorContext);

        CardFaceViewUtils.createEncounterSetCollectionTab(editorContext, encounterSetView, collectionView);
    }

    private void createTitleAndStatsEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);
        portraitView.createEditors(editorContext);

        investigatorClassEditor = EditorUtils.createEnumComboBox(InvestigatorClass.class);
        healthEditor = EditorUtils.createTextField(20);
        sanityEditor = EditorUtils.createTextField(20);
        willpowerEditor = EditorUtils.createTextField(20);
        intellectEditor = EditorUtils.createTextField(20);
        combatEditor = EditorUtils.createTextField(20);
        agilityEditor = EditorUtils.createTextField(20);

        // layout

        // title
        JPanel titlePanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.TITLE));
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, true, true, false);

        // stats
        MigLayout migLayout = new MigLayout(new LC().flowY());
        migLayout.setColumnConstraints("[][grow, fill, :200:]10[][grow, fill, :200:]");
        JPanel statsPanel = new JPanel(migLayout);
        statsPanel.setBorder(BorderFactory.createTitledBorder("Stats")); // TODO: i18n

        // first column
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.CLASS));
        MigLayoutUtils.addLabel(statsPanel, "Health"); // TODO: i18n
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.SANITY));

        // second column
        statsPanel.add(investigatorClassEditor, "newline");
        statsPanel.add(healthEditor);
        statsPanel.add(sanityEditor);

        // third column
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.SKILL_WILLPOWER), "newline");
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.SKILL_INTELLECT));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.SKILL_COMBAT));
        MigLayoutUtils.addLabel(statsPanel, Language.string(InterfaceConstants.SKILL_AGILITY));

        // fourth column
        // second column
        statsPanel.add(willpowerEditor, "newline");
        statsPanel.add(intellectEditor);
        statsPanel.add(combatEditor);
        statsPanel.add(agilityEditor);

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();
        mainPanel.add(titlePanel, "wrap, growx, pushx");
        mainPanel.add(statsPanel, "wrap, growx, pushx");

        editorContext.addDisplayComponent("Stats", mainPanel); // TODO: i18n

        // bindings
        EditorUtils.bindComboBox(investigatorClassEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getInvestigatorFieldsModel()::setInvestigatorClass));
        EditorUtils.bindTextComponent(healthEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getInvestigatorFieldsModel()::setHealth));
        EditorUtils.bindTextComponent(sanityEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getInvestigatorFieldsModel()::setSanity));
        EditorUtils.bindTextComponent(willpowerEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getInvestigatorFieldsModel()::setWillpower));
        EditorUtils.bindTextComponent(intellectEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getInvestigatorFieldsModel()::setIntellect));
        EditorUtils.bindTextComponent(combatEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getInvestigatorFieldsModel()::setCombat));
        EditorUtils.bindTextComponent(agilityEditor, editorContext.wrapConsumerWithMarkedChanged(getModel().getInvestigatorFieldsModel()::setAgility));

        // intialise values
        investigatorClassEditor.setSelectedItem(getModel().getInvestigatorFieldsModel().getInvestigatorClass());
        healthEditor.setText(getModel().getInvestigatorFieldsModel().getHealth());
        sanityEditor.setText(getModel().getInvestigatorFieldsModel().getSanity());
        willpowerEditor.setText(getModel().getInvestigatorFieldsModel().getWillpower());
        intellectEditor.setText(getModel().getInvestigatorFieldsModel().getIntellect());
        combatEditor.setText(getModel().getInvestigatorFieldsModel().getCombat());
        agilityEditor.setText(getModel().getInvestigatorFieldsModel().getAgility());

        collectionView.createEditors(editorContext);
        encounterSetView.createEditors(editorContext);
    }

    private void createRulesEditors(EditorContext editorContext) {
        JPanel generalPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.GENERAL));
        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel, false);

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();

        mainPanel.add(generalPanel, "wrap, pushx, growx");
        mainPanel.add(portraitView.createStandardArtPanel(editorContext), "wrap, pushx, growx");

        // add the panel to the main tab control
        editorContext.addDisplayComponent("Rules / portrait", mainPanel); // TODO: i18n
    }

    private static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetres(8.13, 1.86, 35.05, 4.57);
    private static final RectangleEx SUBTITLE_DRAW_REGION = RectangleEx.millimetres(12.19, 6.60, 29.63, 3.39);
    private static final RectangleEx BODY_DRAW_REGION = RectangleEx.millimetres(51.99, 13.55, 33.87, 36.91);
    private static final RectangleEx COPYRIGHT_DRAW_REGION = RectangleEx.millimetres(66.00, 61.38, 14.10, PaintConstants.FOOTER_TEXT_HEIGHT_MMS);
    private static final RectangleEx ARTIST_DRAW_REGION = RectangleEx.millimetres(49, 61.38, 14.10, PaintConstants.FOOTER_TEXT_HEIGHT_MMS);

    private static final RectangleEx HEALTH_STATISTIC_DRAW_REGION = RectangleEx.millimetres(64.35, 52.15, 0.00, 3.39);
    private static final RectangleEx SANITY_STATISTIC_DRAW_REGION = RectangleEx.millimetres(73.49, 52.15, 0.00, 3.39);

    private static final DimensionEx STAT_DIMENSION = DimensionEx.millimetres(2.71, 3.73);
    private static final double STAT_Y_POSITION = 3.22;

    private static final RectangleEx WILLPOWER_DRAW_REGION = RectangleEx.millimetres(49.28, STAT_Y_POSITION, STAT_DIMENSION);
    private static final RectangleEx INTELLECT_DRAW_REGION = RectangleEx.millimetres(59.44, STAT_Y_POSITION, STAT_DIMENSION);
    private static final RectangleEx COMBAT_DRAW_REGION = RectangleEx.millimetres(69.77, STAT_Y_POSITION, STAT_DIMENSION);
    private static final RectangleEx AGILITY_DRAW_REGION = RectangleEx.millimetres(80.09, STAT_Y_POSITION, STAT_DIMENSION);

    @Override
    public void paint(PaintContext paintContext) {
        // draw the template - unlike most other card types do this first as the template has the background
        paintContext.paintTemplate();

        // special handling for fading
        paintArtPortrait(paintContext);

        paintContext.setRenderingIncludeBleedRegion(false);

        commonCardFieldsView.paintTitles(paintContext, paintContext.toPixelRect(TITLE_DRAW_REGION), paintContext.toPixelRect(SUBTITLE_DRAW_REGION));

        commonCardFieldsView.paintBody(paintContext, paintContext.toPixelRect(BODY_DRAW_REGION));
        commonCardFieldsView.paintCopyright(paintContext, paintContext.toPixelRect(COPYRIGHT_DRAW_REGION));

        if (getModel().getInvestigatorFieldsModel().getInvestigatorClass() == InvestigatorClass.Story) {
            encounterSetView.paintEncounterNumbers(paintContext, CardFaceOrientation.Landscape);
            encounterSetView.paintEncounterPortrait(paintContext, paintContext.toPixelRect(ENCOUNTER_PORTRAIT_DRAW_REGION));
        }

        collectionView.paintCollectionImage(paintContext, CardFaceOrientation.Landscape, true);
        collectionView.paintCollectionNumber(paintContext, CardFaceOrientation.Landscape);

        portraitView.paintArtist(paintContext, paintContext.toPixelRect(ARTIST_DRAW_REGION));

        paintSkills(paintContext);

        PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(HEALTH_STATISTIC_DRAW_REGION), new Statistic(getModel().getInvestigatorFieldsModel().getHealth(), false), PaintUtils.HEALTH_TEXT_OUTLINE_COLOUR, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);
        PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(SANITY_STATISTIC_DRAW_REGION), new Statistic(getModel().getInvestigatorFieldsModel().getSanity(), false), PaintUtils.SANITY_TEXT_OUTLINE_COLOUR, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);
    }


    private void paintSkills(PaintContext paintContext) {
        paintSkill(paintContext, getModel().getInvestigatorFieldsModel().getWillpower(), paintContext.toPixelRect(WILLPOWER_DRAW_REGION));
        paintSkill(paintContext, getModel().getInvestigatorFieldsModel().getIntellect(), paintContext.toPixelRect(INTELLECT_DRAW_REGION));
        paintSkill(paintContext, getModel().getInvestigatorFieldsModel().getCombat(), paintContext.toPixelRect(COMBAT_DRAW_REGION));
        paintSkill(paintContext, getModel().getInvestigatorFieldsModel().getAgility(), paintContext.toPixelRect(AGILITY_DRAW_REGION));
    }

    private void paintSkill(PaintContext paintContext, String text, Rectangle drawRegion) {
        if (StringUtils.isEmpty(text))
            return;

        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getInvestigatorSkillValueTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_CENTER);
        markupRenderer.setMarkupText(text);
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), drawRegion);
    }

    // copied from AHLCG plugin - this does the usual portrait scaling/drawing but mixes in a masking step
    // that creates a faded/integrated look of the source image on to the investigator template to make it look
    // more like official cards
    private void paintArtPortrait(PaintContext paintContext) {
        PortraitModel portraitModel = getModel().getPortraitModel();

        BufferedImage portraitImage = portraitModel.getImage().get();

        if (portraitImage == null)
            return;

        double scale = portraitModel.getScale();

        double cx = portraitImage.getWidth() / 2.0 - portraitModel.getPanX();
        double cy = portraitImage.getHeight() / 2.0 - portraitModel.getPanY();

        Rectangle artPortraitRectangle = paintContext.toPixelRect(ART_PORTRAIT_DRAW_REGION);

        double sizeX = artPortraitRectangle.getWidth() / scale;
        double sizeY = artPortraitRectangle.getHeight() / scale;

        BufferedImage croppedImage = crop(portraitImage, (int)(cx - sizeX / 2.0), (int)(cy - sizeY / 2.0), (int)sizeX, (int)sizeY);
        croppedImage = createStencilImage(croppedImage);

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), croppedImage, artPortraitRectangle);
    }

    private BufferedImage crop(BufferedImage image, int x, int y, int width, int height) {
        if (width < 1)
            width = image.getWidth() - x;
        if (height < 1)
            height = image.getHeight() - y;

        BufferedImage dest = ImageUtilities.createCompatibleIntRGBFormat(image, width, height);
        Graphics2D g = dest.createGraphics();
        try {
            g.drawImage(image, -x, -y, null);
        } finally {
            g.dispose();
        }
        return dest;
    }

    private BufferedImage createStencilImage(BufferedImage source) {
        BufferedImage stencilImage = ImageUtilities.resample(ImageUtils.loadImageReadOnly("/overlays/investigator_mask.png"), source.getWidth(), source.getHeight());

        BufferedImage destinationImage = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = destinationImage.createGraphics();
        try {
            g.drawImage(stencilImage, 0, 0, null);
            g.setComposite(AlphaComposite.SrcIn);
            g.drawImage(source, 0, 0, null);
        } finally {
            g.dispose();
        }

        return destinationImage;
    }
}
