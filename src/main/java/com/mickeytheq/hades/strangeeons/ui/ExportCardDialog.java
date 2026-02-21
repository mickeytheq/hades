package com.mickeytheq.hades.strangeeons.ui;

import com.google.common.collect.Lists;
import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.core.view.component.DistanceComponent;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.ui.DialogEx;
import com.mickeytheq.hades.ui.FileChooser;
import com.mickeytheq.hades.util.shape.Unit;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

public class ExportCardDialog extends DialogEx {
    private int resolution = 600;
    private Distance bleedMargin = Distance.createZeroPoint();
    private Path outputDirectory;

    public ExportCardDialog(Frame frame) {
        super(frame, false);

        JComboBox<Integer> resolutionEditor = new JComboBox<>();
        resolutionEditor.addItem(300);
        resolutionEditor.addItem(600);

        FileChooser outputDirectoryEditor = FileChooser.create(JFileChooser.DIRECTORIES_ONLY);
        DistanceComponent bleedMarginEditor = new DistanceComponent(Lists.newArrayList(Unit.Point, Unit.Pixel));

        EditorUtils.bindComboBox(resolutionEditor, this::setResolution);
        EditorUtils.bindDistanceComponent(bleedMarginEditor, this::setBleedMargin);
        EditorUtils.bindFileChooser(outputDirectoryEditor, this::setOutputDirectory);

        JPanel panel = MigLayoutUtils.createDialogPanel();
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "Resolution", resolutionEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "Bleed margin", bleedMarginEditor);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, "Output directory", outputDirectoryEditor);

        setContentComponent(panel);

        addOkCancelButtons();
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public Distance getBleedMargin() {
        return bleedMargin;
    }

    public void setBleedMargin(Distance bleedMargin) {
        this.bleedMargin = bleedMargin;
    }

    public Path getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
}
