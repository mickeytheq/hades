package com.mickeytheq.hades.core.view;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.core.project.ProjectContext;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public abstract class BasePaintContext implements PaintContext {
    private final RenderTarget renderTarget;
    private final CardFaceView cardFaceView;

    public BasePaintContext(RenderTarget renderTarget, CardFaceView cardFaceView) {
        this.renderTarget = renderTarget;
        this.cardFaceView = cardFaceView;
    }

    @Override
    public RenderTarget getRenderTarget() {
        return renderTarget;
    }

    @Override
    public MarkupRenderer createMarkupRenderer() {
        MarkupRenderer markupRenderer = new MarkupRenderer(getRenderingDpi());

        // fullname is always the front title
        // fullnameb always the back title
        // title - the current card face's title
        setTitleTag(markupRenderer, "fullname", cardFaceView.getCardView().getFrontFaceView().getTitle());

        if (cardFaceView.getCardView().hasBack()) {
            setTitleTag(markupRenderer, "fullnameb", cardFaceView.getCardView().getBackFaceView().getTitle());
        }

        setTitleTag(markupRenderer, "title", cardFaceView.getTitle());

        return markupRenderer;
    }

    @Override
    public ProjectContext getProjectContext() {
        return cardFaceView.getCardView().getProjectContext();
    }

    private void setTitleTag(MarkupRenderer markupRenderer, String tag, String title) {
        if (StringUtils.isEmpty(title))
            return;

        markupRenderer.setReplacementForTag(tag, title);
    }
}
