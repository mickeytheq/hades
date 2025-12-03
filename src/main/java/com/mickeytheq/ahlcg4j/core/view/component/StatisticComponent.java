package com.mickeytheq.ahlcg4j.core.view.component;

import ca.cgjennings.graphics.ImageUtilities;
import ca.cgjennings.ui.DocumentEventAdapter;
import com.mickeytheq.ahlcg4j.core.view.utils.EditorUtils;
import com.mickeytheq.ahlcg4j.core.view.utils.ImageUtils;
import com.mickeytheq.ahlcg4j.core.model.common.Statistic;
import com.mickeytheq.ahlcg4j.core.view.utils.MigLayoutUtils;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class StatisticComponent extends JPanel {
    private final JTextField textField;
    private final JToggleButton perInvestigatorButton;

    private volatile boolean noEvents = false;

    public StatisticComponent() {
        textField = EditorUtils.createTextField(20);

        BufferedImage image = ImageUtils.loadImage(ImageUtils.PER_INVESTIGATOR_ICON_RESOURCE);
        Icon icon = ImageUtilities.createIconForSize(image, 12);
        perInvestigatorButton = new JToggleButton(icon, false);

        setLayout(new MigLayout(MigLayoutUtils.createDefaultLayoutContraints().gridGap("0", "0").insets("0")));

        add(textField, "growx, pushx");
        add(perInvestigatorButton);

        textField.getDocument().addDocumentListener(new DocumentEventAdapter() {
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                fireActionPerformed();
            }
        });

        perInvestigatorButton.addActionListener(e -> fireActionPerformed());
    }

    public Statistic getStatistic() {
        return new Statistic(textField.getText(), perInvestigatorButton.isSelected());
    }

    public void setStatistic(Statistic statistic) {
        // while updating new values we don't want the action event to fire until all the controls
        // are updated otherwise we see the state half-updated
        noEvents = true;
        try {
            textField.setText(statistic.getValue());
            perInvestigatorButton.setSelected(statistic.isPerInvestigator());
        }
        finally {
            noEvents = false;
        }

        fireActionPerformed();
    }

    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    protected void fireActionPerformed() {
        if (noEvents)
            return;

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ActionEvent e = null;

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ActionListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new ActionEvent(this,
                            ActionEvent.ACTION_PERFORMED,
                            null,
                            EventQueue.getMostRecentEventTime(),
                            0);
                }
                ((ActionListener)listeners[i+1]).actionPerformed(e);
            }
        }
    }
}
