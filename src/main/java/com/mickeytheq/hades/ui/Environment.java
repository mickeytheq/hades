package com.mickeytheq.hades.ui;

import java.awt.*;

public class Environment {
    // the top level window used to position other elements such as error dialogs
    private static Window TOP_LEVEL_WINDOW;

    public static void setTopLevelWindow(Window topLevelWindow) {
        TOP_LEVEL_WINDOW = topLevelWindow;
    }

    public static Window getTopLevelWindow() {
        return TOP_LEVEL_WINDOW;
    }
}
