package com.mickeytheq.hades.core.view.common;

import ca.cgjennings.graphics.ImageUtilities;
import ca.cgjennings.graphics.filters.TintFilter;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.model.common.LocationFieldsModel;
import com.mickeytheq.hades.core.view.CardFaceView;
import com.mickeytheq.hades.core.view.EditorContext;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.component.StatisticComponent;
import com.mickeytheq.hades.core.view.utils.*;
import com.mickeytheq.hades.ui.DynamicListCellRenderer;
import com.mickeytheq.hades.util.shape.DimensionEx;
import com.mickeytheq.hades.util.shape.RectangleEx;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.text.WordUtils;
import org.mozilla.javascript.tools.debugger.Dim;
import resources.Language;

import javax.swing.*;
import java.awt.*;
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
        EditorUtils.bindStatisticComponent(shroudEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setShroud(value)));
        EditorUtils.bindStatisticComponent(cluesEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setClues(value)));
        EditorUtils.bindToggleButton(copyOtherFaceEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setCopyOtherFace(value)));
        EditorUtils.bindComboBox(locationIconEditor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setLocationIcon(value.getValue())));
        EditorUtils.bindComboBox(connection1Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setConnectionIcon1(value.getValue())));
        EditorUtils.bindComboBox(connection2Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setConnectionIcon2(value.getValue())));
        EditorUtils.bindComboBox(connection3Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setConnectionIcon3(value.getValue())));
        EditorUtils.bindComboBox(connection4Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setConnectionIcon4(value.getValue())));
        EditorUtils.bindComboBox(connection5Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setConnectionIcon5(value.getValue())));
        EditorUtils.bindComboBox(connection6Editor, editorContext.wrapConsumerWithMarkedChanged(value -> getModel().setConnectionIcon6(value.getValue())));

        // intialise values
        shroudEditor.setStatistic(getModel().getShroud());
        cluesEditor.setStatistic(getModel().getClues());
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

    // tint values copied from AHLCG plugin
    // for some reason the Strange Eons encoding of tints multiplies/divides the h-value by 360 when it is saved/loaded
    // so the h-values that were previously specified in the .settings file need to be divided by 360 to get the correct value
    static {
        LOCATION_ICON_INFOS.add(new LocationIconInfo(LocationFieldsModel.NONE_VALUE, null, Language.string(InterfaceConstants.LOCICON_NONE), null));
        LOCATION_ICON_INFOS.add(new LocationIconInfo(LocationFieldsModel.EMPTY_VALUE, null, Language.string(InterfaceConstants.LOCICON_EMPTY), null));
        LOCATION_ICON_INFOS.add(new LocationIconInfo(LocationFieldsModel.COPY_OTHER_VALUE, null, Language.string(InterfaceConstants.COPY_OTHER_FACE), null));

        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_CIRCLE, "circle"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_CLOVER, "clover"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_CROSS, "cross"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_DIAMOND, "diamond"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_DOUBLESLASH, "double_slash"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_HEART, "heart"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_HOURGLASS, "hourglass"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_MOON, "moon"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_QUOTE, "quote"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_SLASH, "slash"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_SPADE, "spade"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_SQUARE, "square"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_STAR, "star"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_T, "t"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_TRIANGLE, "triangle"));

        // alternates
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_TRIANGLEALT, "triangle_alt"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_CROSSALT, "cross_alt"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_DIAMONDALT, "diamond_alt"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_SLASHALT, "slash_alt"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_TALT, "t_alt"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_HOURGLASSALT, "hourglass_alt"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_MOONALT, "moon_alt"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_DOUBLESLASHALT, "double_slash_alt"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_HEARTALT, "heart_alt"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_STARALT, "star_alt"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_CIRCLEALT, "circle_alt"));
        LOCATION_ICON_INFOS.add(createLocationIconInfo(InterfaceConstants.LOCICON_SQUAREALT, "square_alt"));

        LOCATION_ICON_INFO_MAP = LOCATION_ICON_INFOS.stream().collect(Collectors.toMap(LocationIconInfo::getValue, o -> o, (t, t2) -> t, LinkedHashMap::new));
    }

    private static String getLocationIconResourcePath(String iconResourceName) {
        return "/overlays/location/symbols/" + iconResourceName + ".svg";
    }

    private static LocationIconInfo createLocationIconInfo(String languageKey, String iconResourceName) {
        Icon editorIcon = new ImageIcon(ImageUtils.loadSvgImageReadOnly(getLocationIconResourcePath(iconResourceName), 12, 12));

        String uniqueName = createUnqiueNameFromIconResourceName(iconResourceName);

        return new LocationIconInfo(uniqueName, iconResourceName, Language.string(languageKey), editorIcon);
    }

    // double_slash -> DoubleSlash. consistent with existing names
    private static String createUnqiueNameFromIconResourceName(String iconResourceName) {
        return WordUtils.capitalizeFully(iconResourceName, '_').replace("_", "");
    }

    private static Icon createLocationEditorIcon(String iconResourceName, BufferedImage baseImage) {
        Graphics2D graphics2D = baseImage.createGraphics();
        try {
            BufferedImage iconImage = ImageUtils.loadSvgImageReadOnly("/overlays/location/symbols/" + iconResourceName + ".svg", 12, 12);
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
            BufferedImage iconImage = ImageUtils.loadSvgImageReadOnly("/overlays/location/symbols/" + iconResourceName + ".svg", 12, 12);
            graphics2D.drawImage(iconImage, 5, 5, null);

            return baseImage;
        }
        finally {
            graphics2D.dispose();
        }
    }

    static class LocationIconInfo implements DynamicListCellRenderer.HasDisplay, DynamicListCellRenderer.HasIcon {
        private final String value;
        private final String iconResourceName;
        private final String display;
        private final Icon editorIcon;

        public LocationIconInfo(String value, String iconResourceName, String display, Icon editorIcon) {
            this.value = value;
            this.iconResourceName = iconResourceName;
            this.display = display;
            this.editorIcon = editorIcon;
        }

        public String getValue() {
            return value;
        }

        public String getIconResourceName() {
            return iconResourceName;
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

    }

    // shared values used in both LocationView and LocationBackView
    public static final RectangleEx LABEL_DRAW_REGION = RectangleEx.millimetresHorizontallyCentred(47.00, 16.93, 2.37);
    public static final RectangleEx TITLE_DRAW_REGION = RectangleEx.millimetresHorizontallyCentred(1.20, 40.98, 4.91);
    public static final RectangleEx SUBTITLE_DRAW_REGION = RectangleEx.millimetresHorizontallyCentred(6.40, 32.34, 3.56);
    public static final RectangleEx ENCOUNTER_PORTRAIT_DRAW_REGION = RectangleEx.millimetresHorizontallyCentred(41.49, PaintConstants.ENCOUNTER_SET_ICON_SIZE);

    // negative as the overlay has a fair bit of transparent surrounding space which can clip outside the total draw region safely
    private static final RectangleEx CIRCLE_OVERLAY_DRAW_REGION = RectangleEx.millimetres(0.10, -1.00, 10.58, 10.58);

    private static final DimensionEx LOCATION_ICON_SIZE = DimensionEx.millimetres(5.884, 5.884);

    // the circle overlay isn't perfectly centred in its source image so we have to nudge the icon slightly to centre it
    private static final RectangleEx LOCATION_ICON_DRAW_REGION = CIRCLE_OVERLAY_DRAW_REGION.centreOn(LOCATION_ICON_SIZE).nudgeRight(0.1).nudgeUp(0.1);

    private static final RectangleEx CONNECTION1_DRAW_REGION = RectangleEx.millimetres(11.35, 78.88, LOCATION_ICON_SIZE);
    private static final RectangleEx CONNECTION2_DRAW_REGION = RectangleEx.millimetres(18.29, 77.52, LOCATION_ICON_SIZE);
    private static final RectangleEx CONNECTION3_DRAW_REGION = RectangleEx.millimetres(25.23, 77.02, LOCATION_ICON_SIZE);
    private static final RectangleEx CONNECTION4_DRAW_REGION = RectangleEx.millimetres(32.17, 77.02, LOCATION_ICON_SIZE);
    private static final RectangleEx CONNECTION5_DRAW_REGION = RectangleEx.millimetres(39.12, 77.52, LOCATION_ICON_SIZE);
    private static final RectangleEx CONNECTION6_DRAW_REGION = RectangleEx.millimetres(46.06, 78.88, LOCATION_ICON_SIZE);

    public void paintLocationIcons(PaintContext paintContext) {
        paintCircleOverlay(paintContext);

        LocationFieldsModel otherFaceModel = getOtherFaceModel();

        paintLocationIcon(paintContext, paintContext.toPixelRect(LOCATION_ICON_DRAW_REGION), otherFaceModel, LocationFieldsModel::getLocationIcon);
        paintLocationIcon(paintContext, paintContext.toPixelRect(CONNECTION1_DRAW_REGION), otherFaceModel, LocationFieldsModel::getConnectionIcon1);
        paintLocationIcon(paintContext, paintContext.toPixelRect(CONNECTION2_DRAW_REGION), otherFaceModel, LocationFieldsModel::getConnectionIcon2);
        paintLocationIcon(paintContext, paintContext.toPixelRect(CONNECTION3_DRAW_REGION), otherFaceModel, LocationFieldsModel::getConnectionIcon3);
        paintLocationIcon(paintContext, paintContext.toPixelRect(CONNECTION4_DRAW_REGION), otherFaceModel, LocationFieldsModel::getConnectionIcon4);
        paintLocationIcon(paintContext, paintContext.toPixelRect(CONNECTION5_DRAW_REGION), otherFaceModel, LocationFieldsModel::getConnectionIcon5);
        paintLocationIcon(paintContext, paintContext.toPixelRect(CONNECTION6_DRAW_REGION), otherFaceModel, LocationFieldsModel::getConnectionIcon6);
    }

    private void paintCircleOverlay(PaintContext paintContext) {
        String resourcePath = "/overlays/location/icon_circle_" + cardFaceView.getCardFaceSide().name().toLowerCase() + ".png";

        // paint the location circle for the location icon
        if (!LocationFieldsModel.NONE_VALUE.equals(getModel().getLocationIcon())) {
            PaintUtils.paintBufferedImage(paintContext.getGraphics(), ImageUtils.loadImageReadOnly(resourcePath),
                    paintContext.toPixelRect(CIRCLE_OVERLAY_DRAW_REGION));
        }
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

        String resourcePath = getLocationIconResourcePath(locationIconInfo.getIconResourceName());
        BufferedImage locationIconImage = ImageUtils.loadSvgImageReadOnly(resourcePath, (int)drawRegion.getWidth(), (int)drawRegion.getHeight());

        PaintUtils.paintBufferedImage(paintContext.getGraphics(), locationIconImage, drawRegion);
    }

    // the widths are set to zero as statistic painting uses the height to size the statistic value and per investigator icon
    // so the x position is the centre-line for the shroud/clue circles and the text is centred on that position
    private static final DimensionEx STATISTIC_SIZE = DimensionEx.millimetres(0.00, 3.56);

    private static final RectangleEx SHROUD_DRAW_REGION = RectangleEx.millimetres(5.93, 45.00, STATISTIC_SIZE);
    private static final RectangleEx CLUES_DRAW_REGION = RectangleEx.millimetres(57.66, 45.00, STATISTIC_SIZE);

    public void paintShroudAndClues(PaintContext paintContext) {
        PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(SHROUD_DRAW_REGION), getModel().getShroud(), Color.BLACK, PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR);
        PaintUtils.paintStatistic(paintContext, paintContext.toPixelRect(CLUES_DRAW_REGION), getModel().getClues(), PaintUtils.STATISTIC_LIGHT_TEXT_COLOUR, Color.BLACK);
    }

}
