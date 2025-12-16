package com.mickeytheq.hades.core.view.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class ImageUtils {
    private static final LoadingCache<URL, BufferedImage> IMAGE_CACHE;

    static {
        IMAGE_CACHE = CacheBuilder.newBuilder()
                .softValues()
                .build(CacheLoader.from(ImageUtils::loadImageWithNoCaching));
    }

    public static final Icon HADES_PURPLE_H_ICON = new ImageIcon(ImageUtils.loadImage("/icons/hades-purple-h.png").getScaledInstance(18, 18, Image.SCALE_SMOOTH));
    public static final URL PER_INVESTIGATOR_ICON_RESOURCE = ImageUtils.class.getResource("/icons/AHLCG-PerInvestigator.png");
    public static final URL UNIQUE_STAR_ICON_RESOURCE = ImageUtils.class.getResource("/icons/AHLCG-Unique.png");

    public static BufferedImage loadImage(String absoluteResourcePath) {
        URL resourceUrl = ImageUtils.class.getResource(absoluteResourcePath);

        if (resourceUrl == null)
            throw new RuntimeException("Image at absolute resource path '" + absoluteResourcePath + "' does not exist");

        return loadImage(resourceUrl);
    }

    // loads an image, caching on the URL as a key
    public static BufferedImage loadImage(URL urlToImage) {
        if (urlToImage == null)
            throw new NullPointerException("URL passed to loadImage() was null");

        BufferedImage bufferedImage = IMAGE_CACHE.getUnchecked(urlToImage);

        // BufferedImage is mutable so pass a copy back to the caller to avoid
        // damaging the cache state
        return deepCopy(bufferedImage);
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

    public static void drawImage(Graphics2D g, BufferedImage bufferedImage, Rectangle rectangle) {
        g.drawImage(bufferedImage, (int)rectangle.getX(), (int)rectangle.getY(), (int)rectangle.getWidth(), (int)rectangle.getHeight(), null);
    }

    public static final URL BASIC_WEAKNESS_ICON_RESOURCE = ImageUtils.class.getResource("/icons/AHLCG-BasicWeakness.png");

}
