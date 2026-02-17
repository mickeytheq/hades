package com.mickeytheq.hades.core.view;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.hades.core.project.ProjectContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class DefaultPaintContext implements PaintContext {
    private static final Logger logger = LogManager.getLogger(DefaultPaintContext.class);

    private final RenderTarget renderTarget;
    private final CardFaceView cardFaceView;
    private final TemplateInfo templateInfo;

    private final Graphics2D originalGraphics2D;
    private Graphics2D currentGraphics2D;

    public DefaultPaintContext(Graphics2D graphics2D, RenderTarget renderTarget, CardFaceView cardFaceView, TemplateInfo templateInfo) {
        this.originalGraphics2D = graphics2D;
        this.renderTarget = renderTarget;
        this.cardFaceView = cardFaceView;
        this.templateInfo = templateInfo;

        // start off with the graphics being set as passed in
        this.currentGraphics2D = this.originalGraphics2D;
    }

    @Override
    public TemplateInfo getTemplateInfo() {
        return templateInfo;
    }

    @Override
    public Graphics2D getGraphics() {
        return currentGraphics2D;
    }

    @Override
    public void setRenderingIncludeBleedRegion(boolean includeBleedRegion) {
        // to include the bleed region in rendering just reset back to the original graphics
        if (includeBleedRegion) {
            currentGraphics2D = originalGraphics2D;
            return;
        }

        // if the current graphics is different to the original then we're already in this mode
        if (currentGraphics2D != originalGraphics2D)
            return;

        // if there's no bleed margin in the template this operation has no effect
        if (templateInfo.getAvailableBleedMarginInPixels() == 0)
            return;

        // create a new graphics from the original one that incorporates a transform and clip to exclude the bleed margin
        int bleedMargin = templateInfo.getAvailableBleedMarginInPixels();
        currentGraphics2D = (Graphics2D) originalGraphics2D.create(bleedMargin, bleedMargin, templateInfo.getWidthInPixelsWithoutBleed(), templateInfo.getHeightInPixelsWithoutBleed());
    }

    public void paintTemplate() {
        Graphics2D graphics2D = getGraphics();

        StopWatch stopWatch = StopWatch.createStarted();

        graphics2D.drawImage(getTemplateInfo().getTemplateImage(), 0, 0, templateInfo.getWidthInPixels(), templateInfo.getHeightInPixels(), null);

        if (logger.isTraceEnabled())
            logger.trace("Painting of template completed in " + stopWatch.getTime() + "ms");
    }

    @Override
    public RenderTarget getRenderTarget() {
        return renderTarget;
    }

    @Override
    public MarkupRenderer createMarkupRenderer() {
        MarkupRenderer markupRenderer = new MarkupRenderer(templateInfo.getResolutionInPixelsPerInch());

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
