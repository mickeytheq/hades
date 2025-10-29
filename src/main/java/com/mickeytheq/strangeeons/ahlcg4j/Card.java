package com.mickeytheq.strangeeons.ahlcg4j;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;
import ca.cgjennings.apps.arkham.component.AbstractGameComponent;
import ca.cgjennings.apps.arkham.component.GameComponent;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import ca.cgjennings.io.NewerVersionException;
import resources.Settings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Card extends AbstractGameComponent {
    private static final int CURRENT_VERSION = 1;

    private transient CardFace frontFace;
    private transient CardFace backFace;

    public Card() {
        // TODO: launch a dialog/wizard to specify the front/back faces
        // TODO: have flexibility of choice provided the front/back are the same size

        // TODO: create two CardFace or similar objects to represent the front and back

        frontFace = new Treachery();
        frontFace.initialise(this, CardFaceSide.Front);
        backFace = new EncounterCardBack();
        backFace.initialise(this, CardFaceSide.Back);
    }

    public CardFace getFrontFace() {
        return frontFace;
    }

    public CardFace getBackFace() {
        return backFace;
    }

    @Override
    public Sheet[] createDefaultSheets() {
        // create sheet objects that are provided by the card's face
        Sheet frontSheet = getFrontFace().createSheet();
        Sheet backSheet = getBackFace().createSheet();

        Sheet[] sheets = new Sheet[]{frontSheet, backSheet};

        setSheets(sheets);

        return sheets;
    }

    @Override
    public AbstractGameComponentEditor<? extends GameComponent> createDefaultEditor() {
        return new CardEditor(this);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(CURRENT_VERSION);

        out.writeObject(getName());
        out.writeObject(comments);

        writeFaceSettings(frontFace, CardFaceSide.Front);
        writeFaceSettings(backFace, CardFaceSide.Back);

        out.writeObject(privateSettings);

        frontFace.afterSettingsWrite(out);
        backFace.afterSettingsWrite(out);
    }

    private void writeFaceSettings(CardFace cardFace, CardFaceSide cardFaceSide) {
        String type = CardFaceTypeRegister.get().getSettingsTypeCodeForCardFaceClass(cardFace.getClass());

        getSettings().set(cardFaceSide.getSettingsPrefix() + ".Type", type);

        cardFace.beforeSettingsWrite(getSettings());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int version = in.readInt();

        NewerVersionException.check(CURRENT_VERSION, version);

        setNameImpl((String)in.readObject());
        comments = (String)in.readObject();

        privateSettings = (Settings)in.readObject();

        frontFace = readFace(CardFaceSide.Front, in);
        backFace = readFace(CardFaceSide.Back, in);

        // TODO: allow the card faces to participate in the object stream?
    }

    private CardFace readFace(CardFaceSide cardFaceSide, ObjectInputStream objectInputStream) {
        String settingsKey = cardFaceSide.getSettingsPrefix() + ".Type";

        String type = getSettings().get(settingsKey);

        Class<? extends CardFace> cardFaceClass = CardFaceTypeRegister.get().getCardFaceClassForSettingsTypeCode(type);

        Constructor<? extends CardFace> constructor;
        try {
            constructor = cardFaceClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The required single argument 'Card' constructor is not present on CardFace class '" + cardFaceClass.getName() + "'");
        }

        CardFace cardFace;
        try {
            cardFace = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        cardFace.initialise(this, cardFaceSide);

        cardFace.afterSettingsRead(getSettings(), objectInputStream);

        return cardFace;
    }
}
