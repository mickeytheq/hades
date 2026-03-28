package com.mickeytheq.hades.core.view;

import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.utils.Binder;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;

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
        return getCardFaceView(side == CardFaceSide.Front ? CardFaceSide.Back : CardFaceSide.Front);
    }

    public Optional<CardFaceView> getCardFaceView(CardFaceSide side) {
        if (side == CardFaceSide.Front)
            return Optional.of(getFrontFaceView());

        if (!hasBack())
            return Optional.empty();

        return Optional.of(getBackFaceView());
    }

    public void addMetadataTab(EditorContext editorContext) {
        JPanel commentsPanel = createCommentsPanel(editorContext);
        JPanel metadataPanel = createMetadataPanel(editorContext);

        JPanel panel = MigLayoutUtils.createOrganiserPanel();
        panel.add(metadataPanel, "wrap, pushx, growx");

        // have the comments panel fill all the vertical space available
        panel.add(commentsPanel, "wrap, push, grow");

        editorContext.addDisplayComponent("Metadata", panel); // TODO: i18n
    }

    public JPanel createMetadataPanel(EditorContext editorContext) {
        JCheckBox overrideQuantityEditor = EditorUtils.createCheckBox();
        JSpinner quantityEditor = EditorUtils.createSpinnerNonNegative(Integer.MAX_VALUE);

        JCheckBox setAsideEditor = EditorUtils.createCheckBox();
        JCheckBox rewardEditor = EditorUtils.createCheckBox();
        JCheckBox startingLocationEditor = EditorUtils.createCheckBox();

        Binder.create().onAnyChange(editorContext::markChanged)
                .toggleButton(overrideQuantityEditor, b -> {
                    quantityEditor.setEnabled(b);

                    // when toggling the override on set the persisted value to the current editor value (which will be the default)
                    // otherwise clear it entirely, which effectively reverts to the default for persistence, and show the default
                    if (b) {
                        getCard().getCardMetadataModel().setQuantity((Integer) quantityEditor.getValue());
                    }
                    else {
                        getCard().getCardMetadataModel().setQuantity(null);
                        // TODO: update quantity editor
                    }
                })
                .spinner(quantityEditor, o -> {
                    // only update the model when the quantity changes if the override is enabled
                    // otherwise we will pick up changes made to the editor when reverting to the default
                    if (overrideQuantityEditor.isSelected())
                        getCard().getCardMetadataModel().setQuantity(o);
                })
                .toggleButton(setAsideEditor, getCard().getCardMetadataModel()::setSetAside)
                .toggleButton(rewardEditor, getCard().getCardMetadataModel()::setReward)
                .toggleButton(startingLocationEditor, getCard().getCardMetadataModel()::setStartingLocation);

        overrideQuantityEditor.setSelected(getCard().getCardMetadataModel().getQuantity() != null);
        // TODO: update quantity editor
        setAsideEditor.setSelected(getCard().getCardMetadataModel().isSetAside());
        rewardEditor.setSelected(getCard().getCardMetadataModel().isReward());
        startingLocationEditor.setSelected(getCard().getCardMetadataModel().isStartingLocation());

        JPanel panel = MigLayoutUtils.createTitledPanel("Metadata");
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "Quantity", quantityEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "Set aside", setAsideEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "Reward", rewardEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "Starting location", startingLocationEditor);

        return panel;
    }

    private JPanel createCommentsPanel(EditorContext editorContext) {
        JTextArea commentsEditor = new JTextArea();
        commentsEditor.setLineWrap(true);

        EditorUtils.bindTextComponent(commentsEditor, editorContext.wrapConsumerWithMarkedChanged(s -> getCard().getCardMetadataModel().setComments(s)));
        commentsEditor.setText(getCard().getCardMetadataModel().getComments());

        JScrollPane scrollPane = new JScrollPane(commentsEditor);

        JPanel panel = MigLayoutUtils.createTitledPanel("Comments");
        panel.add(scrollPane, "wrap, grow, push");

        return panel;
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
