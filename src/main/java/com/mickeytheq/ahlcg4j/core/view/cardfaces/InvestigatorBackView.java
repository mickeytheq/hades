package com.mickeytheq.ahlcg4j.core.view.cardfaces;

import ca.cgjennings.layout.PageShape;
import com.google.common.collect.Lists;
import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.InvestigatorBack;
import com.mickeytheq.ahlcg4j.core.model.common.InvestigatorClass;
import com.mickeytheq.ahlcg4j.core.view.BaseCardFaceView;
import com.mickeytheq.ahlcg4j.core.view.EditorContext;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;
import com.mickeytheq.ahlcg4j.core.view.View;
import com.mickeytheq.ahlcg4j.core.view.common.PortraitWithArtistView;
import com.mickeytheq.ahlcg4j.core.view.utils.*;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.layout.UnitValue;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@View(interfaceLanguageKey = "Investigator Back") // TODO: i18n
public class InvestigatorBackView extends BaseCardFaceView<InvestigatorBack> {
    private PortraitWithArtistView portraitWithArtistView;

    private static final Rectangle ART_PORTRAIT_DRAW_REGION = new Rectangle(2, 0, 376, 412);

    @Override
    public void initialiseView() {
        if (!(getCardView().getFrontFaceView() instanceof InvestigatorView)) {
            // TODO: do something better than this - show an error to the user in a more friendly manner
            throw new RuntimeException("Investigator Back card face is only supported when the front is an investigator");
        }

        portraitWithArtistView = new PortraitWithArtistView(getModel().getPortraitWithArtistModel(), ART_PORTRAIT_DRAW_REGION.getSize());
    }

    private InvestigatorView getInvestigatorFront() {
        return (InvestigatorView) getCardView().getFrontFaceView();
    }

