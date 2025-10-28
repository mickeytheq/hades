package com.mickeytheq.strangeeons.ahlcg4j;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CardFaceTypeRegister {
    private final Map<String, Class<? extends CardFace>> typeCodeToClassMap = new HashMap<>();
    private final Map<Class<? extends CardFace>, String> classToTypeCodeMap = new HashMap<>();

    private static final CardFaceTypeRegister cardFaceTypeRegister = new CardFaceTypeRegister();

    public static CardFaceTypeRegister get() {
        return cardFaceTypeRegister;
    }

    private CardFaceTypeRegister() {
        register(Treachery.class);
        register(EncounterCardBack.class);
    }

    private void register(Class<? extends CardFace> cardFaceClass) {
        CardFaceType cardFaceType = cardFaceClass.getAnnotation(CardFaceType.class);

        if (cardFaceType == null)
            throw new RuntimeException("CardFace class '" + cardFaceClass.getName() + "' does not have the CardFaceType annotation");

        String settingsTypeCode = cardFaceType.settingsTypeCode();

        typeCodeToClassMap.put(settingsTypeCode, cardFaceClass);
        classToTypeCodeMap.put(cardFaceClass, settingsTypeCode);
    }

    public String getSettingsTypeCodeForCardFaceClass(Class<? extends CardFace> cardFaceClass) {
        return Optional.ofNullable(classToTypeCodeMap.get(cardFaceClass))
                .orElseThrow(() -> new NoSuchElementException("Card face class '" + cardFaceClass.getName() + "' is not registered"));
    }

    public Class<? extends CardFace> getCardFaceClassForSettingsTypeCode(String typeCode) {
        return Optional.ofNullable(typeCodeToClassMap.get(typeCode))
                .orElseThrow(() -> new NoSuchElementException("Type code '" + typeCode + "' is not registered"));
    }
}
