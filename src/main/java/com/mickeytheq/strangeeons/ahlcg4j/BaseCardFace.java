package com.mickeytheq.strangeeons.ahlcg4j;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;

import java.awt.image.BufferedImage;

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

    protected abstract void paint(Sheet<Card> sheet, RenderTarget renderTarget);

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
            BaseCardFace.this.paint(this, renderTarget);
        }
    }
}
