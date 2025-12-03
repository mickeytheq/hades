package com.mickeytheq.ahlcg4j.core;

import com.mickeytheq.ahlcg4j.core.model.CardFaceModel;
import com.mickeytheq.ahlcg4j.core.model.Model;
import com.mickeytheq.ahlcg4j.core.model.cardfaces.*;
import com.mickeytheq.ahlcg4j.core.view.View;
import com.mickeytheq.ahlcg4j.core.view.cardfaces.*;
import com.mickeytheq.ahlcg4j.core.view.CardFaceView;
import resources.Language;

import java.util.*;

// register of all model and view types with look-ups on key fields
public class CardFaceTypeRegister {
    private final Map<String, CardFaceInfo> typeCodeLookup = new HashMap<>();
    private final Map<Class<? extends CardFaceModel>, CardFaceInfo> modelClassLookup = new HashMap<>();

    private static final CardFaceTypeRegister cardFaceTypeRegister = new CardFaceTypeRegister();

    public static CardFaceTypeRegister get() {
        return cardFaceTypeRegister;
    }

    private CardFaceTypeRegister() {
        register(Investigator.class, InvestigatorView.class);
        register(InvestigatorBack.class, InvestigatorBackView.class);

        register(Asset.class, AssetView.class);
        register(Event.class, EventView.class);

        register(Treachery.class, TreacheryView.class);

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
        Model model = cardFaceModelClass.getAnnotation(Model.class);

        if (model == null)
            throw new RuntimeException("Model class '" + cardFaceModelClass.getName() + "' does not have the @Model annotation");

        View view = cardFaceViewClass.getAnnotation(View.class);

        if (view == null)
            throw new RuntimeException("View class '" + cardFaceViewClass.getName() + "' does not have the @View annotation");

        CardFaceInfo cardFaceInfo = new CardFaceInfo(cardFaceModelClass, cardFaceViewClass, model.typeCode(), view.interfaceLanguageKey());

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
