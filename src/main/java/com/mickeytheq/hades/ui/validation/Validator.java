package com.mickeytheq.hades.ui.validation;

import javax.swing.*;

public interface Validator<T extends JComponent> {
    String validate(T component);
}
