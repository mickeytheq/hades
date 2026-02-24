package com.mickeytheq.hades.scratchpad;

import ca.cgjennings.graphics.filters.TintFilter;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.strangeeons.ui.ExportCardDialog;
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
import java.nio.file.Files;

public class Scratch {
    public static void main(String[] args) throws Exception {
        BufferedImage image = SvgUtils.toBufferedImage(Scratch.class.getResourceAsStream("/overlays/location/symbols/triangle.svg"), 100, 100);
        int i = 1;
    }

}
