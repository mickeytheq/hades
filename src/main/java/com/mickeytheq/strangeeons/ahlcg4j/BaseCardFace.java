package com.mickeytheq.strangeeons.ahlcg4j;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public abstract class BaseCardFace implements CardFace {
    // these two fields are effectively final as they are
    // set during initialise() and should not be touched again
    private Card card;
    private CardFaceSide cardFaceSide;

    private Sheet<Card> sheet;

    // made final to avoid accidental overriding
    // sub-classes should override initialiseCardFace to perform specific initialisation steps
    public final void initialise(Card card, CardFaceSide cardFaceSide) {
        this.card = card;
        this.cardFaceSide = cardFaceSide;

        initialiseCardFace();
    }

    public void initialiseCardFace() {
        // default do nothing
    }

    @Override
    public Card getCard() {
        return card;
    }

    public CardFaceSide getCardFaceSide() {
        return cardFaceSide;
    }

    // takes a consumer, typically used for when binding an editor/component to a field and adds a step that
    // marks the correct sheet as changed on the parent GameComponent to force activities such as UI unchanged status
    // and repainting of the relevant sheet
    protected <T> Consumer<T> wrapEditorBindingWithMarkedChanged(Consumer<T> consumer) {
        return consumer.andThen(t -> getCard().markChanged(getSheetIndex()));
    }

    public Sheet<Card> getSheet() {
        return sheet;
    }

    public int getSheetIndex() {
        return getCardFaceSide() == CardFaceSide.Front ? 0 : 1;
    }

    @Override
    public Sheet<Card> createSheet() {
        sheet = new CardSheet(getCard());
        return sheet;
    }

    public abstract BufferedImage loadTemplateImage();

    protected abstract void paint(Sheet<Card> sheet, Graphics2D g, RenderTarget renderTarget);

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

                BaseCardFace.this.paint(this, g, renderTarget);
            }
            finally {
                g.dispose();
            }
        }
    }
}
