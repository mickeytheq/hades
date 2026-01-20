package com.mickeytheq.hades.strangeeons.util;

import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.StrangeEonsAppWindow;
import ca.cgjennings.apps.arkham.StrangeEonsEditor;

import java.io.File;
import java.nio.file.Path;

public class StrangeEonsAppWindowUtils {
    // navigate to the given Path/file
    // if the file is already open in an editor, that editor is selected
    // otherwise the file is opened
    public static void navigateTo(Path pathToFile) {
        StrangeEonsAppWindow appWindow = StrangeEons.getWindow();

        File file = pathToFile.toFile();

        StrangeEonsEditor[] editors = appWindow.getEditorsShowingFile(file);

        if (editors.length > 0) {
            editors[0].select();
            return;
        }

        appWindow.openFile(file);
    }
}
