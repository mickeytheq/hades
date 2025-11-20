package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.strangeeons.ahlcg4j.Card;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceSide;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseCardFaceView<M extends CardFaceModel> implements CardFaceView<M> {
    private M cardFaceModel;

    private ViewContext viewContext;

    private Sheet<Card> sheet;

    public final void initialiseView(M cardFaceModel) {
        this.cardFaceModel = cardFaceModel;

        viewContext = new ViewContextImpl();

        initialiseView();
    }

    public void initialiseView() {
        // default do nothing
    }

    public M getModel() {
        return cardFaceModel;
    }

    public ViewContext getViewContext() {
        return viewContext;
    }

    class ViewContextImpl implements ViewContext {
        @Override
        public void markChanged() {
            getModel().getCard().markChanged(getSheetIndex());
        }
    }

    public Sheet<Card> getSheet() {
        return sheet;
    }

    public int getSheetIndex() {
        return getModel().getCardFaceSide() == CardFaceSide.Front ? 0 : 1;
    }

    @Override
    public Sheet<Card> createSheet() {
        sheet = new CardSheet(getModel().getCard());
        return sheet;
    }

    public abstract BufferedImage loadTemplateImage();

    protected abstract void paint(PaintContext paintContext);

    private class CardSheet extends Sheet<Card> {
        public CardSheet(Card gameComponent) {
            super(gameComponent);

            // BUG: strange eons code has a bug in the parameter checking of initializeTemplate where the class variable is checked for null
            // rather than the parameter being passed in. workaround this by setting it first
            setExpansionSymbolKey("dummy");
            initializeTemplate("dummy", loadTemplateImage(), "dummy", 150.0, 1.0);
        }

        @Override
        protected void paintSheet(RenderTarget renderTarget) {
            Graphics2D g = createGraphics();
            try {
                // the graphics object created is on top of the existing image
                // so we have to clear it first to paint onto a blank canvas
                g.clearRect(0, 0, getTemplateWidth(), getTemplateHeight());

                PaintContext paintContext = new PaintContextImpl(g, renderTarget, sheet);

                BaseCardFaceView.this.paint(paintContext);
            }
            finally {
                g.dispose();
            }
        }
    }

    static class PaintContextImpl implements PaintContext {
        private final Graphics2D g;
        private final RenderTarget renderTarget;
        private final Sheet<Card> sheet;

        private final Map<String, String> tagReplacements = new HashMap<>();

        public PaintContextImpl(Graphics2D g, RenderTarget renderTarget, Sheet<Card> sheet) {
            this.g = g;
            this.renderTarget = renderTarget;
            this.sheet = sheet;
        }

        @Override
        public Graphics2D getGraphics() {
            return g;
        }

        @Override
        public RenderTarget getRenderTarget() {
            return renderTarget;
        }

        @Override
        public void addTagReplacement(String tag, String replacement) {
            tagReplacements.put(tag, replacement);
        }

        @Override
        public MarkupRenderer createMarkupRenderer() {
            MarkupRenderer markupRenderer = new MarkupRenderer(sheet.getTemplateResolution());

            // TODO: need to set tag values for <title> to this face's title
            // TODO: need to set tag values for <fullname> and <fullnameb> for the front/back titles

            tagReplacements.forEach(markupRenderer::setReplacementForTag);
            return markupRenderer;
        }
    }
}
