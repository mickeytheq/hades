package com.mickeytheq.hades.core.view;

import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import resources.Language;

import javax.swing.*;
import java.util.Optional;

/**
 * Container that joins the {@link Card} model and the front and back {@link CardFaceView}
 */
public class CardView {
    private final Card card;
    private final ProjectContext projectContext;

    private CardFaceView frontFaceView;
    private CardFaceView backFaceView;

    public CardView(Card card, ProjectContext projectContext) {
        this.card = card;
        this.projectContext = projectContext;
    }

    public Card getCard() {
        return card;
    }

    public ProjectContext getProjectContext() {
        return projectContext;
    }

    public CardFaceView getFrontFaceView() {
        return frontFaceView;
    }

    public void setFrontFaceView(CardFaceView frontFaceView) {
        this.frontFaceView = frontFaceView;
    }

    public CardFaceView getBackFaceView() {
        if (!hasBack())
            throw new IllegalStateException("Card does not have a back view");

        return backFaceView;
    }

    public void setBackFaceView(CardFaceView backFaceView) {
        this.backFaceView = backFaceView;
    }

    public boolean hasBack() {
        return card.hasBack();
    }

    public Optional<CardFaceView> getOppositeFaceView(CardFaceSide side) {
        if (side == CardFaceSide.Back)
            return Optional.of(getFrontFaceView());

        if (!hasBack())
            return Optional.empty();

        return Optional.of(getBackFaceView());
    }

    public void addCommentsTab(EditorContext editorContext) {
        JTextArea commentsEditor = new JTextArea();
        commentsEditor.setLineWrap(true);

        EditorUtils.bindTextComponent(commentsEditor, editorContext.wrapConsumerWithMarkedChanged(s -> getCard().setComments(s)));
        commentsEditor.setText(getCard().getComments());

        JScrollPane scrollPane = new JScrollPane(commentsEditor);

        JPanel panel = MigLayoutUtils.createTitledPanel("Comments");
        panel.add(scrollPane, "wrap, grow, push");

        editorContext.addDisplayComponent("Comments", panel);
    }

    public String getBriefDisplayString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Front: " + getFrontFaceView().getTitle() + " (type: " + getFrontFaceView().getClass().getSimpleName() + ")");

        if (hasBack())
            sb.append(", Back: " + getBackFaceView().getTitle() + " (type: " + getBackFaceView().getClass().getSimpleName() + ")");
        else
            sb.append(", Back: (None)");

        return sb.toString();
    }
}
