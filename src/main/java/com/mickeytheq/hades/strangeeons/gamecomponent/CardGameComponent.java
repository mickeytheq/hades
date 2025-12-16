package com.mickeytheq.hades.strangeeons.gamecomponent;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;
import ca.cgjennings.apps.arkham.component.AbstractGameComponent;
import ca.cgjennings.apps.arkham.dialog.ErrorDialog;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import ca.cgjennings.io.NewerVersionException;
import ca.cgjennings.layout.MarkupRenderer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Charsets;
import com.mickeytheq.hades.core.CardFaces;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.view.BasePaintContext;
import com.mickeytheq.hades.core.view.CardFaceView;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.serialise.JsonCardSerialiser;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.util.JsonUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resources.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

// the StrangeEons 'GameComponent' implementation
// the main purposes is to act as a bridge between StrangeEons and its specifics (AbstractGameComponentEditor, Sheet etc)
// and the Hades Card/CardView and associated face model/views
public class CardGameComponent extends AbstractGameComponent {
    private static final Logger logger = LogManager.getLogger(CardGameComponent.class);

    private CardView cardView;

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
            StopWatch stopWatch = StopWatch.createStarted();

            CardEditor cardEditor = new CardEditor(this);

            if (logger.isTraceEnabled()) {
                logger.trace("Opening of card editor: " + cardView.getBriefDisplayString() + " completed in " + stopWatch.getTime() + "ms");
            }

            return cardEditor;
        } catch (Exception e) {
            ErrorDialog.displayError("Error creating editor", e);
            logger.error(e.getMessage(), e);
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
            StopWatch stopWatch = StopWatch.createStarted();

            Graphics2D g = createGraphics();
            try {
                // the graphics object created is on top of the existing image
                // so we have to clear it first to paint onto a blank canvas
                g.clearRect(0, 0, getTemplateWidth(), getTemplateHeight());

                PaintContext paintContext = new PaintContextImpl(g, renderTarget, getCardView(), this);

                // delegate to the card view to do the painting
                cardFaceView.paint(paintContext);

                if (logger.isTraceEnabled()) {
                    logger.trace("Painting of card face " + cardFaceView.getBriefDisplayString() + " completed in " + stopWatch.getTime() + "ms");
                }
            } catch (Exception e) {
                ErrorDialog.displayError("Error painting sheet for card face", e);
                logger.error(e.getMessage(), e);
                throw e;
            } finally {
                g.dispose();
            }
        }
    }

    static class PaintContextImpl extends BasePaintContext {
        private final Graphics2D g;
        private final Sheet<CardGameComponent> sheet;

        public PaintContextImpl(Graphics2D g, RenderTarget renderTarget, CardView cardView, Sheet<CardGameComponent> sheet) {
            super(renderTarget, cardView);
            this.g = g;
            this.sheet = sheet;
        }

        @Override
        public Graphics2D getGraphics() {
            return g;
        }

        @Override
        public double getRenderingDpi() {
            return sheet.getTemplateResolution();
        }
    }
}
