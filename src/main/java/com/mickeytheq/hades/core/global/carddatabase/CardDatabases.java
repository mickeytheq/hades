package com.mickeytheq.hades.core.global.carddatabase;

public class CardDatabases {
    private static CardDatabase cardDatabase = new CardDatabaseImpl();

    // used for testing only
    public static void set(CardDatabase cardDatabase) {
        CardDatabases.cardDatabase = cardDatabase;
    }

    public static CardDatabase getCardDatabase() {
        return cardDatabase;
    }
}
