package com.mickeytheq.hades.core.view.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mickeytheq.hades.util.svg.SvgUtils;
import org.apache.commons.collections4.keyvalue.MultiKey;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class ImageUtils {
    private static final LoadingCache<URL, BufferedImage> IMAGE_CACHE;
    private static final LoadingCache<SvgImageKey, BufferedImage> SVG_IMAGE_CACHE;

    static {
        IMAGE_CACHE = CacheBuilder.newBuilder()
                .softValues()
                .build(CacheLoader.from(ImageUtils::loadImageWithNoCaching));

        // the svg image cache needs to key on width/height as images are scaled from the SVG raw
        // TODO: instead of doing the scaling here could instead have the SVG document loaded here and
        // TODO: return a container instead of a BufferedImage. the container would then perform the scaling as needed
        SVG_IMAGE_CACHE = CacheBuilder.newBuilder()
                .softValues()
                .build(CacheLoader.from(ImageUtils::loadSvgImageWithNoCachingUsingKey));
    }

    public static final Image HADES_PURPLE_H_IMAGE = ImageUtils.loadImageReadOnly("/icons/hades-purple-h.png").getScaledInstance(18, 18, Image.SCALE_SMOOTH);
    public static final Icon HADES_PURPLE_H_ICON = new ImageIcon(HADES_PURPLE_H_IMAGE);
    public static final URL PER_INVESTIGATOR_ICON_RESOURCE = ImageUtils.class.getResource("/icons/AHLCG-PerInvestigator.png");
    public static final URL UNIQUE_STAR_ICON_RESOURCE = ImageUtils.class.getResource("/icons/AHLCG-Unique.png");
    public static final URL EMPTY_IMAGE = ImageUtils.class.getResource("/resources/spacers/empty1x1.png");

    // load an image that can be used for read-only purposes
    // the image returned may be a shared cached copy so must not be altered
    // if a write image is required use deepCopy() on the returned value
    public static BufferedImage loadImageReadOnly(String absoluteResourcePath) {
        URL resourceUrl = ImageUtils.class.getResource(absoluteResourcePath);

        if (resourceUrl == null)
            throw new RuntimeException("Image at absolute resource path '" + absoluteResourcePath + "' does not exist");

        return loadImageReadOnly(resourceUrl);
    }

    // loads an image, caching on the URL as a key
    // see above about read-only conditions of the returned image
    public static BufferedImage loadImageReadOnly(URL urlToImage) {
        if (urlToImage == null)
            throw new NullPointerException("URL passed to loadImage() was null");

        return IMAGE_CACHE.getUnchecked(urlToImage);
    }

    public static BufferedImage loadSvgImageReadOnly(String absoluteResourcePath, int width, int height) {
        URL resourceUrl = ImageUtils.class.getResource(absoluteResourcePath);

        if (resourceUrl == null)
            throw new RuntimeException("Image at absolute resource path '" + absoluteResourcePath + "' does not exist");

        return SVG_IMAGE_CACHE.getUnchecked(new SvgImageKey(resourceUrl, width, height));
    }

    public static BufferedImage loadImageWritable(String absoluteResourcePath) {
        return deepCopy(loadImageReadOnly(absoluteResourcePath));
    }

    public static BufferedImage loadImageWritable(URL urlToImage) {
        return deepCopy(loadImageReadOnly(urlToImage));
    }

    public static BufferedImage deepCopy(BufferedImage toCopy) {
        ColorModel cm = toCopy.getColorModel();
        boolean isAlphaPreMultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = toCopy.copyData(toCopy.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPreMultiplied, null);
    }

    public static BufferedImage loadImageWithNoCaching(URL urlToImage) {
        try {
            return ImageIO.read(urlToImage);
        } catch (IOException e) {
            throw new RuntimeException("Error reading image from URL: " + urlToImage.toExternalForm(), e);
        }
    }

    private static BufferedImage loadSvgImageWithNoCachingUsingKey(SvgImageKey svgImageKey) {
        return loadSvgImageWithNoCaching(svgImageKey.getUrl(), svgImageKey.getWidth(), svgImageKey.getHeight());
    }

    public static BufferedImage loadSvgImageWithNoCaching(URL urlToImage, int width, int height) {
        try (InputStream inputStream = urlToImage.openStream()) {
            return SvgUtils.toBufferedImage(inputStream, width, height);
        } catch (IOException e) {
            throw new RuntimeException("Error reading SVG image from URL: " + urlToImage.toExternalForm(), e);
        }
    }

    public static final URL BASIC_WEAKNESS_ICON_RESOURCE = ImageUtils.class.getResource("/icons/AHLCG-BasicWeakness.png");

    private static class SvgImageKey {
        private final URL url;
        private final int width;
        private final int height;

        public SvgImageKey(URL url, int width, int height) {
            this.url = url;
            this.width = width;
            this.height = height;
        }

        public URL getUrl() {
            return url;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof SvgImageKey)) return false;
            SvgImageKey that = (SvgImageKey) object;
            return width == that.width && height == that.height && Objects.equals(url, that.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(url, width, height);
        }
    }
}
