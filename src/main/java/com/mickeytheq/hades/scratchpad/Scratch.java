package com.mickeytheq.hades.scratchpad;

import ca.cgjennings.graphics.filters.TintFilter;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.util.svg.SvgUtils;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.*;

public class Scratch {
    static final BufferedImage LOCATION_BASE_IMAGE = ImageUtils.loadImageReadOnly("/icons/location/location_base.png");
    public static void main(String[] args) throws Exception {

        generateColour("triangle_alt", 50.0f / 360f, 0.81f, 0.88f);
        generateColour("cross_alt", -2.0f / 360f, 0.83f, 0.73f);
        generateColour("diamond_alt", -150.0f / 360f, 0.51f, 0.45f);
        generateColour("slash_alt", 8.0f / 360f, 0.75f, 0.51f);
        generateColour("t_alt", 98.0f / 360f, 0.61f, 0.63f);
        generateColour("hourglass_alt", 40.0f / 360f, 0.64f, 0.56f);
        generateColour("moon_alt", -123.0f / 360f, 0.49f, 0.34f);
        generateColour("double_slash_alt", -20.0f / 360f, 0.65f, 0.40f);
        generateColour("heart_alt", -25.0f / 360f, 0.61f, 0.61f);
        generateColour("star_alt", 131.0f / 360f, 0.44f, 0.34f);
        generateColour("circle_alt", 24.0f / 360f, 0.82f, 0.83f);
        generateColour("square_alt", -71.0f / 360f, 0.46f, 0.33f);
    }

    private static void generateColour(String name, float h, float s, float b) {
        TintFilter tintFilter = new TintFilter(h, s, b);
        BufferedImage tintedImage = tintFilter.filter(LOCATION_BASE_IMAGE, null);
        int rgb = tintedImage.getRGB(tintedImage.getWidth() / 2, tintedImage.getHeight() / 2);
        rgb = rgb & 0x00FFFFFF;
        String hex = Integer.toHexString(rgb).toUpperCase();

        System.out.println(name + " " + hex);
    }

}
