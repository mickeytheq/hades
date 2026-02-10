package com.mickeytheq.hades.scratchpad;

import com.google.common.collect.Lists;
import com.mickeytheq.hades.strangeeons.plugin.Bootstrapper;
import com.mickeytheq.hades.ui.LoggingLevel;
import com.mickeytheq.hades.ui.ProgressDialog;
import com.mickeytheq.hades.util.log4j.GlobalMemoryAppender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Scratch {
    public static void main(String[] args) throws Exception {
        String str = "YESURTF";

        str = translateWord(str, -8);

        System.out.println(str);
    }

    private static List<String> untranslatedWords = Lists.newArrayList(
            "THE", "UNJHJX", "YHKF", "N", "XIPMF",
            "YMJ", "LTQJR", "PBMRF", "MH", "AXUT",
            "NY", "MJYUEM", "BML", "GTFX",
            "RJYFQ", "YAEMUO"
    );

    private static void translateCyclicRotatingCipher() {
        int shift = 0;
        for (String untranslatedWord : untranslatedWords) {
            String translated = translateWord(untranslatedWord, shift);
            System.out.println(translated);

            int lastChar = translated.charAt(translated.length() - 1);

            shift = (lastChar - 'A') % 26 + 1;
        }
    }

    private static String translateWord(String untranslated, int shift) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < untranslated.length(); i++) {
            int newChar = shiftChar(untranslated.charAt(i), shift);
            sb.append((char)newChar);
        }

        return sb.toString();
    }

    private static int shiftChar(int ch, int shift) {
        ch = ch - shift;

        if (ch < 'A')
            ch = ch + 26;

        if (ch > 'Z')
            ch = ch - 26;

        return ch;
    }
}
