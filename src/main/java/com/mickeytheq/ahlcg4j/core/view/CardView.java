package com.mickeytheq.ahlcg4j.core.view;

import com.mickeytheq.ahlcg4j.core.model.Card;
import com.mickeytheq.ahlcg4j.core.view.utils.EditorUtils;
import com.mickeytheq.ahlcg4j.core.view.utils.MigLayoutUtils;
import net.miginfocom.swing.MigLayout;
import resources.Language;

import javax.swing.*;

/**
 * Container that joins the {@link Card} model and the front and back {@link CardFaceView}
 */
public class CardView {
    private final Card card;

    private CardFaceView frontFaceView;
    private CardFaceView backFaceView;

    public CardView(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
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

    public void addCommentsTab(EditorContext editorContext) {
        JTextArea commentsEditor = new JTextArea();
        commentsEditor.setLineWrap(true);

        EditorUtils.bindTextComponent(commentsEditor, editorContext.wrapConsumerWithMarkedChanged(s -> getCard().setComments(s)));
        commentsEditor.setText(getCard().getComments());

        JScrollPane scrollPane = new JScrollPane(commentsEditor);

        JPanel panel = MigLayoutUtils.createTitledPanel("Comments");
        panel.add(scrollPane, "wrap, grow, push");

        editorContext.addDisplayComponent(Language.gstring("Comments"), panel);
    }
}
