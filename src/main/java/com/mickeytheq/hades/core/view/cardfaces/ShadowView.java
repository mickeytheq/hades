package com.mickeytheq.hades.core.view.cardfaces;

import ca.cgjennings.apps.arkham.StrangeEons;
import com.google.common.collect.Lists;
import com.mickeytheq.hades.codegenerated.InterfaceConstants;
import com.mickeytheq.hades.core.Cards;
import com.mickeytheq.hades.core.global.carddatabase.CardDatabase;
import com.mickeytheq.hades.core.global.carddatabase.CardDatabases;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.cardfaces.Shadow;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.*;
import com.mickeytheq.hades.core.view.PaintContext;
import com.mickeytheq.hades.core.view.utils.EditorUtils;
import com.mickeytheq.hades.core.view.utils.MigLayoutUtils;
import com.mickeytheq.hades.ui.CardChooser;
import resources.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

@View(interfaceLanguageKey = InterfaceConstants.SHADOW)
public class ShadowView extends BaseCardFaceView<Shadow> {
    private CardFaceView shadowingFaceView;

    private static final TemplateInfo NO_SHADOW_TEMPLATE = new TemplateInfoImpl(createNoShadowTemplateImage());

    @Override
    public void initialiseView() {
        refreshShadowView();
    }

    @Override
    public Optional<TemplateInfo> getCompatibleTemplateInfo(int desiredResolutionInPpi) {
        if (shadowingFaceView == null)
            return Optional.of(NO_SHADOW_TEMPLATE);

        return shadowingFaceView.getCompatibleTemplateInfo(desiredResolutionInPpi);
    }

    @Override
    protected List<TemplateInfo> getAvailableTemplateInfos() {
        return null;
    }

    @Override
    public String getTitle() {
        if (shadowingFaceView == null)
            return null;

        return Language.string(InterfaceConstants.SHADOW) + " - " + shadowingFaceView.getTitle();
    }

    @Override
    public void createEditors(EditorContext editorContext) {
        CardChooser cardChooser = new CardChooser(StrangeEons.getOpenProject().getFile().toPath());

        JComboBox<CardFaceSide> cardFaceSideComboBox = EditorUtils.createEnumComboBox(CardFaceSide.class);

        cardChooser.addActionListener(e -> {
            Card shadowingCard = cardChooser.getSelectedCard();
            getModel().setShadowCardId(shadowingCard.getId());
            refreshShadowView();

            editorContext.markChanged();
        });

        EditorUtils.bindComboBox(cardFaceSideComboBox, editorContext.wrapConsumerWithMarkedChanged(cardFaceSide -> {
            getModel().setShadowSide(cardFaceSide);
            refreshShadowView();
        }));

        if (getModel().getShadowCardId() != null) {
            Optional<Card> card = CardDatabases.getCardDatabase().getCardWithId(getModel().getShadowCardId());

            card.ifPresent(cardChooser::setSelectedCard);
        }

        cardFaceSideComboBox.setSelectedItem(getModel().getShadowSide());

        JPanel controlPanel = MigLayoutUtils.createTitledPanel(Language.string(InterfaceConstants.GENERAL));
        MigLayoutUtils.addLabelledComponentWrapGrowPush(controlPanel, "Card to shadow:", cardChooser);
        MigLayoutUtils.addLabelledComponentWrapGrowPush(controlPanel, "Face to shadow:", cardFaceSideComboBox);

        JPanel mainPanel = MigLayoutUtils.createVerticalFlowOrganiserPanel(controlPanel);

        editorContext.addDisplayComponent(Language.string(InterfaceConstants.GENERAL), mainPanel);
    }

    @Override
    public void paint(PaintContext paintContext) {
        if (shadowingFaceView == null) {
            paintContext.paintTemplate();
            return;
        }

        shadowingFaceView.paint(paintContext);
    }

    private void refreshShadowView() {
        CardDatabase cardDatabase = CardDatabases.getCardDatabase();

        // may be null on a new card face of this type - there is no sensible default
        if (getModel().getShadowCardId() == null)
            return;

        Optional<Card> shadowingCardOpt = cardDatabase.getCardWithId(getModel().getShadowCardId());

        if (!shadowingCardOpt.isPresent())
            throw new RuntimeException("The card being shadowed is not available");

        Card shadowingCard = shadowingCardOpt.get();

        // shadowing is only allowed within projects so the project context will be the same
        ProjectContext shadowingProjectContext = getCardView().getProjectContext();

        CardView shadowingView = Cards.createCardView(shadowingCard, shadowingProjectContext);

        if (getModel().getShadowSide() == CardFaceSide.Front) {
            shadowingFaceView = shadowingView.getFrontFaceView();
        }
        else {
            if (!shadowingView.hasBack())
                throw new RuntimeException("Card being shadowed does not have a Back face");

            shadowingFaceView = shadowingView.getBackFaceView();
        }
    }

    public static BufferedImage createNoShadowTemplateImage() {
        List<String> textStrings = Lists.newArrayList("Select card to be shadowed");

        BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        try {
            graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            graphics2D.setColor(new Color(255, 200, 200));
            graphics2D.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

            graphics2D.setColor(Color.BLACK);

            graphics2D.setFont(graphics2D.getFont().deriveFont(24.0f));

            int yPos = 100;

            for (String textString : textStrings) {
                int width = graphics2D.getFontMetrics().stringWidth(textString);
                int height = graphics2D.getFontMetrics().getHeight();

                graphics2D.drawString(textString, bufferedImage.getWidth() / 2 - width / 2, yPos);

                yPos = (int)(yPos + height * 1.5f);
            }
        }
        finally {
            graphics2D.dispose();
        }

        return bufferedImage;
    }

    static class TemplateInfoImpl implements TemplateInfo {
        private final BufferedImage image;

        public TemplateInfoImpl(BufferedImage image) {
            this.image = image;
        }

        @Override
        public int getWidthInPixels() {
            return image.getWidth();
        }

        @Override
        public int getHeightInPixels() {
            return image.getHeight();
        }

        @Override
        public int getResolutionInPixelsPerInch() {
            return 300;
        }

        @Override
        public int getAvailableBleedMarginInPixels() {
            return 0;
        }

        @Override
        public double getAvailableBleedMarginInPoints() {
            return 0;
        }

        @Override
        public BufferedImage getTemplateImage() {
            return image;
        }

        @Override
        public TemplateInfo scaleToResolution(int newPpi) {
            return this;
        }
    }
}
