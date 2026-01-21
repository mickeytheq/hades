package com.mickeytheq.hades.strangeeons.util;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.StrangeEonsAppWindow;
import ca.cgjennings.apps.arkham.StrangeEonsEditor;
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

        File file = pathToFile.toFile();

        StrangeEonsEditor[] editors = appWindow.getEditorsShowingFile(file);

        if (editors.length > 0) {
            editors[0].select();

            selectPreviewFace(editors[0], cardFaceSide);
            return;
        }

        selectFaceOnOpen.put(pathToFile, cardFaceSide);

        appWindow.openFile(file);
    }

    private static void selectPreviewFace(StrangeEonsEditor editor, CardFaceSide cardFaceSide) {
        if (!(editor instanceof CardEditor))
            return;

        CardEditor cardEditor = (CardEditor)editor;
        cardEditor.selectPreviewFace(cardFaceSide);
    }
}
