package com.mickeytheq.ahlcg4j.core;

import com.mickeytheq.ahlcg4j.core.model.CardFaceModel;
import com.mickeytheq.ahlcg4j.core.model.CardFaceType;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Asset;
import com.mickeytheq.ahlcg4j.core.view.cardfaces.EncounterCardBackView;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.PlayerCardBack;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Event;
import com.mickeytheq.ahlcg4j.core.view.cardfaces.EventView;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.Treachery;
import com.mickeytheq.ahlcg4j.core.view.cardfaces.TreacheryView;
import com.mickeytheq.ahlcg4j.core.view.CardFaceView;
import com.mickeytheq.ahlcg4j.core.view.cardfaces.AssetView;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.EncounterCardBack;
import com.mickeytheq.ahlcg4j.core.view.cardfaces.PlayerCardBackView;
import resources.Language;

import java.util.*;

public class CardFaceTypeRegister {
    private final Map<String, CardFaceInfo> typeCodeLookup = new HashMap<>();
    private final Map<Class<? extends CardFaceModel>, CardFaceInfo> modelClassLookup = new HashMap<>();

    private static final CardFaceTypeRegister cardFaceTypeRegister = new CardFaceTypeRegister();

    public static CardFaceTypeRegister get() {
        return cardFaceTypeRegister;
    }

    private CardFaceTypeRegister() {
        register(Asset.class, AssetView.class);
        register(Treachery.class, TreacheryView.class);
        register(Event.class, EventView.class);

        register(EncounterCardBack.class, EncounterCardBackView.class);
        register(PlayerCardBack.class, PlayerCardBackView.class);
    }

    private void register(Class<? extends CardFaceModel> cardFaceModelClass, Class<? extends CardFaceView> cardFaceViewClass) {
        CardFaceInfo cardFaceInfo = buildInfo(cardFaceModelClass, cardFaceViewClass);

        typeCodeLookup.put(cardFaceInfo.getTypeCode(), cardFaceInfo);
        modelClassLookup.put(cardFaceModelClass, cardFaceInfo);
    }

    public CardFaceInfo getInfoForCardFaceModelClass(Class<? extends CardFaceModel> cardFaceClass) {
        return Optional.ofNullable(modelClassLookup.get(cardFaceClass))
                .orElseThrow(() -> new NoSuchElementException("Card face model class '" + cardFaceClass.getName() + "' is not registered"));
    }

    public CardFaceInfo getInfoForTypeCode(String typeCode) {
        return Optional.ofNullable(typeCodeLookup.get(typeCode))
                .orElseThrow(() -> new NoSuchElementException("Type code '" + typeCode + "' is not registered"));
    }

    public List<CardFaceInfo> getAllCardInformation() {
        return new ArrayList<>(modelClassLookup.values());
    }

    private CardFaceInfo buildInfo(Class<? extends CardFaceModel> cardFaceModelClass, Class<? extends CardFaceView> cardFaceViewClass) {
        CardFaceType cardFaceType = cardFaceModelClass.getAnnotation(CardFaceType.class);

        if (cardFaceType == null)
            throw new RuntimeException("CardFaceModel implementation '" + cardFaceModelClass.getName() + "' does not have the CardFaceType annotation");

        CardFaceInfo cardFaceInfo = new CardFaceInfo(cardFaceModelClass, cardFaceViewClass, cardFaceType.typeCode(), cardFaceType.interfaceLanguageKey());

        return cardFaceInfo;
    }

    public static class CardFaceInfo {
        private final Class<? extends CardFaceModel> cardFaceModelClass;
        private final Class<? extends CardFaceView> cardFaceViewClass;
        private final String typeCode;
        private final String interfaceLanguageKey;

        public CardFaceInfo(Class<? extends CardFaceModel> cardFaceModelClass, Class<? extends CardFaceView> cardFaceViewClass, String typeCode, String interfaceLanguageKey) {
            this.cardFaceModelClass = cardFaceModelClass;
            this.cardFaceViewClass = cardFaceViewClass;
            this.typeCode = typeCode;
            this.interfaceLanguageKey = interfaceLanguageKey;
        }

        public Class<? extends CardFaceModel> getCardFaceModelClass() {
            return cardFaceModelClass;
        }

        public Class<? extends CardFaceView> getCardFaceViewClass() {
            return cardFaceViewClass;
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
            return Objects.equals(cardFaceModelClass, that.cardFaceModelClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cardFaceModelClass);
        }

        @Override
        public String toString() {
            return Language.string(getInterfaceLanguageKey());
        }
    }
}
