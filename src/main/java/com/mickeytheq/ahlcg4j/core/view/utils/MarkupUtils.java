package com.mickeytheq.ahlcg4j.core.view.utils;

import ca.cgjennings.layout.MarkupRenderer;
import com.mickeytheq.ahlcg4j.codegenerated.GameConstants;
import resources.Language;

import java.util.HashMap;
import java.util.Map;

public class MarkupUtils {
    private static final Map<String, String> BODY_TAG_REPLACEMENT = new HashMap<>();

    static {
        BODY_TAG_REPLACEMENT.put("HorizontalSpacer", "<image res://ArkhamHorrorLCG/images/empty1x1.png 0.12in 1pt>");
        BODY_TAG_REPLACEMENT.put("LargeVerticalSpacer", "<image res://ArkhamHorrorLCG/images/empty1x1.png 1pt 3.0pt>");
        BODY_TAG_REPLACEMENT.put("VerticalSpacer", "<image res://ArkhamHorrorLCG/images/empty1x1.png 1pt 1.5pt>");
        BODY_TAG_REPLACEMENT.put("SmallVerticalSpacer", "<image res://ArkhamHorrorLCG/images/empty1x1.png 1pt 0.5pt>");
        BODY_TAG_REPLACEMENT.put("act", "<ahf>n</ahf>");
        BODY_TAG_REPLACEMENT.put("rea", "<ahf>r</ahf>");
        BODY_TAG_REPLACEMENT.put("fre", "<ahf>f</ahf>");
        BODY_TAG_REPLACEMENT.put("dash", "<ahf>-</ahf>");
        BODY_TAG_REPLACEMENT.put("gua", "<ahf>G</ahf>");
        BODY_TAG_REPLACEMENT.put("see", "<ahf>K</ahf>");
        BODY_TAG_REPLACEMENT.put("rog", "<ahf>R</ahf>");
        BODY_TAG_REPLACEMENT.put("mys", "<ahf>M</ahf>");
        BODY_TAG_REPLACEMENT.put("sur", "<ahf>V</ahf>");
        BODY_TAG_REPLACEMENT.put("wil", "<ahf>w</ahf>");
        BODY_TAG_REPLACEMENT.put("int", "<ahf>i</ahf>");
        BODY_TAG_REPLACEMENT.put("com", "<ahf>c</ahf>");
        BODY_TAG_REPLACEMENT.put("agi", "<ahf>a</ahf>");
        BODY_TAG_REPLACEMENT.put("wild", "<ahf>?</ahf>");
        BODY_TAG_REPLACEMENT.put("sku", "<ahf>S</ahf>");
        BODY_TAG_REPLACEMENT.put("cul", "<ahf>C</ahf>");
        BODY_TAG_REPLACEMENT.put("tab", "<ahf>A</ahf>");
        BODY_TAG_REPLACEMENT.put("mon", "<ahf>L</ahf>");
        BODY_TAG_REPLACEMENT.put("eld", "<ahf>E</ahf>");
        BODY_TAG_REPLACEMENT.put("ten", "<ahf>T</ahf>");
        BODY_TAG_REPLACEMENT.put("ble", "<ahf>D</ahf>");
        BODY_TAG_REPLACEMENT.put("cur", "<ahf>U</ahf>");
        BODY_TAG_REPLACEMENT.put("fro", "<ahf>H</ahf>");
        BODY_TAG_REPLACEMENT.put("seal1", "<ahf>1</ahf>");
        BODY_TAG_REPLACEMENT.put("seal2", "<ahf>2</ahf>");
        BODY_TAG_REPLACEMENT.put("seal3", "<ahf>3</ahf>");
        BODY_TAG_REPLACEMENT.put("seal4", "<ahf>4</ahf>");
        BODY_TAG_REPLACEMENT.put("seal5", "<ahf>5</ahf>");
        BODY_TAG_REPLACEMENT.put("ast", "<ahf>*</ahf>");
        BODY_TAG_REPLACEMENT.put("uni", "<ahf>u</ahf>");
        BODY_TAG_REPLACEMENT.put("per", "<ahf>p</ahf>");
        BODY_TAG_REPLACEMENT.put("bul", "<ahf>b</ahf>");
        BODY_TAG_REPLACEMENT.put("gbul", "<ahf><boxbullet>B</boxbullet></ahf>");
        BODY_TAG_REPLACEMENT.put("squ", "<ahf>s</ahf>");
        BODY_TAG_REPLACEMENT.put("res", "#AHLCG-Resolution-tag");
        BODY_TAG_REPLACEMENT.put("/res", "</hdr>");
        BODY_TAG_REPLACEMENT.put("entry1", "<image res://ArkhamHorrorLCG/images/EntryLine.png 0.5in 8pt bottom>");
        BODY_TAG_REPLACEMENT.put("entry2", "<image res://ArkhamHorrorLCG/images/EntryLine.png 0.75in 8pt bottom>");
        BODY_TAG_REPLACEMENT.put("entry3", "<image res://ArkhamHorrorLCG/images/EntryLine.png 1.0in 8pt bottom>");
        BODY_TAG_REPLACEMENT.put("entry4", "<image res://ArkhamHorrorLCG/images/EntryLine.png 1.25in 8pt bottom>");
        BODY_TAG_REPLACEMENT.put("entry5", "<image res://ArkhamHorrorLCG/images/EntryLine.png 1.5in 8pt bottom>");

        // TODO: there are two sets of declarations in the game settings but one overwrites the other and they are the same
        // TODO: probably used in different contexts (maybe one for Arno and one for non as the images referenced below include Arno?)
//        BODY_TAG_REPLACEMENT.put("TextEntry1A", "<image res://ArkhamHorrorLCG/images/EntryLineArno.png 0.5in 8pt bottom>");
//        BODY_TAG_REPLACEMENT.put("TextEntry2A", "<image res://ArkhamHorrorLCG/images/EntryLineArno.png 0.75in 8pt bottom>");
//        BODY_TAG_REPLACEMENT.put("TextEntry3A", "<image res://ArkhamHorrorLCG/images/EntryLineArno.png 1.0in 8pt bottom>");
//        BODY_TAG_REPLACEMENT.put("TextEntry4A", "<image res://ArkhamHorrorLCG/images/EntryLineArno.png 1.25in 8pt bottom>");
//        BODY_TAG_REPLACEMENT.put("TextEntry5A", "<image res://ArkhamHorrorLCG/images/EntryLineArno.png 1.5in 8pt bottom>");
        BODY_TAG_REPLACEMENT.put("pre", Language.gstring(GameConstants.PREY_TAG));
        BODY_TAG_REPLACEMENT.put("spa", Language.gstring(GameConstants.SPAWN_TAG));
        BODY_TAG_REPLACEMENT.put("rev", Language.gstring(GameConstants.REVELATION_TAG));
        BODY_TAG_REPLACEMENT.put("for", Language.gstring(GameConstants.FORCED_TAG));
        BODY_TAG_REPLACEMENT.put("obj", Language.gstring(GameConstants.OBJECTIVE_TAG));
        BODY_TAG_REPLACEMENT.put("hau", Language.gstring(GameConstants.HAUNTED_TAG));
        BODY_TAG_REPLACEMENT.put("shi", Language.gstring(GameConstants.SHIFT_TAG));
        BODY_TAG_REPLACEMENT.put("cop", "\u00a9");
        BODY_TAG_REPLACEMENT.put("xpbox", "<family>\u29e0</family>");

        // TODO: similarly there's two xp boxes and check boxes declared, probably for use in different contexts
//        BODY_TAG_REPLACEMENT.put("XPBoxA", "\u2610");

        BODY_TAG_REPLACEMENT.put("cbox", "<family>\u29e0</family>");
//        BODY_TAG_REPLACEMENT.put("CheckBoxA", "\u2610");
    }

