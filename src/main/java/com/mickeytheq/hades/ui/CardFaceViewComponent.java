package com.mickeytheq.hades.ui;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import com.mickeytheq.hades.core.view.CardFaceView;
import com.mickeytheq.hades.core.view.TemplateInfo;
import com.mickeytheq.hades.core.view.utils.CardFaceViewUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

// swing component that renders a card face
public class CardFaceViewComponent extends JPanel {
    private final CardFaceView cardFaceView;
    private final TemplateInfo templateInfo;

    public CardFaceViewComponent(CardFaceView cardFaceView, int desiredResolution) {
        this.cardFaceView = cardFaceView;

        Optional<TemplateInfo> templateInfo = cardFaceView.getCompatibleTemplateInfo(desiredResolution);

        if (!templateInfo.isPresent()) {
            throw new RuntimeException("No template available for card face view with resolution " + desiredResolution);
        }

        this.templateInfo = templateInfo.get();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D)graphics;
        Paint oldPaint = g.getPaint();
        try {
            g.setPaint(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        finally {
            g.setPaint(oldPaint);
        }

        if (cardFaceView == null)
            return;

        BufferedImage bufferedImage = CardFaceViewUtils.paintCardFace(cardFaceView, RenderTarget.PREVIEW, templateInfo.getResolutionInPixelsPerInch());

        int x = (getWidth() - bufferedImage.getWidth()) / 2;
        int y = (getHeight() - bufferedImage.getHeight()) / 2;

        g.drawImage(bufferedImage, x, y, null);
    }

    @Override
    public Dimension getPreferredSize() {
        Optional<TemplateInfo> templateInfoOptional = cardFaceView.getCompatibleTemplateInfo(300);

        if (!templateInfoOptional.isPresent())
            throw new RuntimeException("No template image available");

        return new Dimension(templateInfoOptional.get().getWidthInPixels(), templateInfoOptional.get().getHeightInPixels());
    }
}
