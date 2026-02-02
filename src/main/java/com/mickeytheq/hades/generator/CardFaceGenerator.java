package com.mickeytheq.hades.generator;

import com.mickeytheq.hades.core.model.cardfaces.Asset;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// utility for generating card faces with random content
public class CardFaceGenerator {
    private final ProjectContext projectContext;
    private final Random random = new Random();

    public CardFaceGenerator(ProjectContext projectContext) {
        this.projectContext = projectContext;
    }

    public Asset createAsset() {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            Asset asset = new Asset();
            asset.getAssetFieldsModel().setSlot1(randomEnum(Asset.AssetSlot.class));
            asset.getAssetFieldsModel().setSlot2(randomEnum(Asset.AssetSlot.class));
            asset.getAssetFieldsModel().setHealth(randomStatistic(3));
            asset.getAssetFieldsModel().setSanity(randomStatistic(3));

            randomlyPopulateCommonCardFieldsModel(asset.getCommonCardFieldsModel());
            randomlyPopulatePlayerCardFieldsModel(asset.getPlayerCardFieldsModel());

            return asset;
        });
    }

    private <E extends Enum<E>> E randomEnum(Class<E> enumClass) {
        E[] constants = enumClass.getEnumConstants();

        return constants[random.nextInt(constants.length)];
    }

    private Statistic randomStatistic(int maxValue) {
        return new Statistic(Integer.toString(random.nextInt(maxValue) + 1), random.nextBoolean());
    }

    private String randomString(int maxWords, int maxWordLength) {
        return IntStream.range(1, random.nextInt(maxWords) + 1)
                .mapToObj(i -> RandomStringUtils.insecure().nextAlphabetic(maxWordLength))
                .collect(Collectors.joining(" "));
    }

    private void randomlyPopulateCommonCardFieldsModel(CommonCardFieldsModel model) {
        model.setTitle(randomString(5, 5));
        model.setSubtitle(randomString(3, 5));
        model.setUnique(random.nextBoolean());
        model.setRules(randomString(50, 8));
        model.setCopyright(randomString(2, 8));
        model.setKeywords(randomString(3, 8));
        model.setFlavourText(randomString(20, 6));
        model.setTraits(randomString(3, 8));

        if (random.nextInt(5) == 0)
            model.setVictory("Victory " + random.nextInt(2) + 1);
    }

    private void randomlyPopulatePlayerCardFieldsModel(PlayerCardFieldsModel model) {
        model.setLevel(random.nextInt(6));
        model.setCardType(randomEnum(PlayerCardType.class));
        model.setCost(Integer.toString(random.nextInt(5)));

        model.setPlayerCardClasses(IntStream.range(0, random.nextInt(3) + 1).mapToObj(o -> randomEnum(PlayerCardClass.class)).collect(Collectors.toList()));
        model.setSkillIcons(IntStream.range(0, random.nextInt(6) + 1).mapToObj(o -> randomEnum(PlayerCardSkillIcon.class)).collect(Collectors.toList()));
    }
}
