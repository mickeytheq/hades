package com.mickeytheq.hades.strangeeons.ahlcg.migration;

import ca.cgjennings.apps.arkham.component.DefaultPortrait;
import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.view.CardFaceSide;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class PortraitUtils {

    // collection
    public static final String COLLECTION_PORTRAIT_SUFFIX = "-Collection";
    public static final String USER_COLLECTION_ICON_PREFIX = "AHLCG-UserCollectionIcon";
    public static final String ENCOUNTER_SET_PORTRAIT_SUFFIX = "-Encounter";
    public static final String USER_ENCOUNTER_SET_ICON_PREFIX = "AHLCG-UserEncounterIcon";

    public static DefaultPortrait getCollectionPortrait(DIY diy) {
        return getDefaultPortrait(diy, COLLECTION_PORTRAIT_SUFFIX);
    }

    public static Optional<DefaultPortrait> findCollectionPortrait(DIY diy) {
        return findDefaultPortrait(diy, COLLECTION_PORTRAIT_SUFFIX);
    }

    public static DefaultPortrait getEncounterSetPortrait(DIY diy) {
        return getDefaultPortrait(diy, ENCOUNTER_SET_PORTRAIT_SUFFIX);
    }

    public static Optional<DefaultPortrait> findEncounterSetPortrait(DIY diy) {
        return findDefaultPortrait(diy, ENCOUNTER_SET_PORTRAIT_SUFFIX);
    }

    public static List<DefaultPortrait> getDefaultPortraits(DIY diy) {
        return IntStream.range(0, diy.getPortraitCount())
                .mapToObj(diy::getPortrait)
                .filter(o -> o instanceof DefaultPortrait)
                .map(o -> (DefaultPortrait)o)
                .collect(Collectors.toList());
    }

    public static DefaultPortrait getArtPortrait(DIY diy, CardFaceSide cardFaceSide) {
        String templateKey = cardFaceSide == CardFaceSide.Front ? diy.getFrontTemplateKey() : diy.getBackTemplateKey();

        if (StringUtils.isEmpty(templateKey))
            throw new RuntimeException("DIY component '" + diy.getName() + "' has no template key for card face side " + cardFaceSide.name());

        // deconstruct the template key by removing the standard prefix and the suffix
        // this should leave the card type which will match the corresponding portrait
        String temp = Strings.CS.removeStart(templateKey, "AHLCG-");
        String cardType = StringUtils.substring(temp, 0, Strings.CS.lastIndexOf(temp, "-"));

        // portrait keys are of the form AHLCG-<card type>-<suffix>
        // so we can use the card type we've found above to locate the corresponding template
        // also have to exclude the collection and encounter set portraits which share the same card type as the front of the card
        List<DefaultPortrait> matchingPortraits = getDefaultPortraits(diy).stream()
                .filter(o -> o.getBaseKey().contains("-" + cardType + "-"))
                .filter(o -> !o.getBaseKey().endsWith(COLLECTION_PORTRAIT_SUFFIX))
                .filter(o -> !o.getBaseKey().endsWith(ENCOUNTER_SET_PORTRAIT_SUFFIX))
                .collect(Collectors.toList());

        if (matchingPortraits.size() > 1)
            throw new RuntimeException("Multiple art portraits found for diy '" + diy.getName() + "' when matching using card type '" + cardType + "'");

        if (matchingPortraits.isEmpty())
            throw new RuntimeException("No art portrait found for diy '" + diy.getName() + "' when matching using card type '" + cardType + "'");

        return matchingPortraits.get(0);
    }

    public static DefaultPortrait getDefaultPortrait(DIY diy, String portraitKeySuffix) {
        Optional<DefaultPortrait> portraitMatch = getDefaultPortraits(diy).stream().filter(o -> o.getBaseKey().endsWith(portraitKeySuffix)).findFirst();

        if (!portraitMatch.isPresent())
            throw new RuntimeException("Could not find portrait with key suffix " + portraitKeySuffix + " in DIY object " + diy);

        return portraitMatch.get();
    }

    public static Optional<DefaultPortrait> findDefaultPortrait(DIY diy, String portraitKeySuffix) {
        Optional<DefaultPortrait> portraitMatch = getDefaultPortraits(diy).stream().filter(o -> o.getBaseKey().endsWith(portraitKeySuffix)).findFirst();

        return portraitMatch;
    }
}
