package com.mickeytheq.ahlcg4j.strangeeons.tasks;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;
import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.project.*;
import com.mickeytheq.ahlcg4j.core.CardFaces;
import com.mickeytheq.ahlcg4j.core.model.Card;
import com.mickeytheq.ahlcg4j.core.model.CardFaceModel;
import com.mickeytheq.ahlcg4j.core.view.CardView;
import com.mickeytheq.ahlcg4j.strangeeons.gamecomponent.CardGameComponent;
import com.mickeytheq.ahlcg4j.strangeeons.ui.NewCardDialog;

import java.io.File;

import static resources.Language.string;

public class NewCard extends BaseTaskAction {
    @Override
    public String getLabel() {
        return "New card"; // TODO: i18n
    }

    @Override
    public boolean appliesToSelection(Member[] members) {
        if (members.length > 1)
            return false;

        return true;
    }

    @Override
    public boolean performOnSelection(Member[] members) {
        // launch the dialog to select config for the new card
        NewCardDialog newCardDialog = new NewCardDialog(true);
        newCardDialog.setLocationRelativeTo(StrangeEons.getWindow());
        newCardDialog.setVisible(true);

        Class<? extends CardFaceModel> backFaceModelClass = null;
        if (newCardDialog.getSelectedBackFace() != null) {
            backFaceModelClass = newCardDialog.getSelectedBackFace().getCardFaceModelClass();
        }

        // create the card model and view
        Card card = CardFaces.createCardModel(newCardDialog.getSelectedFrontFace().getCardFaceModelClass(), backFaceModelClass);
        CardView cardView = CardFaces.createCardView(card);

        // create the game component and its editor and attach the editor to the Strange Eons window
        CardGameComponent cardGameComponent = new CardGameComponent(cardView);
        AbstractGameComponentEditor<CardGameComponent> editor = cardGameComponent.createDefaultEditor();
        StrangeEons.getWindow().addEditor(editor);

        // find the Strange Eons member that will parent this new component
        Member parentMember;

        // if there was no selection put it in the root
        if (members.length == 0) {
            parentMember = StrangeEons.getOpenProject();
        }
        else {
            // otherwise find the first folder above the selected member
            parentMember = members[0];

            while (!parentMember.isFolder())
                parentMember = parentMember.getParent();
        }

        // create the new file, attach it to the editor and save it
        File f = new File(parentMember.getFile(), string("pa-new-comp-name") + ".eon");
        f = ProjectUtilities.getAvailableFile(f);
        editor.setFile(f);
        editor.save();

        // tell the parent something has changed
        parentMember.synchronize();

        // select the new component in the project view
        ProjectView view = StrangeEons.getWindow().getOpenProjectView();
        Member m = view.getProject().findMember(f);
        if (m != null) {
            view.select(m);
        }

        return true;
    }
}
