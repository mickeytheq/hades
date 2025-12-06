package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.cardfaces.Investigator;
import com.mickeytheq.hades.core.model.common.InvestigatorClass;
import com.mickeytheq.hades.core.view.BaseCardFaceView;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.View;
import com.mickeytheq.hades.core.view.common.CommonCardFieldsView;
import com.mickeytheq.hades.core.view.common.NumberingView;
import com.mickeytheq.hades.core.view.common.PortraitWithArtistView;
import com.mickeytheq.hades.core.view.utils.*;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@View(interfaceLanguageKey = InterfaceConstants.INVESTIGATOR)
public class InvestigatorView extends BaseCardFaceView<Investigator> {
    private CommonCardFieldsView commonCardFieldsView;
    private NumberingView numberingView;
    private PortraitWithArtistView portraitWithArtistView;

    private JComboBox<InvestigatorClass> investigatorClassEditor;
    private JTextField healthEditor;
    private JTextField sanityEditor;
    private JTextField willpowerEditor;
    private JTextField intellectEditor;
    private JTextField combatEditor;
    private JTextField agilityEditor;

    private static final Rectangle ART_PORTRAIT_DRAW_REGION = new Rectangle(0, 120, 556, 630);
    private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(22, 24, 56, 56);
    private static final Rectangle COLLECTION_PORTRAIT_DRAW_REGION = new Rectangle(952, 720, 26, 26);

    @Override
    public void initialiseView() {
        commonCardFieldsView = new CommonCardFieldsView(getModel().getCommonCardFieldsModel());
        numberingView = new NumberingView(getModel().getNumberingModel(), COLLECTION_PORTRAIT_DRAW_REGION.getSize(), ENCOUNTER_PORTRAIT_DRAW_REGION.getSize());
        portraitWithArtistView = new PortraitWithArtistView(getModel().getPortraitWithArtistModel(), ART_PORTRAIT_DRAW_REGION.getSize());
    }

    // TODO: story template image is half-resolution
    @Override
    protected BufferedImage getTemplateImage() {
        return ImageUtils.loadImage("/templates/investigator/investigator_" + getModel().getInvestigatorClass().name().toLowerCase() + ".png");
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        createTitleAndStatsEditors(editorContext);
        createRulesEditors(editorContext);

        editorContext.addDisplayComponent("Collection / encounter", numberingView.createStandardCollectionEncounterPanel(editorContext));
    }

    private void createTitleAndStatsEditors(EditorContext editorContext) {
        commonCardFieldsView.createEditors(editorContext);
        portraitWithArtistView.createEditors(editorContext);

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
        commonCardFieldsView.addTitleEditorsToPanel(titlePanel, true, true);

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

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.INVESTIGATOR) + " - " + "Stats", mainPanel); // TODO: i18n

        // bindings
        EditorUtils.bindComboBox(investigatorClassEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setInvestigatorClass(value)));
        EditorUtils.bindTextComponent(healthEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setHealth(value)));
        EditorUtils.bindTextComponent(sanityEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setSanity(value)));
        EditorUtils.bindTextComponent(willpowerEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setWillpower(value)));
        EditorUtils.bindTextComponent(intellectEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setIntellect(value)));
        EditorUtils.bindTextComponent(combatEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setCombat(value)));
        EditorUtils.bindTextComponent(agilityEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setAgility(value)));

        // intialise values
        investigatorClassEditor.setSelectedItem(getModel().getInvestigatorClass());
        healthEditor.setText(getModel().getHealth());
        sanityEditor.setText(getModel().getSanity());
        willpowerEditor.setText(getModel().getWillpower());
        intellectEditor.setText(getModel().getIntellect());
        combatEditor.setText(getModel().getCombat());
        agilityEditor.setText(getModel().getAgility());

        numberingView.createEditors(editorContext);
    }

    private void createRulesEditors(EditorContext editorContext) {
        JPanel generalPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.GENERAL));
        commonCardFieldsView.addNonTitleEditorsToPanel(generalPanel, false);

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();

        mainPanel.add(generalPanel, "wrap, pushx, growx");
        mainPanel.add(portraitWithArtistView.createStandardArtPanel(editorContext), "wrap, pushx, growx");

        // add the panel to the main tab control
        editorContext.addDisplayComponent("Rules / portrait", mainPanel); // TODO: i18n
    }

    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(96, 22, 414, 54);
    private static final Rectangle SUBTITLE_DRAW_REGION = new Rectangle(144, 78, 350, 40);
    private static final Rectangle BODY_DRAW_REGION = new Rectangle(614, 160, 400, 436);

    private static final Rectangle HEALTH_DRAW_REGION = new Rectangle(732, 592, 62, 80);
    private static final Rectangle SANITY_DRAW_REGION = new Rectangle(828, 600, 86, 70);

    private static final Rectangle WILLPOWER_DRAW_REGION = new Rectangle(582, 38, 32, 44);
    private static final Rectangle INTELLECT_DRAW_REGION = new Rectangle(702, 38, 32, 44);
    private static final Rectangle COMBAT_DRAW_REGION = new Rectangle(824, 38, 32, 44);
    private static final Rectangle AGILITY_DRAW_REGION = new Rectangle(946, 38, 32, 44);

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitWithArtistView.paintArtPortrait(paintContext, ART_PORTRAIT_DRAW_REGION);

        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // title
        commonCardFieldsView.paintTitles(paintContext, TITLE_DRAW_REGION, SUBTITLE_DRAW_REGION);

        commonCardFieldsView.paintBodyAndCopyright(paintContext, BODY_DRAW_REGION);

        if (getModel().getInvestigatorClass() == InvestigatorClass.Story) {
            numberingView.paintEncounterNumbers(paintContext);
            numberingView.paintEncounterPortrait(paintContext, ENCOUNTER_PORTRAIT_DRAW_REGION);
        }

        numberingView.paintCollectionPortrait(paintContext, COLLECTION_PORTRAIT_DRAW_REGION, true);
        numberingView.paintCollectionNumber(paintContext);

        portraitWithArtistView.paintArtist(paintContext);

        paintSkills(paintContext);

        PaintUtils.paintHealth(paintContext, HEALTH_DRAW_REGION, false, getModel().getHealth(), false);
        PaintUtils.paintSanity(paintContext, SANITY_DRAW_REGION, false, getModel().getSanity(), false);
    }


    private void paintSkills(PaintContext paintContext) {
        paintSkill(paintContext, getModel().getWillpower(), WILLPOWER_DRAW_REGION);
        paintSkill(paintContext, getModel().getIntellect(), INTELLECT_DRAW_REGION);
        paintSkill(paintContext, getModel().getCombat(), COMBAT_DRAW_REGION);
        paintSkill(paintContext, getModel().getAgility(), AGILITY_DRAW_REGION);
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
}
