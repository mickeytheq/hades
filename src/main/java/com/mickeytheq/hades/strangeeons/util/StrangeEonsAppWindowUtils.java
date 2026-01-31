package com.mickeytheq.hades.strangeeons.util;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.StrangeEonsAppWindow;
import ca.cgjennings.apps.arkham.StrangeEonsEditor;
import ca.cgjennings.apps.arkham.project.Member;
import ca.cgjennings.apps.arkham.project.Project;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.strangeeons.gamecomponent.CardEditor;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class StrangeEonsAppWindowUtils {
    // map and listener that handles selecting the correct card face in the preview when an Editor is opened
    private final static Map<Path, CardFaceSide> selectFaceOnOpen = new HashMap<>();

    static {
        StrangeEons.getWindow().addEditorAddedListener(strangeEonsEditor -> {
            CardFaceSide cardFaceSide = selectFaceOnOpen.remove(strangeEonsEditor.getFile().toPath());

            if (cardFaceSide == null)
                return;

            if (!(strangeEonsEditor instanceof CardEditor))
                return;

            CardEditor cardEditor = (CardEditor)strangeEonsEditor;
            cardEditor.selectPreviewFace(cardFaceSide);
        });
    }

    // navigate to the given Path/file
    // if the file is already open in an editor, that editor is selected
    // otherwise the file is opened
    // cardFaceSide specifies which face should be opened in the preview pane
    public static void navigateTo(Path pathToFile, CardFaceSide cardFaceSide) {
        StrangeEonsAppWindow appWindow = StrangeEons.getWindow();

        if (appWindow == null)
            return;

        File file = pathToFile.toFile();

        StrangeEonsEditor[] editors = appWindow.getEditorsShowingFile(file);

        if (editors.length > 0) {
            editors[0].select();

            selectPreviewFace(editors[0], cardFaceSide);
            return;
        }

        // open() below is asynchronous so we have a permanent listener that waits for the editor to open
        // and selects the correct card face
        selectFaceOnOpen.put(pathToFile, cardFaceSide);

        appWindow.openFile(file);

        // select the file in the project view making sure it is visible in the project view
        Project project = StrangeEons.getOpenProject();

        if (project == null)
            return;

        Member member = StrangeEons.getOpenProject().findMember(file);

        if (member == null)
            return;

        StrangeEons.getOpenProject().getView().ensureVisible(member);
        StrangeEons.getOpenProject().getView().select(member);
    }

    private static void selectPreviewFace(StrangeEonsEditor editor, CardFaceSide cardFaceSide) {
        if (!(editor instanceof CardEditor))
            return;

        CardEditor cardEditor = (CardEditor)editor;
        cardEditor.selectPreviewFace(cardFaceSide);
    }
}