    public static void applyTagMarkupConfiguration(MarkupRenderer markupRenderer) {

        BODY_TAG_REPLACEMENT.forEach(markupRenderer::setReplacementForTag);

        markupRenderer.setStyleForTag("ahf", TextStyleUtils.getArkhamHorrorFontTextStyle());
        markupRenderer.setStyleForTag("ts", TextStyleUtils.getTraitTextStyle());
        markupRenderer.setStyleForTag("vic", TextStyleUtils.getVictoryTextStyle());
        markupRenderer.setStyleForTag("fs", TextStyleUtils.getFlavorTextStyle());
        markupRenderer.setStyleForTag("t", TextStyleUtils.getTraitTextStyle());
        markupRenderer.setStyleForTag("hdr", TextStyleUtils.getHeaderTextStyle());
        markupRenderer.setStyleForTag("fla", TextStyleUtils.getFlavorTextStyle());
        markupRenderer.setStyleForTag("sto", TextStyleUtils.getStoryTextStyle());
        markupRenderer.setStyleForTag("bod", TextStyleUtils.getBodyTextStyle());

        // TODO: missing tags with styles - iss, css, gss, section, header, boxbullet, suf, sufb
    }

    // the res:// protocol maps into a 'resources' folder in the root of the classpath which is why
    // the targeted file is in resources (the actual classpath root) /resources (the above folder)/spacers
    public static String getSpacerMarkup(double horizontalPoints, double verticalPoints) {
        return "<image res://spacers/empty1x1.png " + horizontalPoints + "pt " + verticalPoints + "pt>";
    }
}
