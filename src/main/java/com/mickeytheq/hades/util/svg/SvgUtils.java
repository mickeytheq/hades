package com.mickeytheq.hades.util.svg;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class SvgUtils {
    public static BufferedImage toBufferedImage(InputStream inputStream, int width, int height) {
        SVGUniverse universe = new SVGUniverse();
        URI uri;
        try {
            uri = universe.loadSVG(inputStream, "IMAGE");
        } catch (IOException e) {
            throw new RuntimeException("Error loading SVG image", e);
        }
        SVGDiagram diagram = universe.getDiagram(uri);
        diagram.setIgnoringClipHeuristic(true);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            float diagramWidth = diagram.getWidth();
            float diagramHeight = diagram.getHeight();

            double scale = calculateScale( new Dimension((int) diagramWidth, (int) diagramHeight), new Dimension(width, height) );

            AffineTransform transform = g.getTransform();
            transform.setToScale(scale, scale);

            g.setTransform(transform);

            diagram.render(g);
        } catch (SVGException e) {
            throw new RuntimeException(e);
        } finally {
            g.dispose();
        }

        return bufferedImage;
    }

    private static double calculateScale( final Dimension src, final Dimension dst ) {
        double srcWidth = src.getWidth();
        double srcHeight = src.getHeight();

        return Math.min(dst.getWidth() / srcWidth, dst.getHeight() / srcHeight);
    }
}
