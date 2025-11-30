package com.mickeytheq.ahlcg4j.strangeeons.gamecomponent;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;
import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.component.AbstractGameComponent;
import ca.cgjennings.apps.arkham.component.GameComponent;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import ca.cgjennings.io.NewerVersionException;
import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.ahlcg4j.core.Card;
import com.mickeytheq.ahlcg4j.core.CardFaces;
import com.mickeytheq.ahlcg4j.core.model.CardFaceModel;
import com.mickeytheq.ahlcg4j.core.view.CardFaceView;
import com.mickeytheq.ahlcg4j.strangeeons.NewCardDialog;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

// the StrangeEons 'GameComponent' implementation
// the main purposes is to act as a bridge between StrangeEons and its specifics (AbstractGameComponentEditor, Sheet etc)
// and the AHLCG4J 'Card' and associated faces model
public class CardGameComponent extends AbstractGameComponent {
    private static final int CURRENT_VERSION = 1;

    private final Card card;

    public CardGameComponent() {
        NewCardDialog newCardDialog = new NewCardDialog(false);
        newCardDialog.setLocationRelativeTo(StrangeEons.getWindow());
        newCardDialog.setVisible(true);

        Class<? extends CardFaceModel> backFaceModelClass = null;
        if (newCardDialog.getSelectedBackFace() != null) {
            backFaceModelClass = newCardDialog.getSelectedBackFace().getCardFaceModelClass();
        }

        card = CardFaces.createCard(newCardDialog.getSelectedFrontFace().getCardFaceModelClass(), backFaceModelClass);
    }

    public Card getCard() {
        return card;
    }

    @Override
    public Sheet[] createDefaultSheets() {
        // create sheet objects that are provided by the card's face
        Sheet frontSheet = new CardSheet(this, card.getFrontFaceView());

        Sheet[] sheets;

        if (card.getBackFaceView() != null) {
            Sheet backSheet = new CardSheet(this, card.getBackFaceView());
            sheets = new Sheet[]{frontSheet, backSheet};
        }
        else {
            sheets = new Sheet[]{frontSheet};
        }

        setSheets(sheets);

        return sheets;
    }

    @Override
    public AbstractGameComponentEditor<? extends GameComponent> createDefaultEditor() {
        return new CardEditor(this);
    }

    private class CardSheet extends Sheet<CardGameComponent> {
        private final CardFaceView cardFaceView;

        public CardSheet(CardGameComponent gameComponent, CardFaceView cardFaceView) {
            super(gameComponent);

            this.cardFaceView = cardFaceView;

            Dimension dimension = cardFaceView.getDimension();

            // the only things the 'template' image is used for in our scenario is the
            BufferedImage template = new BufferedImage((int) dimension.getWidth(), (int) dimension.getHeight(), BufferedImage.TYPE_INT_ARGB);

            // BUG: strange eons code has a bug in the parameter checking of initializeTemplate where the class variable is checked for null
            // rather than the parameter being passed in. workaround this by setting it first
            setExpansionSymbolKey("dummy");
            initializeTemplate("dummy", template, "dummy", 150.0, 1.0);
        }

        @Override
        protected void paintSheet(RenderTarget renderTarget) {
            Graphics2D g = createGraphics();
            try {
                // the graphics object created is on top of the existing image
                // so we have to clear it first to paint onto a blank canvas
                g.clearRect(0, 0, getTemplateWidth(), getTemplateHeight());

                PaintContext paintContext = new PaintContextImpl(g, renderTarget, this);

                cardFaceView.paint(paintContext);
            } finally {
                g.dispose();
            }
        }
    }

    static class PaintContextImpl implements PaintContext {
        private final Graphics2D g;
        private final RenderTarget renderTarget;
        private final Sheet<CardGameComponent> sheet;

        private final Map<String, String> tagReplacements = new HashMap<>();

        public PaintContextImpl(Graphics2D g, RenderTarget renderTarget, Sheet<CardGameComponent> sheet) {
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
        public double getRenderingDpi() {
            return sheet.getTemplateResolution();
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

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(CURRENT_VERSION);

        // TODO: serialisation
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int version = in.readInt();

        NewerVersionException.check(CURRENT_VERSION, version);

        // TODO: do we need to store the name? a card is just two faces so the less 'Card' level fields the better
//        setNameImpl((String)in.readObject());

        // TODO: serialisation
    }
}
