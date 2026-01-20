package com.mickeytheq.hades.strangeeons.tasks;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;
import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.project.*;
import com.mickeytheq.hades.core.CardFaceTypeRegister;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.strangeeons.gamecomponent.CardGameComponent;
import com.mickeytheq.hades.strangeeons.ui.NewCardDialog;
import com.mickeytheq.hades.ui.DialogWithButtons;

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
        Member member = members.length == 0 ? null : members[0];

        newCard(member);

        return true;
    }

    public static void newCard(Member member) {
        // launch the dialog to select config for the new card
        NewCardDialog newCardDialog = new NewCardDialog();
        newCardDialog.setLocationRelativeTo(StrangeEons.getWindow());
        if (newCardDialog.showDialog() != DialogWithButtons.OK_OPTION)
            return;

        Class<? extends CardFaceModel> backFaceModelClass = newCardDialog.getSelectedBackFace()
                .map(CardFaceTypeRegister.CardFaceInfo::getCardFaceModelClass)
                .orElse(null);

        // when creating a new card there may not be a hades project directory in the strange eons root
        // so give the opportunity to create it if required
        ProjectContext projectContext = StandardProjectContext.createContextForStrangeEonsRoot(StrangeEons.getOpenProject().getFile().toPath());

        // create the card model and view
        Card card = ProjectContexts.withContextReturn(projectContext,
                () -> CardFaces.createNewCardModel(newCardDialog.getSelectedFrontFace().getCardFaceModelClass(), backFaceModelClass, projectContext));

        // find the Strange Eons member that will parent this new component
        Member parentMember;

        // if there was no selection put it in the root
        if (member == null) {
            parentMember = StrangeEons.getOpenProject();
        }
        else {
            // otherwise find the first folder above the selected member
            parentMember = member;

            while (!parentMember.isFolder())
                parentMember = parentMember.getParent();
        }

        // create the new file, attach it to the editor and save it
        File f = new File(parentMember.getFile(), newCardDialog.getFilename() + "." + CardIO.HADES_FILE_EXTENSION);
        f = ProjectUtilities.getAvailableFile(f);

        CardView cardView = CardFaces.createCardView(card, projectContext);

        // create the game component and its editor and attach the editor to the Strange Eons window
        CardGameComponent cardGameComponent = new CardGameComponent(cardView, projectContext);
        AbstractGameComponentEditor<CardGameComponent> editor = cardGameComponent.createDefaultEditor();
        StrangeEons.getWindow().addEditor(editor);

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
    }
}
