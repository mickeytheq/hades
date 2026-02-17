package com.mickeytheq.hades.strangeeons.gamecomponent;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;
import ca.cgjennings.apps.arkham.component.AbstractGameComponent;
import ca.cgjennings.apps.arkham.dialog.ErrorDialog;
import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import com.mickeytheq.hades.core.global.configuration.CardPreviewConfiguration;
import com.mickeytheq.hades.core.global.configuration.GlobalConfigurations;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.utils.CardFaceViewUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

// the StrangeEons 'GameComponent' implementation
// the main purposes is to act as a bridge between StrangeEons and its specifics (AbstractGameComponentEditor, Sheet etc)
// and the Hades Card/CardView and associated face model/views
public class CardGameComponent extends AbstractGameComponent {
    private static final Logger logger = LogManager.getLogger(CardGameComponent.class);

    private final CardView cardView;

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

    // this class is partially defunct as the CardEditor implements previews without using the Sheet concept.
    // however core Strange Eons concepts such as export and print are currently tied to use Sheets
    // it is possible to replace these functions in the CardEditor by overriding export/print methods at which
    // point this may be able to be removed entirely
    private static class CardSheet extends Sheet<CardGameComponent> {
        private final CardFaceView cardFaceView;

        private final int desiredResolutionInPpi;

        private TemplateInfo currentTemplateInfo;

        public CardSheet(CardGameComponent gameComponent, CardFaceView cardFaceView) {
            super(gameComponent);

            this.cardFaceView = cardFaceView;

            CardPreviewConfiguration cardPreviewConfiguration = GlobalConfigurations.get().getCardPreviewConfiguration();

            this.desiredResolutionInPpi = cardPreviewConfiguration.getDesiredPreviewResolutionPpi();

            // ask the CardFaceView to provide template information for that PPI - dimension, available bleed margin etc
            Optional<TemplateInfo> templateInfoOptional = cardFaceView.getCompatibleTemplateInfo(desiredResolutionInPpi);

            // if no template is suitable then show an error picture instead
            if (!templateInfoOptional.isPresent()) {
                setTemplate(CardFaceViewUtils.createMissingTemplateImage(cardFaceView, desiredResolutionInPpi), desiredResolutionInPpi);
                return;
            }

            currentTemplateInfo = templateInfoOptional.get();

            setTemplate(currentTemplateInfo);

            // TODO: set user bleed margin
        }

        private void setTemplate(TemplateInfo templateInfo) {
            // the only things the 'template' image is used for in our scenario is the size but the Sheet API requires a full image
            BufferedImage template = new BufferedImage(templateInfo.getWidthInPixels(), templateInfo.getHeightInPixels(), BufferedImage.TYPE_INT_ARGB);

            setTemplate(template, desiredResolutionInPpi);
        }

        private void setTemplate(BufferedImage template, int ppi) {
            // BUG: strange eons code has a bug in the parameter checking of initializeTemplate where the class variable is checked for null
            // rather than the parameter being passed in. workaround this by setting it first
            setExpansionSymbolKey("dummy");
            initializeTemplate("dummy", template, "dummy", ppi, 1.0);
        }

        @Override
        public double getBleedMargin() {
            if (currentTemplateInfo == null)
                return 0.0;

            return currentTemplateInfo.getAvailableBleedMarginInPoints();
        }

        private boolean needToRecreateTemplateBuffer(TemplateInfo templateInfo) {
            if (currentTemplateInfo.getHeightInPixels() != templateInfo.getHeightInPixels())
                return true;

            if (currentTemplateInfo.getWidthInPixels() != templateInfo.getWidthInPixels())
                return true;

            return false;
        }

        @Override
        protected void paintSheet(RenderTarget renderTarget) {
            Optional<TemplateInfo> templateInfoOptional = cardFaceView.getCompatibleTemplateInfo(desiredResolutionInPpi);

            if (!templateInfoOptional.isPresent()) {
                paintNoTemplateImage();
                return;
            }

            TemplateInfo templateInfo = templateInfoOptional.get();

            // check for dimension change - not common but if it has happened we need to update the 'template image' of the sheet
            // so the new painting will use the correct size
            // this will occur if a card face has variable dimensions based on editor settings, for example a ShadowView
            if (needToRecreateTemplateBuffer(templateInfo)) {
                currentTemplateInfo = templateInfo;

                setTemplate(currentTemplateInfo);
            }

            StopWatch stopWatch = StopWatch.createStarted();

            Graphics2D g = createGraphics();
            try {
                // the graphics object created is on top of the existing image
                // so we have to clear it first to paint onto a blank canvas
                g.clearRect(0, 0, getTemplateWidth(), getTemplateHeight());

                PaintContext paintContext = new DefaultPaintContext(g, renderTarget, cardFaceView, currentTemplateInfo);

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

        private void paintNoTemplateImage() {
            Graphics2D g = createGraphics();
            try {
                g.clearRect(0, 0, getTemplateWidth(), getTemplateHeight());
                BufferedImage missingTemplateImage = CardFaceViewUtils.createMissingTemplateImage(cardFaceView, desiredResolutionInPpi);
                g.drawImage(missingTemplateImage, 0, 0, missingTemplateImage.getWidth(), missingTemplateImage.getHeight(), null);
            } catch (Exception e) {
                ErrorDialog.displayError("Error painting sheet for card face", e);
                logger.error(e.getMessage(), e);
                throw e;
            } finally {
                g.dispose();
            }
        }
    }
}
