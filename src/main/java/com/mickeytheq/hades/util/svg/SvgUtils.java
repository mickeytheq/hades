package com.mickeytheq.hades.util.svg;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.ext.awt.RenderingHintsKeyExt;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class SvgUtils {
    // converts an SVG input stream into a BufferedImage scaled to the desired size
    public static BufferedImage toBufferedImage(InputStream inputStream, int width, int height) {
        TranscoderInput transcoderInput = new TranscoderInput(inputStream);
        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, (float)width);
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, (float)height);

        try {
            transcoder.transcode(transcoderInput, null);
        } catch (TranscoderException e) {
            throw new RuntimeException(e);
        }

        return transcoder.getLastRendered();
    }

    private static class BufferedImageTranscoder extends ImageTranscoder {
        private BufferedImage lastRendered;

        @Override
        public BufferedImage createImage(int width, int height) {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        @Override
        public void writeImage(BufferedImage bufferedImage, TranscoderOutput transcoderOutput) throws TranscoderException {
            lastRendered = bufferedImage;
        }

        public BufferedImage getLastRendered() {
            return lastRendered;
        }
    }
}
