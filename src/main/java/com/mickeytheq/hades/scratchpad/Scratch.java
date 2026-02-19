package com.mickeytheq.hades.scratchpad;

import com.mickeytheq.hades.util.svg.SvgUtils;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Scratch {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\micha\\Downloads\\skill-agility.svg"));

//        BufferedImage bufferedImage = png(fileInputStream);
        BufferedImage bufferedImage = svg(fileInputStream);

        int i = 1;
    }

    private static BufferedImage png(InputStream inputStream) throws Exception {

        // Create a PNG transcoder.
        Transcoder t = new PNGTranscoder();

        // Set the transcoding hints.
        t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 1000f);
        t.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 1000f);

        // Create the transcoder input.
        TranscoderInput input = new TranscoderInput(inputStream);

        ByteArrayOutputStream ostream = null;
        try {
            // Create the transcoder output.
            ostream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(ostream);

            // Save the image.
            t.transcode(input, output);

            // Flush and close the stream.
            ostream.flush();
            ostream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Convert the byte stream into an image.
        byte[] imgData = ostream.toByteArray();
        return ImageIO.read(new ByteArrayInputStream(imgData));
    }

    private static BufferedImage svg(InputStream inputStream) throws Exception {
        return SvgUtils.toBufferedImage(inputStream, 1000, 1000);
    }
}
