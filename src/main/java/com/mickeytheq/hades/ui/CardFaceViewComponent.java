package com.mickeytheq.hades.ui;

import ca.cgjennings.apps.arkham.sheet.RenderTarget;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.BasePaintContext;
import com.mickeytheq.hades.core.view.CardFaceView;
import com.mickeytheq.hades.core.view.CardView;
import com.mickeytheq.hades.scratchpad.QuickCardView;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// swing component that renders a card face
public class CardFaceViewComponent extends JPanel {
    private final CardFaceView cardFaceView;

    public CardFaceViewComponent(CardFaceView cardFaceView) {
        this.cardFaceView = cardFaceView;
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

        BufferedImage bufferedImage = new BufferedImage((int) cardFaceView.getDimension().getWidth(), (int) cardFaceView.getDimension().getHeight(), BufferedImage.TYPE_INT_ARGB);
        cardFaceView.paint(new PaintContextImpl(RenderTarget.PREVIEW, cardFaceView.getCardView(), bufferedImage));

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int x = (getWidth() - bufferedImage.getWidth()) / 2;
        int y = (getHeight() - bufferedImage.getHeight()) / 2;

        g.drawImage(bufferedImage, x, y, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return cardFaceView.getDimension();
    }

    private class PaintContextImpl extends BasePaintContext {
        private final Graphics2D graphics2D;
        private final double dpi = 300;

        public PaintContextImpl(RenderTarget renderTarget, CardView cardView, BufferedImage bufferedImage) {
            super(renderTarget, cardView);
            this.graphics2D = bufferedImage.createGraphics();

            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }

        @Override
        public Graphics2D getGraphics() {
            return graphics2D;
        }

        @Override
        public double getRenderingDpi() {
            return dpi;
        }

        @Override
        public ProjectContext getProjectContext() {
            return cardFaceView.getCardView().getProjectContext();
        }
    }
}
