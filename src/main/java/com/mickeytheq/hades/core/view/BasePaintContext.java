package com.mickeytheq.hades.core.view;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.layout.MarkupRenderer;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public abstract class BasePaintContext implements PaintContext {
    private final RenderTarget renderTarget;
    private final CardView cardView;

    public BasePaintContext(RenderTarget renderTarget, CardView cardView) {
        this.renderTarget = renderTarget;
        this.cardView = cardView;
    }

    @Override
    public RenderTarget getRenderTarget() {
        return renderTarget;
    }

    @Override
    public MarkupRenderer createMarkupRenderer() {
        MarkupRenderer markupRenderer = new MarkupRenderer(getRenderingDpi());

        String frontTitle = cardView.getFrontFaceView().getTitle();

        if (!StringUtils.isEmpty(frontTitle)) {
            markupRenderer.setReplacementForTag("fullname", frontTitle);
            markupRenderer.setReplacementForTag("title", frontTitle);
        }

        if (cardView.hasBack()) {
            String backTitle = cardView.getBackFaceView().getTitle();

            if (!StringUtils.isEmpty(backTitle))
                markupRenderer.setReplacementForTag("fullnameb", backTitle);
        }

        return markupRenderer;
    }
}
