package com.mickeytheq.hades.core.view.component;

import com.google.common.collect.Lists;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.util.shape.DimensionEx;
import com.mickeytheq.hades.util.shape.Unit;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// UI control for editing a DistanceEx object
public class DimensionExComponent extends JPanel {
    private final JSpinner widthEditor;
    private final JSpinner heightEditor;
    private final JComboBox<Unit> unitEditor;

    private volatile boolean noEvents = false;

    public DimensionExComponent() {
        // by default don't include pixel for distance specification as when rendering content
        this(Lists.newArrayList(Unit.Inch, Unit.Point, Unit.Millimetre));
    }

    public DimensionExComponent(List<Unit> units) {
        widthEditor = new JSpinner(new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 1));
        heightEditor = new JSpinner(new SpinnerNumberModel(0, 0, Double.MAX_VALUE, 1));

        unitEditor = new JComboBox<>();
        units.forEach(unitEditor::addItem);

        setLayout(new MigLayout(MigLayoutUtils.createDefaultLayoutConstraints().gridGap("0", "0").insets("0")));

        add(widthEditor, "width 80:80:80");
        add(heightEditor, "width 80:80:80");
        add(unitEditor, "width 100:100:100");

        widthEditor.addChangeListener(e -> fireActionPerformed());
        heightEditor.addChangeListener(e -> fireActionPerformed());

        unitEditor.addItemListener(e -> fireActionPerformed());
    }

    public DimensionEx getDimensionEx() {
        return DimensionEx.create((Unit)unitEditor.getSelectedItem(), (Double) widthEditor.getValue(), (Double) heightEditor.getValue());
    }

    public void setDimensionEx(DimensionEx dimension) {
        // while updating new values we don't want the action event to fire until all the controls
        // are updated otherwise we see the state half-updated
        noEvents = true;
        try {
            widthEditor.setValue(dimension.getWidth());
            heightEditor.setValue(dimension.getHeight());
            unitEditor.setSelectedItem(dimension.getUnit());
        }
        finally {
            noEvents = false;
        }

        fireActionPerformed();
    }

    public void setEnabled(boolean enabled) {
        widthEditor.setEnabled(enabled);
        heightEditor.setEnabled(enabled);
        unitEditor.setEnabled(enabled);
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
