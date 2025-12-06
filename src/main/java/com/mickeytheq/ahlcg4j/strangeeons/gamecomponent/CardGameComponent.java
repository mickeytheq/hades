package com.mickeytheq.ahlcg4j.strangeeons.gamecomponent;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;
import ca.cgjennings.apps.arkham.component.AbstractGameComponent;
import ca.cgjennings.apps.arkham.component.GameComponent;
import ca.cgjennings.apps.arkham.dialog.ErrorDialog;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import ca.cgjennings.io.NewerVersionException;
import ca.cgjennings.layout.MarkupRenderer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mickeytheq.ahlcg4j.core.CardFaces;
import com.mickeytheq.ahlcg4j.core.model.Card;
import com.mickeytheq.ahlcg4j.core.view.CardFaceView;
import com.mickeytheq.ahlcg4j.core.view.CardView;
import com.mickeytheq.ahlcg4j.serialise.JsonCardSerialiser;
import com.mickeytheq.ahlcg4j.core.view.PaintContext;
import org.apache.commons.lang3.exception.ExceptionUtils;
import resources.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.Logger;

// the StrangeEons 'GameComponent' implementation
// the main purposes is to act as a bridge between StrangeEons and its specifics (AbstractGameComponentEditor, Sheet etc)
// and the AHLCG4J Card/CardView and associated face model/views
public class CardGameComponent extends AbstractGameComponent {
    static final long serialVersionUID = -6_569_100_078_755_650_503L;

    private static final Logger logger = Logger.getLogger(CardGameComponent.class.getName());

    private static final int CURRENT_VERSION = 1;

    private CardView cardView;

    // deserialisation constructor
    public CardGameComponent() {
    }

    // regular constructor
    public CardGameComponent(CardView cardView) {
        this.cardView = cardView;

        // TODO: name should be the actual title?
        setNameImpl("Card");
    }

    public CardView getCardView() {
        return cardView;
    }

    @Override
    public Sheet[] createDefaultSheets() {
        // create sheet objects that are provided by the card's face
        Sheet frontSheet = new CardSheet(this, cardView.getFrontFaceView());

        Sheet[] sheets;

        if (cardView.hasBack()) {
            Sheet backSheet = new CardSheet(this, cardView.getBackFaceView());
            sheets = new Sheet[]{frontSheet, backSheet};
        } else {
            sheets = new Sheet[]{frontSheet};
        }

        setSheets(sheets);

        return sheets;
    }

    @Override
    public AbstractGameComponentEditor<CardGameComponent> createDefaultEditor() {
        try {
            return new CardEditor(this);
        } catch (Exception e) {
            ErrorDialog.displayError("Error creating editor", e);
            logger.severe(e.getMessage() + ": " + ExceptionUtils.getStackTrace(e));
            throw e;
        }
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
        public double getBleedMargin() {
            return 0.0;
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
            } catch (Exception e) {
                ErrorDialog.displayError("Error painting sheet for card face", e);
                logger.severe(e.getMessage() + ": " + ExceptionUtils.getStackTrace(e));
                throw e;
            } finally {
                g.dispose();
            }
        }
    }

    static class PaintContextImpl implements PaintContext {
        private final Graphics2D g;
        private final RenderTarget renderTarget;
        private final Sheet<CardGameComponent> sheet;

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
        public MarkupRenderer createMarkupRenderer() {
            MarkupRenderer markupRenderer = new MarkupRenderer(sheet.getTemplateResolution());

            // TODO: need to set tag values for <title> to this face's title
            // TODO: need to set tag values for <fullname> and <fullnameb> for the front/back titles

            return markupRenderer;
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(CURRENT_VERSION);

        ObjectNode objectNode = JsonCardSerialiser.serialiseCard(cardView.getCard());
        StringWriter stringWriter = new StringWriter();
        createSerialisationObjectMapper().writeValue(stringWriter, objectNode);

        out.writeUTF(stringWriter.toString());

        // must remember to call this otherwise the file will show as unsaved in the Strange Eons UI
        markSaved();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int version = in.readInt();

        NewerVersionException.check(CURRENT_VERSION, version);

        // TODO: if the CURRENT_VERSION > version then do an upgrade
        // TODO: this should only pertain to major structural changes, e.g. if the content was previously encoded as JSON and we were switching to YAML
        // TODO: changes to individual card/faces should be handled later

        String jsonString = in.readUTF();
        ObjectNode objectNode = (ObjectNode) createSerialisationObjectMapper().readTree(new StringReader(jsonString));
        Card card = JsonCardSerialiser.deserialiseCard(objectNode);

        cardView = CardFaces.createCardView(card);

        // Strange Eons loves to interact with the settings all the time (even if it finds nothing)
        // create an empty one to avoid a bunch of null pointers
        privateSettings = new Settings();
        setNameImpl("Card");
    }

    private ObjectMapper createSerialisationObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }
}
