package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.MultiSectionRenderer;
import com.mickeytheq.hades.core.view.utils.TextStyleUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

// some utility methods that cards faces want to share but don't strictly belong to another utility class
public class CardFaceViewUtils {
    public static void createEncounterSetCollectionTab(EditorContext editorContext, EncounterSetView encounterSetView, CollectionView collectionView) {
        if (encounterSetView == null && collectionView == null)
            return;

        JPanel mainPanel = MigLayoutUtils.createOrganiserPanel();

        if (encounterSetView != null) {
            JPanel encounterSetPanel = encounterSetView.createStandardEncounterPanel(editorContext);
            mainPanel.add(encounterSetPanel, "pushx, growx, wrap");
        }

        if (collectionView != null) {
            JPanel collectionPanel = collectionView.createStandardCollectionPanel(editorContext);
            mainPanel.add(collectionPanel, "pushx, growx, wrap");
        }

        editorContext.addDisplayComponent("Collection / encounter", mainPanel);
    }

    // build the header/story/rules sections with appropriate gaps/lines between them
    public static void buildActAgendaSections(PaintContext paintContext, MultiSectionRenderer renderer, ActAgendaCommonFieldsView ... sectionViews) {
        boolean needSeparator = false;

        for (ActAgendaCommonFieldsView sectionView : sectionViews) {
            if (sectionView.getModel().isNull())
                continue;

            if (needSeparator) {
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));
                renderer.getSections().add(new MultiSectionRenderer.HorizontalLineSection(2));
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));
            }

            buildSection(paintContext, renderer, sectionView);

            needSeparator = true;
        }
    }

    private static void buildSection(PaintContext paintContext, MultiSectionRenderer renderer, ActAgendaCommonFieldsView sectionView) {
        if (sectionView.getModel().isNull())
            return;

        String header = sectionView.getModel().getHeader();
        String story = sectionView.getModel().getStory();
        String rules = sectionView.getModel().getRules();

        boolean needSeparator = false;

        if (!StringUtils.isEmpty(header)) {
            renderer.getSections().add(new MultiSectionRenderer.TextSection(header, TextStyleUtils.getHeaderTextStyle(),
                    MarkupRenderer.LAYOUT_LEFT, paintContext.getRenderingDpi()));

            needSeparator = true;
        }

        if (!StringUtils.isEmpty(story)) {
            if (needSeparator)
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));

            renderer.getSections().add(new MultiSectionRenderer.StoryTextSection(story, TextStyleUtils.getStoryTextStyle(),
                    MarkupRenderer.LAYOUT_LEFT, paintContext.getRenderingDpi(), 20));

            needSeparator = true;
        }

        if (!StringUtils.isEmpty(rules)) {
            if (needSeparator)
                renderer.getSections().add(new MultiSectionRenderer.VerticalSpacerSection(10));

            renderer.getSections().add(new MultiSectionRenderer.TextSection(rules, TextStyleUtils.getBodyTextStyle(),
                    MarkupRenderer.LAYOUT_LEFT, paintContext.getRenderingDpi()));
        }
    }
}
