package com.mickeytheq.ahlcg4j.core.view.common;

import ca.cgjennings.apps.arkham.PortraitPanel;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.ahlcg4j.codegenerated.InterfaceConstants;
import com.mickeytheq.ahlcg4j.core.model.common.PortraitWithArtistModel;
import com.mickeytheq.ahlcg4j.core.view.EditorContext;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;
import com.mickeytheq.ahlcg4j.core.view.utils.EditorUtils;
import com.mickeytheq.ahlcg4j.core.view.utils.MigLayoutUtils;
import com.mickeytheq.ahlcg4j.core.view.utils.TextStyleUtils;
import net.miginfocom.layout.LC;
import resources.Language;

import javax.swing.*;
import java.awt.*;

public class PortraitWithArtistView {
    private final PortraitWithArtistModel model;

    private PortraitView portraitView;
    private JTextField artistEditor;

    public PortraitWithArtistView(PortraitWithArtistModel model, Dimension portraitDimension) {
        this.model = model;

        portraitView = PortraitView.createWithDefaultImage(getModel().getPortraitModel(), portraitDimension);
    }

    public PortraitWithArtistModel getModel() {
        return model;
    }

    public void createEditors(EditorContext editorContext) {
        artistEditor = EditorUtils.createTextField(30);
        EditorUtils.bindTextComponent(artistEditor, editorContext.wrapConsumerWithMarkedChanged(model::setArtist));
        artistEditor.setText(model.getArtist());
    }

    public JPanel createStandardArtPanel(EditorContext editorContext) {
        // have a zero inset as to avoid a double-spaced margin as this panel is usually embedded within other panels
        JPanel artistWithPortraitPanel = MigLayoutUtils.createPanel(new LC().insets("0"));

        PortraitPanel portraitPanel = portraitView.createPortraitPanel(editorContext, Language.string(InterfaceConstants.PORTRAIT));

        artistWithPortraitPanel.add(portraitPanel, "wrap, pushx, growx");

        JPanel artistPanel = MigLayoutUtils.createPanel(Language.string(InterfaceConstants.ARTIST));
        MigLayoutUtils.addLabelledComponentWrap(artistPanel, Language.string(InterfaceConstants.ARTIST), artistEditor);

        artistWithPortraitPanel.add(artistPanel, "wrap, pushx, growx");

        return artistWithPortraitPanel;
    }

    private static final Rectangle ARTIST_DRAW_REGION = new Rectangle(28, 1024, 242, 20);

    public void paintArtPortrait(PaintContext paintContext, Rectangle drawRegion) {
        portraitView.paint(paintContext, drawRegion, false);
    }

    public void paintArtist(PaintContext paintContext) {
        MarkupRenderer markupRenderer = paintContext.createMarkupRenderer();
        markupRenderer.setDefaultStyle(TextStyleUtils.getArtistTextStyle());
        markupRenderer.setAlignment(MarkupRenderer.LAYOUT_MIDDLE | MarkupRenderer.LAYOUT_LEFT);
        markupRenderer.setMarkupText(model.getArtist());
        markupRenderer.drawAsSingleLine(paintContext.getGraphics(), ARTIST_DRAW_REGION);
    }
}
