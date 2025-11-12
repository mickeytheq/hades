package com.mickeytheq.strangeeons.ahlcg4j;

import resources.Language;

import java.util.*;

public class CardFaceTypeRegister {
    private final Map<String, CardFaceInfo> typeCodeLookup = new HashMap<>();
    private final Map<Class<? extends CardFace>, CardFaceInfo> classLookup = new HashMap<>();

    private static final CardFaceTypeRegister cardFaceTypeRegister = new CardFaceTypeRegister();

    public static CardFaceTypeRegister get() {
        return cardFaceTypeRegister;
    }

    private CardFaceTypeRegister() {
        register(Asset.class);
        register(Treachery.class);

        register(EncounterCardBack.class);
        register(PlayerCardBack.class);
    }

    private void register(Class<? extends CardFace> cardFaceClass) {
        CardFaceInfo cardFaceInfo = buildInfo(cardFaceClass);

        typeCodeLookup.put(cardFaceInfo.getTypeCode(), cardFaceInfo);
        classLookup.put(cardFaceClass, cardFaceInfo);
    }

    public CardFaceInfo getInfoForCardFaceClass(Class<? extends CardFace> cardFaceClass) {
        return Optional.ofNullable(classLookup.get(cardFaceClass))
                .orElseThrow(() -> new NoSuchElementException("Card face class '" + cardFaceClass.getName() + "' is not registered"));
    }

    public CardFaceInfo getInfoForTypeCode(String typeCode) {
        return Optional.ofNullable(typeCodeLookup.get(typeCode))
                .orElseThrow(() -> new NoSuchElementException("Type code '" + typeCode + "' is not registered"));
    }

    public List<CardFaceInfo> getAllCardInformation() {
        return new ArrayList<>(classLookup.values());
    }

    private CardFaceInfo buildInfo(Class<? extends CardFace> cardFaceClass) {
        CardFaceType cardFaceType = cardFaceClass.getAnnotation(CardFaceType.class);

        if (cardFaceType == null)
            throw new RuntimeException("CardFace class '" + cardFaceClass.getName() + "' does not have the CardFaceType annotation");

        CardFaceInfo cardFaceInfo = new CardFaceInfo(cardFaceClass, cardFaceType.typeCode(), cardFaceType.interfaceLanguageKey());

        return cardFaceInfo;
    }

    public static class CardFaceInfo {
        private final Class<? extends CardFace> cardFaceClass;
        private final String typeCode;
        private final String interfaceLanguageKey;

        public CardFaceInfo(Class<? extends CardFace> cardFaceClass, String typeCode, String interfaceLanguageKey) {
            this.cardFaceClass = cardFaceClass;
            this.typeCode = typeCode;
            this.interfaceLanguageKey = interfaceLanguageKey;
        }

        public Class<? extends CardFace> getCardFaceClass() {
            return cardFaceClass;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public String getInterfaceLanguageKey() {
            return interfaceLanguageKey;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CardFaceInfo)) return false;
            CardFaceInfo that = (CardFaceInfo) o;
            return Objects.equals(cardFaceClass, that.cardFaceClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cardFaceClass);
        }

        @Override
        public String toString() {
            return Language.string(getInterfaceLanguageKey());
        }
    }
}
