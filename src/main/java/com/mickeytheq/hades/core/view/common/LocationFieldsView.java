package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.graphics.ImageUtilities;
import ca.cgjennings.graphics.filters.TintFilter;
import ca.cgjennings.layout.PageShape;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.common.LocationFieldsModel;
import com.mickeytheq.hades.core.view.CardFaceView;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.component.StatisticComponent;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.ui.DynamicListCellRenderer;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.text.WordUtils;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LocationFieldsView {
    private final LocationFieldsModel model;
    private final CardFaceView cardFaceView;

    public LocationFieldsView(LocationFieldsModel model, CardFaceView cardFaceView) {
        this.model = model;
        this.cardFaceView = cardFaceView;
    }

    public LocationFieldsModel getModel() {
        return model;
    }

    private StatisticComponent shroudEditor;
    private StatisticComponent cluesEditor;

    private JCheckBox copyOtherFaceEditor;

    private JComboBox<LocationIconInfo> locationIconEditor;
    private JComboBox<LocationIconInfo> connection1Editor;
    private JComboBox<LocationIconInfo> connection2Editor;
    private JComboBox<LocationIconInfo> connection3Editor;
    private JComboBox<LocationIconInfo> connection4Editor;
    private JComboBox<LocationIconInfo> connection5Editor;
    private JComboBox<LocationIconInfo> connection6Editor;

    public void createEditors(EditorContext editorContext) {
        shroudEditor = new StatisticComponent();
        cluesEditor = new StatisticComponent();

        locationIconEditor = createLocationComboBox(false);
        connection1Editor = createLocationComboBox(true);
        connection2Editor = createLocationComboBox(true);
        connection3Editor = createLocationComboBox(true);
        connection4Editor = createLocationComboBox(true);
        connection5Editor = createLocationComboBox(true);
        connection6Editor = createLocationComboBox(true);

        copyOtherFaceEditor = EditorUtils.createCheckBox();
        copyOtherFaceEditor.addChangeListener(e -> {
            boolean enable = !copyOtherFaceEditor.isSelected();

            locationIconEditor.setEnabled(enable);
            connection1Editor.setEnabled(enable);
            connection2Editor.setEnabled(enable);
            connection3Editor.setEnabled(enable);
            connection4Editor.setEnabled(enable);
            connection5Editor.setEnabled(enable);
            connection6Editor.setEnabled(enable);
        });

        // bindings
        EditorUtils.bindToggleButton(copyOtherFaceEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setCopyOtherFace(value)));
        EditorUtils.bindComboBox(locationIconEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setLocationIcon(value.getValue())));
        EditorUtils.bindComboBox(connection1Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setConnectionIcon1(value.getValue())));
        EditorUtils.bindComboBox(connection2Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setConnectionIcon2(value.getValue())));
        EditorUtils.bindComboBox(connection3Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setConnectionIcon3(value.getValue())));
        EditorUtils.bindComboBox(connection4Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setConnectionIcon4(value.getValue())));
        EditorUtils.bindComboBox(connection5Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setConnectionIcon5(value.getValue())));
        EditorUtils.bindComboBox(connection6Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setConnectionIcon6(value.getValue())));

        // intialise values
        copyOtherFaceEditor.setSelected(getModel().isCopyOtherFace());
        locationIconEditor.setSelectedItem(getLocationIconInfoForValue(getModel().getLocationIcon()));
        connection1Editor.setSelectedItem(getLocationIconInfoForValue(getModel().getConnectionIcon1()));
        connection2Editor.setSelectedItem(getLocationIconInfoForValue(getModel().getConnectionIcon2()));
        connection3Editor.setSelectedItem(getLocationIconInfoForValue(getModel().getConnectionIcon3()));
        connection4Editor.setSelectedItem(getLocationIconInfoForValue(getModel().getConnectionIcon4()));
        connection5Editor.setSelectedItem(getLocationIconInfoForValue(getModel().getConnectionIcon5()));
        connection6Editor.setSelectedItem(getLocationIconInfoForValue(getModel().getConnectionIcon6()));
    }

    public JPanel createShroudCluesPanel() {
        JPanel panel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.GENERAL));

        MigLayoutUtils.addLabelledComponent(panel, Language.string(InterfaceConstants.SHROUD), shroudEditor, "growx, pushx");
        MigLayoutUtils.addLabelledComponentWrapGrowPush(panel, Language.string(InterfaceConstants.CLUES), cluesEditor);

        return panel;
    }

    public JPanel createConnectionsPanel() {
        JPanel copyOtherFacePanel = MigLayoutUtils.createOrganiserPanel();
        MigLayoutUtils.addLabelledComponentWrapGrowPush(copyOtherFacePanel, Language.string(InterfaceConstants.COPY_OTHER_FACE), copyOtherFaceEditor);

        MigLayout connectionsLayout = new MigLayout(MigLayoutUtils.createDefaultLayoutConstraints().flowY().insets("0"));
        connectionsLayout.setColumnConstraints("[][grow, fill]10[][grow, fill]");
        JPanel connectionsPanel = new JPanel(connectionsLayout);

        // layout
        // first column
        MigLayoutUtils.addLabel(connectionsPanel, Language.string(InterfaceConstants.ICON));
        MigLayoutUtils.addLabel(connectionsPanel, "1", "align right");
        MigLayoutUtils.addLabel(connectionsPanel, "2", "align right");
        MigLayoutUtils.addLabel(connectionsPanel, "3", "align right");

        // second column
        MigLayoutUtils.addComponentGrowXPushX(connectionsPanel, locationIconEditor, "newline");
        MigLayoutUtils.addComponentGrowXPushX(connectionsPanel, connection1Editor);
        MigLayoutUtils.addComponentGrowXPushX(connectionsPanel, connection2Editor);
        MigLayoutUtils.addComponentGrowXPushX(connectionsPanel, connection3Editor);

        // third column
        MigLayoutUtils.addComponentGrowXPushX(connectionsPanel, new JPanel(), "newline");
        MigLayoutUtils.addLabel(connectionsPanel, "4", "align right");
        MigLayoutUtils.addLabel(connectionsPanel, "5", "align right");
        MigLayoutUtils.addLabel(connectionsPanel, "6", "align right");

        // fourth column
        MigLayoutUtils.addComponentGrowXPushX(connectionsPanel, new JPanel(), "newline");
        MigLayoutUtils.addComponentGrowXPushX(connectionsPanel, connection4Editor);
        MigLayoutUtils.addComponentGrowXPushX(connectionsPanel, connection5Editor);
        MigLayoutUtils.addComponentGrowXPushX(connectionsPanel, connection6Editor);

        // composite panel
        JPanel locationPanel = MigLayoutUtils.createDialogPanel();
        locationPanel.setBorder(BorderFactory.createTitledBorder(Language.string(InterfaceConstants.CONNECTIONS)));

        locationPanel.add(copyOtherFacePanel, "growx, pushx, wrap");
        locationPanel.add(connectionsPanel, "growx, pushx, wrap");

        return locationPanel;
    }

    private LocationIconInfo getLocationIconInfoForValue(String value) {
        LocationIconInfo info = LOCATION_ICON_INFO_MAP.get(value);

        // if the value is somehow not valid, set it to the empty value
        if (info == null)
            return getLocationIconInfoForValue(LocationFieldsModel.EMPTY_VALUE);

        return info;
    }

    private JComboBox<LocationIconInfo> createLocationComboBox(boolean connection) {
        JComboBox<LocationIconInfo> comboBox = new JComboBox<>();
        comboBox.setRenderer(new DynamicListCellRenderer());

        List<LocationIconInfo> locationIconInfos = LOCATION_ICON_INFOS;

        // if this is a connection rather than the location's own icon remove the None option
        if (connection) {
            locationIconInfos = locationIconInfos.stream().filter(o -> !LocationFieldsModel.NONE_VALUE.equals(o.getValue())).collect(Collectors.toList());
        }

        // the main icon of the location can be 'None' which means the icon and containing circle are missing
        for (LocationIconInfo locationIconInfo : locationIconInfos) {
            comboBox.addItem(locationIconInfo);
        }

        return comboBox;
    }

    private static final List<LocationIconInfo> LOCATION_ICON_INFOS = new ArrayList<>();
    private static final Map<String, LocationIconInfo> LOCATION_ICON_INFO_MAP;

    private static final BufferedImage LOCATION_BASE_IMAGE = ImageUtils.loadImage("/icons/location/location_base.png");

    // tint values copied from AHLCG plugin
    // for some reason the Strange Eons encoding of tints multiplies/divides the h-value by 360 when it is saved/loaded
    // so the h-values that were previously specified in the .settings file need to be divided by 360 to get the correct value
    static {
        LOCATION_ICON_INFOS.add(new LocationIconInfo(LocationFieldsModel.NONE_VALUE, Language.string(InterfaceConstants.LOCICON_NONE), null, null));
        LOCATION_ICON_INFOS.add(new LocationIconInfo(LocationFieldsModel.EMPTY_VALUE, Language.string(InterfaceConstants.LOCICON_EMPTY), null, null));
        LOCATION_ICON_INFOS.add(new LocationIconInfo(LocationFieldsModel.COPY_OTHER_VALUE, Language.string(InterfaceConstants.COPY_OTHER_FACE), null, null));

        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_CIRCLE, "circle", 50.0f / 360f, 0.71f, 0.78f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_SQUARE, "square", -2.0f / 360f, 0.73f, 0.63f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_TRIANGLE, "triangle", -150.0f / 360f, 0.41f, 0.35f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_CROSS, "cross", 8.0f / 360f, 0.65f, 0.31f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_DIAMOND, "diamond", 98.0f / 360f, 0.51f, 0.53f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_SLASH, "slash", 40.0f / 360f, 0.54f, 0.46f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_T, "t", -123.0f / 360f, 0.39f, 0.24f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_HOURGLASS, "hourglass", -20.0f / 360f, 0.55f, 0.30f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_MOON, "moon", -25.0f / 360f, 0.51f, 0.51f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_DOUBLESLASH, "double_slash", 131.0f / 360f, 0.34f, 0.24f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_HEART, "heart", 24.0f / 360f, 0.72f, 0.73f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_STAR, "star", -71.0f / 360f, 0.36f, 0.23f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_QUOTE, "quote", 23.0f / 360f, 0.64f, 0.47f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_CLOVER, "clover", 123.0f / 360f, 0.23f, 0.44f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_SPADE, "spade", -26.0f / 360f, 0.66f, 0.72f));

        // alternates
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_TRIANGLEALT, "triangle_alt", 50.0f / 360f, 0.81f, 0.88f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_CROSSALT, "cross_alt", -2.0f / 360f, 0.83f, 0.73f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_DIAMONDALT, "diamond_alt", -150.0f / 360f, 0.51f, 0.45f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_SLASHALT, "slash_alt", 8.0f / 360f, 0.75f, 0.51f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_TALT, "t_alt", 98.0f / 360f, 0.61f, 0.63f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_HOURGLASSALT, "hourglass_alt", 40.0f / 360f, 0.64f, 0.56f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_MOONALT, "moon_alt", -123.0f / 360f, 0.49f, 0.34f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_DOUBLESLASHALT, "double_slash_alt", -20.0f / 360f, 0.65f, 0.40f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_HEARTALT, "heart_alt", -25.0f / 360f, 0.61f, 0.61f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_STARALT, "star_alt", 131.0f / 360f, 0.44f, 0.34f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_CIRCLEALT, "circle_alt", 24.0f / 360f, 0.82f, 0.83f));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_SQUAREALT, "square_alt", -71.0f / 360f, 0.46f, 0.33f));

        LOCATION_ICON_INFO_MAP = LOCATION_ICON_INFOS.stream().collect(Collectors.toMap(LocationIconInfo::getValue, o -> o, (t, t2) -> t, LinkedHashMap::new));
    }

    private static LocationIconInfo createLocationIconInfo(String languageKey, String iconResourceName, float hTint, float sTint, float bTint) {
        TintFilter tintFilter = new TintFilter();
        tintFilter.setFactors(hTint, sTint, bTint);
        BufferedImage tintedImage = tintFilter.filter(LOCATION_BASE_IMAGE, null);

        Icon editorIcon = createLocationEditorIcon(iconResourceName, ImageUtils.deepCopy(tintedImage));
        BufferedImage paintImage = createLocationPaintImage(iconResourceName, ImageUtils.deepCopy(tintedImage));

        String uniqueName = createUnqiueNameFromIconResourceName(iconResourceName);

        return new LocationIconInfo(uniqueName, Language.string(languageKey), editorIcon, paintImage);
    }

    // double_slash -> DoubleSlash. consistent with existing names
    private static String createUnqiueNameFromIconResourceName(String iconResourceName) {
        return WordUtils.capitalizeFully(iconResourceName, '_').replace("_", "");
    }

    private static Icon createLocationEditorIcon(String iconResourceName, BufferedImage baseImage) {
        Graphics2D graphics2D = baseImage.createGraphics();
        try {
            BufferedImage iconImage = ImageUtils.loadImage("/icons/location/location_" + iconResourceName + ".png");
            graphics2D.drawImage(iconImage, 4, 4, null);

            return ImageUtilities.createIconForSize(baseImage, 12);
        }
        finally {
            graphics2D.dispose();
        }
    }

    private static BufferedImage createLocationPaintImage(String iconResourceName, BufferedImage baseImage) {
        Graphics2D graphics2D = baseImage.createGraphics();
        try {
            BufferedImage iconImage = ImageUtils.loadImage("/icons/location/location_" + iconResourceName + ".png");
            graphics2D.drawImage(iconImage, 5, 5, null);

            return baseImage;
        }
        finally {
            graphics2D.dispose();
        }
    }

    static class LocationIconInfo implements DynamicListCellRenderer.HasDisplay, DynamicListCellRenderer.HasIcon {
        private final String value;
        private final String display;
        private final Icon editorIcon;
        private final BufferedImage renderImage;

        public LocationIconInfo(String value, String display, Icon editorIcon, BufferedImage renderImage) {
            this.value = value;
            this.display = display;
            this.editorIcon = editorIcon;
            this.renderImage = renderImage;
        }

        public String getValue() {
            return value;
        }

        public String getDisplay() {
            return display;
        }

        @Override
        public Icon getIcon() {
            return getEditorIcon();
        }

        public Icon getEditorIcon() {
            return editorIcon;
        }

        public BufferedImage getRenderImage() {
            return renderImage;
        }
    }

    private static final Rectangle LOCATION_ICON_DRAW_REGION = new Rectangle(28, 12, 72, 70);
    private static final Rectangle CONNECTION1_DRAW_REGION = new Rectangle(134, 934, 70, 70);
    private static final Rectangle CONNECTION2_DRAW_REGION = new Rectangle(216, 918, 70, 70);
    private static final Rectangle CONNECTION3_DRAW_REGION = new Rectangle(298, 912, 70, 70);
    private static final Rectangle CONNECTION4_DRAW_REGION = new Rectangle(380, 912, 70, 70);
    private static final Rectangle CONNECTION5_DRAW_REGION = new Rectangle(462, 918, 70, 70);
    private static final Rectangle CONNECTION6_DRAW_REGION = new Rectangle(544, 934, 70, 70);
    
    private static final Rectangle CIRCLE_OVERLAY_DRAW_REGION = new Rectangle(18, 2, 94, 92);

    public void paintLocationIcons(PaintContext paintContext) {
        // paint the location background for the location icon but only for the location front template
        if (!LocationFieldsModel.NONE_VALUE.equals(getModel().getLocationIcon())) {
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImage("/overlays/location_circle.png"), CIRCLE_OVERLAY_DRAW_REGION);
        }

        LocationFieldsModel otherFaceModel = getOtherFaceModel();

        paintLocationIcon(paintContext, LOCATION_ICON_DRAW_REGION, otherFaceModel, LocationFieldsModel::getLocationIcon);
        paintLocationIcon(paintContext, CONNECTION1_DRAW_REGION, otherFaceModel, LocationFieldsModel::getConnectionIcon1);
        paintLocationIcon(paintContext, CONNECTION2_DRAW_REGION, otherFaceModel, LocationFieldsModel::getConnectionIcon2);
        paintLocationIcon(paintContext, CONNECTION3_DRAW_REGION, otherFaceModel, LocationFieldsModel::getConnectionIcon3);
        paintLocationIcon(paintContext, CONNECTION4_DRAW_REGION, otherFaceModel, LocationFieldsModel::getConnectionIcon4);
        paintLocationIcon(paintContext, CONNECTION5_DRAW_REGION, otherFaceModel, LocationFieldsModel::getConnectionIcon5);
        paintLocationIcon(paintContext, CONNECTION6_DRAW_REGION, otherFaceModel, LocationFieldsModel::getConnectionIcon6);
    }

    private LocationFieldsModel getOtherFaceModel() {
        Optional<CardFaceView> otherCardFaceViewOpt = cardFaceView.getOtherFaceView();

        // no other face
        if (!otherCardFaceViewOpt.isPresent())
            return null;

        CardFaceView otherCardFaceView = otherCardFaceViewOpt.get();

        if (!(otherCardFaceView instanceof HasLocationFieldsView))
            return null;

        return ((HasLocationFieldsView)otherCardFaceView).getLocationsFieldView().getModel();
    }

    private void paintLocationIcon(PaintContext paintContext, Rectangle drawRegion, LocationFieldsModel otherFaceModel, Function<LocationFieldsModel, String> iconSelector) {
        String locationIcon;

        if (getModel().isCopyOtherFace()) {
            // can't copy the other face if it's not there so do nothing
            if (otherFaceModel == null)
                return;

            locationIcon = LocationFieldsModel.COPY_OTHER_VALUE;
        }
        else {
            locationIcon = iconSelector.apply(getModel());
        }

        // if the model says to copy the other face
        if (LocationFieldsModel.COPY_OTHER_VALUE.equals(locationIcon)) {
            // can't copy the other face if it's not there so do nothing
            if (otherFaceModel == null)
                return;

            locationIcon = iconSelector.apply(otherFaceModel);
        }

        paintLocationIcon(paintContext, drawRegion, locationIcon);
    }

    private void paintLocationIcon(PaintContext paintContext, Rectangle drawRegion, String value) {
        if (value == null)
            return;

        if (value.equals(LocationFieldsModel.NONE_VALUE))
            return;

        LocationIconInfo locationIconInfo = getLocationIconInfoForValue(value);

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), locationIconInfo.getRenderImage(), drawRegion);
    }

    public void paintShroudAndClues(PaintContext paintContext) {

    }

    public static PageShape createBodyPageShape(Rectangle drawRegion) {
        Function<Point2D, Point2D> mapIntoRegionFunction = MarkupUtils.createRatioIntoDrawRegionMapper(drawRegion);

        Path2D path2D = new Path2D.Double();

        Point2D movePoint = mapIntoRegionFunction.apply(new Point2D.Double(0.074, 0.0));
        path2D.moveTo(movePoint.getX(), movePoint.getY());

        Point2D bezierFirstPoint = mapIntoRegionFunction.apply(new Point2D.Double(0.037, 0.153));
        Point2D bezierSecondPoint = mapIntoRegionFunction.apply(new Point2D.Double(0.107, 0.139));
        movePoint = mapIntoRegionFunction.apply(new Point2D.Double(0.0, 0.174));
        path2D.curveTo(bezierFirstPoint.getX(), bezierFirstPoint.getY(), bezierSecondPoint.getX(), bezierSecondPoint.getY(), movePoint.getX(), movePoint.getY());

        movePoint = mapIntoRegionFunction.apply(new Point2D.Double(0.0, 1.0));
        path2D.lineTo(movePoint.getX(), movePoint.getY());

        movePoint = mapIntoRegionFunction.apply(new Point2D.Double(1.0, 1.0));
        path2D.lineTo(movePoint.getX(), movePoint.getY());

        movePoint = mapIntoRegionFunction.apply(new Point2D.Double(1.0, 0.319));
        path2D.lineTo(movePoint.getX(), movePoint.getY());

        bezierFirstPoint = mapIntoRegionFunction.apply(new Point2D.Double(0.991, 0.278));
        bezierSecondPoint = mapIntoRegionFunction.apply(new Point2D.Double(0.962, 0.167));
        movePoint = mapIntoRegionFunction.apply(new Point2D.Double(0.951, 0.125));
        path2D.curveTo(bezierFirstPoint.getX(), bezierFirstPoint.getY(), bezierSecondPoint.getX(), bezierSecondPoint.getY(), movePoint.getX(), movePoint.getY());

        bezierFirstPoint = mapIntoRegionFunction.apply(new Point2D.Double(0.936, 0.132));
        bezierSecondPoint = mapIntoRegionFunction.apply(new Point2D.Double(0.970, 0.174));
        movePoint = mapIntoRegionFunction.apply(new Point2D.Double(0.926, 0.0));
        path2D.curveTo(bezierFirstPoint.getX(), bezierFirstPoint.getY(), bezierSecondPoint.getX(), bezierSecondPoint.getY(), movePoint.getX(), movePoint.getY());

        movePoint = mapIntoRegionFunction.apply(new Point2D.Double(0.074, 0.0));
        path2D.lineTo(movePoint.getX(), movePoint.getY());

        return new PageShape.GeometricShape(path2D, drawRegion);
    }
}