    @Override
    protected BufferedImage getTemplateImage() {
        InvestigatorClass investigatorClass = getInvestigatorFront().getModel().getInvestigatorClass();

        return ImageUtils.loadImage("/templates/investigator/investigator_back_" + investigatorClass.name().toLowerCase() + ".png");
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        portraitWithArtistView.createEditors(editorContext);

        JPanel generalPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.GENERAL));

        createSection(editorContext, getModel().getSection1(), 1, generalPanel);
        createSection(editorContext, getModel().getSection2(), 2, generalPanel);
        createSection(editorContext, getModel().getSection3(), 3, generalPanel);
        createSection(editorContext, getModel().getSection4(), 4, generalPanel);
        createSection(editorContext, getModel().getSection5(), 5, generalPanel);
        createSection(editorContext, getModel().getSection6(), 6, generalPanel);
        createSection(editorContext, getModel().getSection7(), 7, generalPanel);
        createSection(editorContext, getModel().getSection8(), 8, generalPanel);

        JTextArea storyEditor = EditorUtils.createTextArea(8, 30);
        EditorUtils.bindTextComponent(storyEditor, editorContext.wrapConsumerWithMarkedChanged(s -> getModel().setStory(s)));
        storyEditor.setText(getModel().getStory());
        MigLayoutUtils.addLabelledComponentWrap(generalPanel, Language.string(InterfaceConstants.STORY), storyEditor);

        editorContext.addDisplayComponent( "Back - General", generalPanel); // TODO: i18n
        editorContext.addDisplayComponent("Back - Portrait", portraitWithArtistView.createStandardArtPanel(editorContext)); // TODO: i18n
    }

    private void createSection(EditorContext editorContext, InvestigatorBack.InvestigatorBackSection section, int sectionIndex, JPanel panel) {
        JTextField headerEditor = EditorUtils.createTextField(30);
        JTextArea textEditor = EditorUtils.createTextArea(6, 30);
        JSpinner afterSpaceEditor = EditorUtils.createSpinnerNonNegative(100);

        EditorUtils.bindTextComponent(headerEditor, editorContext.wrapConsumerWithMarkedChanged(section::setHeader));
        EditorUtils.bindTextComponent(textEditor, editorContext.wrapConsumerWithMarkedChanged(section::setText));
        EditorUtils.bindSpinner(afterSpaceEditor, editorContext.wrapConsumerWithMarkedChanged(section::setAfterSpacing));

        headerEditor.setText(section.getHeader());
        textEditor.setText(section.getText());
        afterSpaceEditor.setValue(section.getAfterSpacing());

        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.SECTION) + sectionIndex, headerEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.TEXT), textEditor);
        MigLayoutUtils.addLabelledComponentWrap(panel, Language.string(InterfaceConstants.SPACING), afterSpaceEditor);
    }

    private static final Rectangle TITLE_DRAW_REGION = new Rectangle(496, 18, 492, 58);
    private static final Rectangle SUBTITLE_DRAW_REGION = new Rectangle(596, 82, 318, 30);
    private static final Rectangle BODY_DRAW_REGION = new Rectangle(60, 158, 940, 546);

    @Override
    public void paint(PaintContext paintContext) {
        // paint the main/art portrait first as it sits behind the card template
        portraitWithArtistView.paintArtPortrait(paintContext, ART_PORTRAIT_DRAW_REGION);

        // draw the template
        paintContext.getGraphics().drawImage(getTemplateImage(), 0, 0, null);

        // titles
        PaintUtils.paintTitle(paintContext, TITLE_DRAW_REGION, getInvestigatorFront().getModel().getCommonCardFieldsModel().getTitle(), getInvestigatorFront().getModel().getCommonCardFieldsModel().isUnique());
        PaintUtils.paintSubtitle(paintContext, SUBTITLE_DRAW_REGION, getInvestigatorFront().getModel().getCommonCardFieldsModel().getSubtitle());

        // sections
        String markupText = composeSectionString();

        PaintUtils.paintBodyText(paintContext, markupText, BODY_DRAW_REGION, BODY_PAGE_SHAPES.get(getInvestigatorFront().getModel().getInvestigatorClass()));

        portraitWithArtistView.paintArtist(paintContext);
    }

    private String composeSectionString() {
        StringBuilder sb = new StringBuilder();

        for (InvestigatorBack.InvestigatorBackSection section : getModel().getSections()) {
            String header = section.getHeader();
            String text = section.getText();
            int afterSpacing = section.getAfterSpacing();

            if (StringUtils.isEmpty(text))
                continue;

            if (sb.length() > 0)
                sb.append("\n");

            // header and text
            sb.append("<left>");

            if (!StringUtils.isEmpty(header))
                sb.append(section.getHeader());

            sb.append(text);

            // spacing
            if (section.getAfterSpacing() > 0)
                sb.append(MarkupUtils.getSpacerMarkup(1, afterSpacing));
            else
                sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        if (!StringUtils.isEmpty(getModel().getStory())) {
            if (sb.length() > 0)
                sb.append("\n");

            sb.append("<left>");
            sb.append("<iss>");
            sb.append(getModel().getStory());
            sb.append("</iss>");
        }

        return sb.toString();
    }

    private static final Map<InvestigatorClass, PageShape> BODY_PAGE_SHAPES;

    static {
        BODY_PAGE_SHAPES = new HashMap<>();

        BODY_PAGE_SHAPES.put(InvestigatorClass.Guardian, MarkupUtils.createStraightLinePathingPageShape(BODY_DRAW_REGION,
                Lists.newArrayList(
                        new Point2D.Double(0.355, 0.000),
                        new Point2D.Double(0.337, 0.566),
                        new Point2D.Double(0.271, 0.566),
                        new Point2D.Double(0.267, 0.600),
                        new Point2D.Double(0.010, 0.600),
                        new Point2D.Double(0.010, 1.000),
                        new Point2D.Double(1.0, 1.000),
                        new Point2D.Double(1.0, 0.0)
                )));

        BODY_PAGE_SHAPES.put(InvestigatorClass.Seeker, MarkupUtils.createStraightLinePathingPageShape(BODY_DRAW_REGION,
                Lists.newArrayList(
                        new Point2D.Double(0.355, 0.000),
                        new Point2D.Double(0.322, 0.585),
                        new Point2D.Double(0.296, 0.578),
                        new Point2D.Double(0.275, 0.630),
                        new Point2D.Double(0.010, 0.622),
                        new Point2D.Double(0.010, 1.000),
                        new Point2D.Double(1.0, 1.000),
                        new Point2D.Double(1.0, 0.0)
                )));

        BODY_PAGE_SHAPES.put(InvestigatorClass.Mystic, MarkupUtils.createStraightLinePathingPageShape(BODY_DRAW_REGION,
                Lists.newArrayList(
                        new Point2D.Double(0.355, 0.000),
                        new Point2D.Double(0.315, 0.544),
                        new Point2D.Double(0.276, 0.544),
                        new Point2D.Double(0.264, 0.611),
                        new Point2D.Double(0.010, 0.611),
                        new Point2D.Double(0.010, 1.000),
                        new Point2D.Double(1.0, 1.000),
                        new Point2D.Double(1.0, 0.0)
                )));

        BODY_PAGE_SHAPES.put(InvestigatorClass.Rogue, MarkupUtils.createStraightLinePathingPageShape(BODY_DRAW_REGION,
                Lists.newArrayList(
                        new Point2D.Double(0.355, 0.000),
                        new Point2D.Double(0.326, 0.511),
                        new Point2D.Double(0.272, 0.511),
                        new Point2D.Double(0.264, 0.583),
                        new Point2D.Double(0.000, 0.583),
                        new Point2D.Double(0.000, 1.000),
                        new Point2D.Double(1.0, 1.000),
                        new Point2D.Double(1.0, 0.0)
                )));

        // survivor and mystic are the same
        BODY_PAGE_SHAPES.put(InvestigatorClass.Survivor, BODY_PAGE_SHAPES.get(InvestigatorClass.Mystic));

        BODY_PAGE_SHAPES.put(InvestigatorClass.Neutral, MarkupUtils.createStraightLinePathingPageShape(BODY_DRAW_REGION,
                Lists.newArrayList(
                        new Point2D.Double(0.400, 0.000),
                        new Point2D.Double(0.357, 0.468),
                        new Point2D.Double(0.010, 0.468),
                        new Point2D.Double(0.010, 1.000),
                        new Point2D.Double(1.0, 1.000),
                        new Point2D.Double(1.0, 0.0)
                )));
    }
}
