package com.mickeytheq.hades.core.view.utils;

import ca.cgjennings.layout.MarkupRenderer;
import ca.cgjennings.layout.TextStyle;
import com.mickeytheq.hades.core.view.PaintContext;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

// a renderer that can take multiple rendering 'sections' and automatically scale them in equal amounts
// to fit an overall region
// this is only designed to handle vertical scaling, e.g. sections of text on top of each other such as a story card or agenda/act back
// it works by
// - asking each section to provide its starting/desired height
// - checking if the total exceeds the available space
// - asking each section that supports scaling to reduce its size
// - finally asking each section to draw itself
public class MultiSectionRenderer {
    private final PaintContext paintContext;
    private final Rectangle drawRegion;

    private final List<Section> sections = new ArrayList<>();

    public MultiSectionRenderer(PaintContext paintContext, Rectangle drawRegion) {
        this.paintContext = paintContext;
        this.drawRegion = drawRegion;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void draw() {
        Map<Section, Double> lastCalculatedHeights = new IdentityHashMap<>();

        // calculate the total height that is fixed/cannot be scaled
        double unscalableHeight = sections.stream()
                .filter(o -> !o.isScalable())
                .mapToDouble(o -> o.calculateHeight(paintContext.getGraphics(), drawRegion))
                .sum();

        do {
            // calculate height for each section
            for (Section section : sections) {
                double sectionHeight = section.calculateHeight(paintContext.getGraphics(), drawRegion);
                lastCalculatedHeights.put(section, sectionHeight);
            }

            double totalHeight = lastCalculatedHeights.values().stream().mapToDouble(value -> value).sum();

            if (totalHeight < drawRegion.getHeight())
                break;

            // adjust scaling of all scalable sections by the same factor and try again
            // TODO: currently this uses a basic 95% repeated reduction until a suitable size is found
            // TODO: could be improved by doing a binary-search-esque approach with the initial bounds being 0.0 to 1.0
            //
            // TODO: may also be better, rather than looping here, to calculate the amount of height each scalable section
            // TODO: can have and passing that to changeScale() and have the scale down loop performed there with the
            // TODO: expectation that the resulting draw be as close to, but not exceeding, the height it is given as possible
            sections.stream().filter(Section::isScalable).forEach(o -> o.changeScale(0.95));
        } while (true);

        double currentYOffset = 0.0;

        // adjustment complete, now draw
        for (Section section : sections) {
            double textSectionHeight = (Double)lastCalculatedHeights.get(section);

            Rectangle sectionDrawRegion = new Rectangle(
                    (int)drawRegion.getX(), (int)(drawRegion.getY() + currentYOffset),
                    (int)drawRegion.getWidth(), (int) textSectionHeight);

            section.draw(paintContext.getGraphics(), sectionDrawRegion);

            currentYOffset = currentYOffset + textSectionHeight;
        }
    }

    public interface Section {
        double calculateHeight(Graphics2D g, Rectangle drawRegion);

        boolean isScalable();

        void changeScale(double scale);

        void draw(Graphics2D g, Rectangle drawRegion);
    }

    public static class VerticalSpacerSection implements Section {
        private final int verticalSpace;

        public VerticalSpacerSection(int verticalSpace) {
            this.verticalSpace = verticalSpace;
        }

        @Override
        public double calculateHeight(Graphics2D g, Rectangle drawRegion) {
            return verticalSpace;
        }

        @Override
        public boolean isScalable() {
            return false;
        }

        @Override
        public void changeScale(double scale) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void draw(Graphics2D g, Rectangle drawRegion) {
        }
    }

    public static class HorizontalLineSection implements Section {
        private final int lineVerticalSize;

        public HorizontalLineSection(int lineVerticalSize) {
            this.lineVerticalSize = lineVerticalSize;
        }

        @Override
        public double calculateHeight(Graphics2D g, Rectangle drawRegion) {
            return lineVerticalSize;
        }

        @Override
        public boolean isScalable() {
            return false;
        }

        @Override
        public void changeScale(double scale) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void draw(Graphics2D g, Rectangle drawRegion) {
            Graphics2D copy = (Graphics2D)g.create();

            copy.setPaint(Color.BLACK);
            copy.setStroke(new BasicStroke(lineVerticalSize));
            copy.drawLine((int) drawRegion.getX(), (int) drawRegion.getY(), (int) (drawRegion.getX() + drawRegion.getWidth()), (int) drawRegion.getY());
        }
    }

    public static class TextSection implements Section {
        private final String text;
        private final int alignment;
        private final double dpi;
        private final TextStyle textStyle;

        public TextSection(String text, TextStyle originalTextStyle, int alignment, double dpi) {
            this.text = text;

            // clone the TextStyle as we might change the values
            textStyle = new TextStyle();
            textStyle.add(originalTextStyle);

            this.alignment = alignment;
            this.dpi = dpi;
        }

        public String getText() {
            return text;
        }

        public TextStyle getTextStyle() {
            return textStyle;
        }

        @Override
        public double calculateHeight(Graphics2D g, Rectangle drawRegion) {
            MarkupRenderer markupRenderer = createMarkupRenderer();

            return markupRenderer.measure(g, drawRegion);
        }

        @Override
        public boolean isScalable() {
            return true;
        }

        @Override
        public void changeScale(double scale) {
            Number number = (Number) textStyle.get(TextAttribute.SIZE);

            textStyle.add(TextAttribute.SIZE, number.doubleValue() * scale);
        }

        @Override
        public void draw(Graphics2D g, Rectangle drawRegion) {
            MarkupRenderer markupRenderer = createMarkupRenderer();
            markupRenderer.draw(g, drawRegion);
        }

        protected MarkupRenderer createMarkupRenderer() {
            MarkupRenderer markupRenderer = new MarkupRenderer(dpi);
            markupRenderer.setDefaultStyle(textStyle);
            markupRenderer.setAlignment(alignment);
            markupRenderer.setMarkupText(text);
            markupRenderer.setLineTightness(0.6f);
            markupRenderer.setTextFitting(MarkupRenderer.FIT_NONE);

            return markupRenderer;
        }
    }

    public static class StoryTextSection extends TextSection {
        private final int leftIndent;

        public StoryTextSection(String text, TextStyle originalTextStyle, int alignment, double dpi, int leftIndent) {
            super(text, originalTextStyle, alignment, dpi);
            this.leftIndent = leftIndent;
        }

        @Override
        public double calculateHeight(Graphics2D g, Rectangle drawRegion) {
            return super.calculateHeight(g, getAdjustedDrawRegion(drawRegion));
        }

        @Override
        public void draw(Graphics2D g, Rectangle drawRegion) {
            super.draw(g, getAdjustedDrawRegion(drawRegion));

            // draw the two vertical lines down the side of the text
            Graphics2D copy = (Graphics2D) g.create();
            copy.setPaint(Color.BLACK);
            copy.setStroke(new BasicStroke(2));

            AttributedString attributedString = new AttributedString("y");
            getTextStyle().applyStyle(attributedString);

            copy.drawLine(drawRegion.x, drawRegion.y, drawRegion.x, drawRegion.y + (int)drawRegion.getHeight());
            copy.drawLine(drawRegion.x + 5, drawRegion.y, drawRegion.x + 5, drawRegion.y + (int)drawRegion.getHeight());
        }

        private Rectangle getAdjustedDrawRegion(Rectangle drawRegion) {
            Rectangle adjustedDrawRegion = drawRegion.getBounds();

            adjustedDrawRegion.width = adjustedDrawRegion.width - leftIndent;
            adjustedDrawRegion.x = adjustedDrawRegion.x + leftIndent;

            return adjustedDrawRegion;
        }
    }
}
