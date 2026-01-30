package com.mickeytheq.hades.core.view.component;

import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.util.shape.Unit;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// UI control for editing a Distance object
public class DistanceComponent extends JPanel {
    private final JSpinner valueEditor;
    private final JComboBox<Unit> unitEditor;

    private volatile boolean noEvents = false;

    public DistanceComponent() {
        valueEditor = new JSpinner(new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 1));

        unitEditor = EditorUtils.createEnumComboBox(Unit.class);

        setLayout(new MigLayout(MigLayoutUtils.createDefaultLayoutConstraints().gridGap("0", "0").insets("0")));

        add(valueEditor, "width 80:80:80");
        add(unitEditor, "width 100:100:100");

        valueEditor.addChangeListener(e -> fireActionPerformed());

        unitEditor.addItemListener(e -> fireActionPerformed());
    }

    public Distance getDistance() {
        return new Distance((Double)valueEditor.getValue(), (Unit)unitEditor.getSelectedItem());
    }

    public void setDistance(Distance distance) {
        // while updating new values we don't want the action event to fire until all the controls
        // are updated otherwise we see the state half-updated
        noEvents = true;
        try {
            valueEditor.setValue(distance.getAmount());
            unitEditor.setSelectedItem(distance.getUnit());
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
