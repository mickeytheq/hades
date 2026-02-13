package com.mickeytheq.hades.scratchpad;

import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.ui.DialogEx;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Scratch {
    public static void main(String[] args) throws Exception {
        JPanel panel = MigLayoutUtils.createDialogPanel();
        JTextField validatingTextField = new JTextField(50);

        Component component = wrapWithValidationIcon(validatingTextField, () -> !StringUtils.isEmpty(validatingTextField.getText()));

        panel.add(new JLabel("Label for component: "));
        panel.add(component, "growx, pushx");

        DialogEx dialogEx = new DialogEx(null, false);
        dialogEx.setContentComponent(panel);
        dialogEx.addOkCancelButtons(() -> {
            walkComponentHierarchy(dialogEx, c -> {
                if (!(c instanceof JComponent))
                    return;

                JComponent jComponent = (JComponent)c;

                Object validation = jComponent.getClientProperty("VALIDATION");

                if (validation == null)
                    return;

                JLabel validationIcon = (JLabel)jComponent.getClientProperty("VALIDATION_ICON");

                Supplier<Boolean> validator = (Supplier<Boolean>)validation;
                boolean valid = validator.get();

                validationIcon.setVisible(!valid);
                validationIcon.setToolTipText("Text is empty");

                jComponent.revalidate();
            });

            return false;
        });

        dialogEx.showDialog();
    }

    public static Component wrapWithValidationIcon(JComponent component, Supplier<Boolean> validator) {
        JPanel panel = MigLayoutUtils.createOrganiserPanel();
        panel.add(component);

        Image errorIcon = null;
        try {
            errorIcon = ImageIO.read(Scratch.class.getResource("/icons/application_icons/icons8-cancel-16.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JLabel validationIcon = new JLabel();
        validationIcon.setIcon(new ImageIcon(errorIcon));
        validationIcon.setVisible(false);

        panel.add(validationIcon, "aligny center, hidemode 3");

        component.putClientProperty("VALIDATION", validator);
        component.putClientProperty("VALIDATION_ICON", validationIcon);

        return panel;
    }

    public static void walkComponentHierarchy(Component component, Consumer<Component> consumer) {
        consumer.accept(component);

        if (component instanceof Container) {
            Container container = (Container)component;

            for (Component containerComponent : container.getComponents()) {
                walkComponentHierarchy(containerComponent, consumer);
            }
        }
    }
}
