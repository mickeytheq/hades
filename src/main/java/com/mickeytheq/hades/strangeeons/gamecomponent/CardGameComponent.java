package com.mickeytheq.hades.strangeeons.gamecomponent;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;
import ca.cgjennings.apps.arkham.component.AbstractGameComponent;
import ca.cgjennings.apps.arkham.dialog.ErrorDialog;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.BasePaintContext;
import com.mickeytheq.hades.core.view.CardFaceView;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.core.view.PaintContext;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;

// the StrangeEons 'GameComponent' implementation
// the main purposes is to act as a bridge between StrangeEons and its specifics (AbstractGameComponentEditor, Sheet etc)
// and the Hades Card/CardView and associated face model/views
public class CardGameComponent extends AbstractGameComponent {
    private static final Logger logger = LogManager.getLogger(CardGameComponent.class);

    private final CardView cardView;
    private final ProjectContext projectContext;

    public CardGameComponent(CardView cardView, ProjectContext projectContext) {
        this.cardView = cardView;
        this.projectContext = projectContext;

        // TODO: name should be the actual title?
        setNameImpl("Card");
    }

    public CardView getCardView() {
        return cardView;
    }

    public ProjectContext getProjectContext() {
        return projectContext;
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

        private Dimension currentDimension;

        public CardSheet(CardGameComponent gameComponent, CardFaceView cardFaceView) {
            super(gameComponent);

            this.cardFaceView = cardFaceView;

            currentDimension = cardFaceView.getDimension();

            // the only things the 'template' image is used for in our scenario is the
            BufferedImage template = new BufferedImage((int) currentDimension.getWidth(), (int) currentDimension.getHeight(), BufferedImage.TYPE_INT_ARGB);

            // BUG: strange eons code has a bug in the parameter checking of initializeTemplate where the class variable is checked for null
            // rather than the parameter being passed in. workaround this by setting it first
            setExpansionSymbolKey("dummy");
            initializeTemplate("dummy", template, "dummy", 300.0, 1.0);
        }

        @Override
        public double getBleedMargin() {
            return 0.0;
        }

        @Override
        protected void paintSheet(RenderTarget renderTarget) {
            Dimension newDimension = cardFaceView.getDimension();

            // check for dimension change - not common but if it has happened we need to update the 'template image' of the sheet
            // so the new painting will use the correct size
            // this will occur if a card face has variable dimensions based on editor settings, for example a ShadowView
            if (!newDimension.equals(currentDimension)) {
                currentDimension = newDimension;

                BufferedImage template = new BufferedImage((int) currentDimension.getWidth(), (int) currentDimension.getHeight(), BufferedImage.TYPE_INT_ARGB);
                replaceTemplateImage(template);
            }

            StopWatch stopWatch = StopWatch.createStarted();

            Graphics2D g = createGraphics();
            try {
                // the graphics object created is on top of the existing image
                // so we have to clear it first to paint onto a blank canvas
                g.clearRect(0, 0, getTemplateWidth(), getTemplateHeight());

                PaintContext paintContext = new PaintContextImpl(g, renderTarget, cardFaceView, this);

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

    class PaintContextImpl extends BasePaintContext {
        private final Graphics2D g;
        private final Sheet<CardGameComponent> sheet;

        public PaintContextImpl(Graphics2D g, RenderTarget renderTarget, CardFaceView cardFaceView, Sheet<CardGameComponent> sheet) {
            super(renderTarget, cardFaceView);
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

        @Override
        public ProjectContext getProjectContext() {
            return projectContext;
        }
    }
}
