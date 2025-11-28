package com.mickeytheq.strangeeons.ahlcg4j;

import ca.cgjennings.apps.arkham.AbstractGameComponentEditor;
import ca.cgjennings.apps.arkham.StrangeEons;
import ca.cgjennings.apps.arkham.component.AbstractGameComponent;
import ca.cgjennings.apps.arkham.component.GameComponent;
import ca.cgjennings.apps.arkham.sheet.Sheet;
import ca.cgjennings.io.NewerVersionException;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.CardFaceModel;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.CardFaceView;
import resources.Settings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

// TODO: refactor this to
// - put the serialisation/construction logic of a set of views/models in a utility where it can be used outside of a card being constructed/read inside Strange Eons, e.g. testing
// - further de-couple the Card concept from StrangeEons by having a very light 'Card' to join the two faces together and have a CardGameComponent to be the SE bridge to allow
//   the Card model/view to be used outside SE, e.g. testing. ideally the model/view becomes somewhat SE agnostic/unaware
public class Card extends AbstractGameComponent {
    private static final int CURRENT_VERSION = 1;

    private CardFaceModel frontFaceModel;
    private CardFaceView frontFaceView;
    private CardFaceModel backFaceModel;
    private CardFaceView backFaceView;

    public Card() {
        NewCardDialog newCardDialog = new NewCardDialog(false);
        newCardDialog.setLocationRelativeTo(StrangeEons.getWindow());
        newCardDialog.setVisible(true);

        frontFaceModel = createCardFaceModel(newCardDialog.getSelectedFrontFace().getCardFaceModelClass());
        frontFaceModel.initialiseModel(this, CardFaceSide.Front);

        frontFaceView = createViewForModel(frontFaceModel);

        if (newCardDialog.getSelectedBackFace() != null) {
            backFaceModel = createCardFaceModel(newCardDialog.getSelectedBackFace().getCardFaceModelClass());
            backFaceModel.initialiseModel(this, CardFaceSide.Back);

            backFaceView = createViewForModel(backFaceModel);
        }
    }

    public Card(CardFaceView frontFaceView, CardFaceModel frontFaceModel, CardFaceView backFaceView, CardFaceModel backFaceModel) {
        // constructor used for testing
        this.frontFaceView = frontFaceView;
        this.frontFaceModel = frontFaceModel;

        this.backFaceView = backFaceView;
        this.backFaceModel = backFaceModel;
    }

    public CardFaceModel getFrontFaceModel() {
        return frontFaceModel;
    }

    public CardFaceModel getBackFaceModel() {
        return backFaceModel;
    }

    public CardFaceView getFrontFaceView() {
        return frontFaceView;
    }

    public CardFaceView getBackFaceView() {
        return backFaceView;
    }

    @Override
    public Sheet[] createDefaultSheets() {
        // create sheet objects that are provided by the card's face
        Sheet frontSheet = getFrontFaceView().createSheet();
        Sheet backSheet = getBackFaceView().createSheet();

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

        writeFaceSettings(frontFaceModel, CardFaceSide.Front);
        writeFaceSettings(backFaceModel, CardFaceSide.Back);

        out.writeObject(privateSettings);

        markSaved();
    }

    private void writeFaceSettings(CardFaceModel cardFaceModel, CardFaceSide cardFaceSide) {
        String type = CardFaceTypeRegister.get().getInfoForCardFaceModelClass(cardFaceModel.getClass()).getTypeCode();

        getSettings().set(cardFaceSide.getSettingsPrefix() + ".Type", type);

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int version = in.readInt();

        NewerVersionException.check(CURRENT_VERSION, version);

        // TODO: do we need to store the name? a card is just two faces so the less 'Card' level fields the better
        setNameImpl((String)in.readObject());
        comments = (String)in.readObject();

        privateSettings = (Settings)in.readObject();

        frontFaceModel = readFace(CardFaceSide.Front, in);
        frontFaceView = createViewForModel(frontFaceModel);
        backFaceModel = readFace(CardFaceSide.Back, in);
        backFaceView = createViewForModel(backFaceModel);
    }

    private CardFaceModel readFace(CardFaceSide cardFaceSide, ObjectInputStream objectInputStream) {
        String settingsKey = cardFaceSide.getSettingsPrefix() + ".Type";

        String type = getSettings().get(settingsKey);

        Class<? extends CardFaceModel> cardFaceClass = CardFaceTypeRegister.get().getInfoForTypeCode(type).getCardFaceModelClass();

        CardFaceModel cardFaceModel = createCardFaceModel(cardFaceClass);
        cardFaceModel.initialiseModel(this, cardFaceSide);

        return cardFaceModel;
    }

    private static CardFaceModel createCardFaceModel(Class<? extends CardFaceModel> cardFaceClass) {
        Constructor<? extends CardFaceModel> constructor;
        try {
            constructor = cardFaceClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The required single argument 'Card' constructor is not present on CardFace class '" + cardFaceClass.getName() + "'");
        }

        CardFaceModel cardFaceModel;
        try {
            cardFaceModel = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return cardFaceModel;
    }

    private static CardFaceView createViewForModel(CardFaceModel cardFaceModel) {
        CardFaceTypeRegister.CardFaceInfo cardFaceInfo = CardFaceTypeRegister.get().getInfoForCardFaceModelClass(cardFaceModel.getClass());

        CardFaceView cardFaceView = createCardFaceView(cardFaceInfo.getCardFaceViewClass());
        cardFaceView.initialiseView(cardFaceModel);

        return cardFaceView;
    }

    private static CardFaceView createCardFaceView(Class<? extends CardFaceView> cardFaceViewClass) {
        Constructor<? extends CardFaceView> constructor;
        try {
            constructor = cardFaceViewClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The required single argument 'Card' constructor is not present on CardFaceView class '" + cardFaceViewClass.getName() + "'");
        }

        CardFaceView cardFaceView;
        try {
            cardFaceView = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return cardFaceView;
    }
}
