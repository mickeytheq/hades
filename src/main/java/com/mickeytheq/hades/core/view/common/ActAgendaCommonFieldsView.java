package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.layout.PageShape;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.common.ActAgendaCommonFieldsModel;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MarkupUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.core.view.utils.PaintUtils;
import org.apache.commons.lang3.StringUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;

public class ActAgendaCommonFieldsView {
    private final ActAgendaCommonFieldsModel model;

    private JTextArea headerEditor;
    private JSpinner afterHeaderSpaceEditor;
    private JTextArea storyEditor;
    private JSpinner afterStorySpaceEditor;
    private JTextArea rulesEditor;

    public ActAgendaCommonFieldsView(ActAgendaCommonFieldsModel model) {
        this.model = model;
    }

    public ActAgendaCommonFieldsModel getModel() {
        return model;
    }

    public void createEditors(EditorContext editorContext) {
        headerEditor = EditorUtils.createTextArea(2, 30);
        afterHeaderSpaceEditor = EditorUtils.createSpinnerNonNegative(Integer.MAX_VALUE);
        storyEditor = EditorUtils.createTextArea(6, 30);
        afterStorySpaceEditor = EditorUtils.createSpinnerNonNegative(Integer.MAX_VALUE);
        rulesEditor = EditorUtils.createTextArea(6, 30);

        EditorUtils.bindTextComponent(headerEditor, editorContext.wrapConsumerWithMarkedChanged(model::setHeader));
        EditorUtils.bindSpinner(afterHeaderSpaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setAfterHeaderSpace));
        EditorUtils.bindTextComponent(storyEditor, editorContext.wrapConsumerWithMarkedChanged(model::setStory));
        EditorUtils.bindSpinner(afterStorySpaceEditor, editorContext.wrapConsumerWithMarkedChanged(model::setAfterStorySpace));
        EditorUtils.bindTextComponent(rulesEditor, editorContext.wrapConsumerWithMarkedChanged(model::setRules));

        headerEditor.setText(model.getHeader());
        afterHeaderSpaceEditor.setValue(model.getAfterHeaderSpace());
        storyEditor.setText(model.getStory());
        afterStorySpaceEditor.setValue(model.getAfterStorySpace());
        rulesEditor.setText(model.getRules());
    }

    public JPanel createPanel(boolean includeHeader) {
        JPanel panel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.GENERAL));

        if (includeHeader) {
            MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.HEADER), headerEditor);
            MigLayoutUtils.addLabelledComponent(panel, Language.string(InterfaceConstants.SPACING), afterHeaderSpaceEditor, MigLayoutUtils.SPACING_EDITOR_CONSTRAINTS);
        }

        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.STORY), storyEditor);
        MigLayoutUtils.addLabelledComponent(panel, Language.string(InterfaceConstants.SPACING), afterStorySpaceEditor, MigLayoutUtils.SPACING_EDITOR_CONSTRAINTS);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.RULES), rulesEditor);

        return panel;
    }

    public void paintBody(PaintContext paintContext, Rectangle bodyDrawRegion, PageShape bodyPageShape) {
        String bodyString = composeBodyString();

        PaintUtils.paintBodyText(paintContext, bodyString, bodyDrawRegion, bodyPageShape);
    }

    private String composeBodyString() {
        StringBuilder sb = new StringBuilder();

        if (!StringUtils.isEmpty(model.getHeader())) {
            sb.append("<center>");
            sb.append("<ts>");
            sb.append(model.getHeader());
            sb.append("</ts>");

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        addSpacing(sb, model.getAfterHeaderSpace());

        if (!StringUtils.isEmpty(model.getStory())) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<gss>");
            sb.append(model.getStory());
            sb.append("</gss>");

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        addSpacing(sb, model.getAfterStorySpace());

        if (!StringUtils.isEmpty(model.getRules())) {
            if (sb.length() > 0)
                sb.append("\n");
            sb.append("<left>");
            sb.append(model.getRules());

            sb.append("\n");
            sb.append(MarkupUtils.getSpacerMarkup(1, 1.5));
        }

        return sb.toString();
    }

    private void addSpacing(StringBuilder sb, Integer spacing) {
        if (spacing == null)
            return;

        if (spacing == 0)
            return;

        sb.append(MarkupUtils.getSpacerMarkup(1, spacing));
    }
}
