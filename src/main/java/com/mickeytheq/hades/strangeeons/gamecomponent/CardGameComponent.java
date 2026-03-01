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
        throw new UnsupportedOperationException("Sheets are not supported");
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
}
