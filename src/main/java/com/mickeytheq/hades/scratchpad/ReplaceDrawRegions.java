package com.mickeytheq.hades.scratchpad;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceDrawRegions {
    final static String prefix = "_DRAW_REGION = new Rectangle\\(";
    final static String newPrefix = "_DRAW_REGION = RectangleEx.millimeters(";
    final static Pattern pattern = Pattern.compile("(.+) Rectangle (.+)" + prefix + "\\s*(\\d+),\\s*(\\d+),\\s*(\\d+),\\s*(\\d+)\\s*\\);(.*)");

    public static void main(String[] args) throws Exception {
//        String line = "private static final Rectangle ENCOUNTER_PORTRAIT_DRAW_REGION = new Rectangle(658, 20, 60, 60);";
//        String newLine = replaceLine(line);

        new ReplaceDrawRegions().run();
    }

    private void run() throws Exception {
        Files.list(Paths.get("D:\\Dev\\Projects\\AHLCG4J\\src\\main\\java\\com\\mickeytheq\\hades\\core\\view\\common\\"))
                .forEach(this::replaceFile);
    }

    private void replaceFile(Path path) {
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8))) {
                lines.forEach(o -> writer.println(replaceLine(o)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String replaceLine(String line) {
        Matcher matcher = pattern.matcher(line);

        if (!matcher.matches())
            return line;

        StringBuilder sb = new StringBuilder();
        sb.append(matcher.group(1));
        sb.append(" RectangleEx ");
        sb.append(matcher.group(2));
        sb.append(newPrefix);

        for (int i = 3; i<= 5; i++) {
            sb.append(convertUnit(matcher.group(i)));
            sb.append(", ");
        }
        sb.append(convertUnit(matcher.group(6)));
        sb.append(");");

        if (matcher.groupCount() >= 7)
            sb.append(matcher.group(7));

        return sb.toString();
    }

    private static String convertUnit(String unitStr) {
        int pixelUnit = Integer.parseInt(unitStr);

        double mmUnit = (double)pixelUnit / 300.0 * 25.4;

        return String.format("%.2f", mmUnit);
    }
}
