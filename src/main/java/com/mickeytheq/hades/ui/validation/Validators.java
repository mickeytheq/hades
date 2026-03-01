package com.mickeytheq.hades.ui.validation;

import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.util.SwingUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Validators {
    private static final Image ERROR_ICON;

    private static final Object VALIDATION_HOLDER_KEY = new Object();

    static {
        try {
            ERROR_ICON = ImageIO.read(Validators.class.getResource("/icons/application/icons8-cancel-16.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // creates a validation wrapper around a component
    // when the given validator 'fails' an error icon will appear with the validation text as a tooltip
    public static <T extends JComponent> JPanel addValidationIcon(T component, Validator<T> validator) {
        // this is achieved by wrapping the given component in a JPanel container with an additional JLabel used
        // to display the error icon
        JPanel panel = MigLayoutUtils.createOrganiserPanel();

        panel.add(component);

        JLabel validationIcon = new JLabel();
        validationIcon.setIcon(new ImageIcon(ERROR_ICON));
        validationIcon.setVisible(false);

        // use hidemode which will use the visible status of the component to decide if the component is included
        // in the layout. by default an invisible component still consumes layout space
        panel.add(validationIcon, "aligny center, hidemode 3");

        // store information against the component for later use when validating
        component.putClientProperty(VALIDATION_HOLDER_KEY, new ValidationHolder((Validator<JComponent>)validator, validationIcon));

        return panel;
    }

    public static JPanel required(JTextComponent textEditor) {
        return addValidationIcon(textEditor, component -> {
            if (StringUtils.isEmpty(component.getText()))
                return "Required field";

            return null;
        });
    }

    // called by/on behalf of a container, e.g. an owning dialog, to validate all component in the hierarchy
    // returns false if any component fails validation
    // validation is performed for each component independently, the process does not stop on the first validation failure
    public static boolean validateComponentHierarchy(Component component) {
        AtomicBoolean valid = new AtomicBoolean(true);

        SwingUtils.walkComponentHierarchy(component, c -> {
            if (!validateComponent(c))
                valid.set(false);
        });

        return valid.get();
    }

    public static boolean validateComponent(Component component) {
        if (!(component instanceof JComponent))
            return true;

        JComponent jComponent = (JComponent)component;

        Object clientPropertyValue = jComponent.getClientProperty(VALIDATION_HOLDER_KEY);

        if (!(clientPropertyValue instanceof ValidationHolder))
            return true;

        ValidationHolder validationHolder = (ValidationHolder)clientPropertyValue;

        Validator<JComponent> validator = validationHolder.getValidator();
        String validationResult = validator.validate(jComponent);

        JLabel validationIcon = validationHolder.getValidationIcon();

        boolean isValid = validationResult == null;

        validationIcon.setVisible(!isValid);
        validationIcon.setToolTipText(validationResult);

        jComponent.revalidate();

        return isValid;
    }

    static class ValidationHolder {
        private final Validator<JComponent> validator;
        private final JLabel validationIcon;

        public ValidationHolder(Validator<JComponent> validator, JLabel validationIcon) {
            this.validator = validator;
            this.validationIcon = validationIcon;
        }

        public Validator<JComponent> getValidator() {
            return validator;
        }

        public JLabel getValidationIcon() {
            return validationIcon;
        }
    }
}
