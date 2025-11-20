package com.mickeytheq.strangeeons.ahlcg4j.cardfaces.treachery;

import ca.cgjennings.apps.arkham.component.DefaultPortrait;
import com.mickeytheq.strangeeons.ahlcg4j.cardfaces.*;
import com.mickeytheq.strangeeons.ahlcg4j.CardFaceType;
import com.mickeytheq.strangeeons.ahlcg4j.WeaknessType;
import com.mickeytheq.strangeeons.ahlcg4j.codegenerated.InterfaceConstants;
import org.checkerframework.checker.units.qual.N;
import resources.Settings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@CardFaceType(typeCode = "Treachery", interfaceLanguageKey = InterfaceConstants.TREACHERY)
public class Treachery extends BaseCardFaceModel {
    // TODO: add some help to get rid of the boilerplate for each field that consists of:
    //
    // SETTINGS
    //
    //   - populating from the settings when loading
    //   - updating the settings when saving
    //
    // EDITOR & LAYOUT
    //
    //   - having an appropriate editor field (may be difficult in edge cases so need an out to do it manually)
    //   - binding the editor field to the variable so changes in the UI reflect immediately to the variable which in turns is used when repainting
    //   - populating the editor field from the variable after the editors are created
    //   - placing the editor in the correct layout/tab etc
    //
    //   the declarative/annotation approach to this will 'hide' the editor/control from regular code so it may be difficult to automate the first items and NOT automate the last item
    //   in those cases it may have to be manual for all
    //   for this to be feasible we need to be able to integrate manual editor creation/management/layout for certain fields with automation for others
    //
    // DRAWING
    //
    //   - extracting the value from the variable and drawing it in the correct place on the sheet
    //
    //
    // perhaps a small utility library to create an object per field to manage some/all of the above
    // could further drive creation and setup of the above via annotations to cut down further - perhaps good for simple cases

    // TODO: i18n/l10n for visible text. leverage the existing plugin property files or copy from them


    private WeaknessType weaknessType;

    private CommonCardFieldsModel commonCardFieldsModel;

    private NumberingModel numberingModel;

    public void initialiseModel() {
        weaknessType = WeaknessType.None;

        commonCardFieldsModel = new CommonCardFieldsModel();
        numberingModel = new NumberingModel();
    }

    public WeaknessType getWeaknessType() {
        return weaknessType;
    }

    public void setWeaknessType(WeaknessType weaknessType) {
        this.weaknessType = weaknessType;
    }

    public CommonCardFieldsModel getCommonCardFieldsModel() {
        return commonCardFieldsModel;
    }

    public NumberingModel getNumberingModel() {
        return numberingModel;
    }

    // TODO: consider how to serialise Portraits - do we follow the same as SE and embed the image or make it a much lighter concept
    // TODO: and instead just serialise the image path and pan/scale settings

    // TODO: add annotation support to do bindings - hides the front/back business. can a library/function called from here to allow
    // anything a basic annotation bind won't handle. could do it for portraits too but need to make sure order is predictable

    // TODO: need a wrapper to manage settings prefixes (Front/Back) that is somewhat invisible to this code
    @Override
    public void afterSettingsRead(Settings settings, ObjectInputStream objectInputStream) {
//        title = settings.get("Front.Title");
//        rules = settings.get("Front.Rules");
//        flavourText = settings.get("Front.FlavorText");
//        victory = settings.get("Front.Victory");
//        keywords = settings.get("Front.Keywords");
//        traits = settings.get("Front.Traits");
//        copyright = settings.get("Front.Copyright");
//        artist = settings.get("Front.Artist");
//        weaknessType = Enum.valueOf(WeaknessType.class, settings.get("Front.WeaknessType"));
//
//        try {
//            artPortraitModel = (DefaultPortrait) objectInputStream.readObject();
//            collectionPortrait = (DefaultPortrait) objectInputStream.readObject();
//            encounterPortrait = (DefaultPortrait) objectInputStream.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void beforeSettingsWrite(Settings settings) {
//        settings.set("Front.Title", title);
//        settings.set("Front.Rules", rules);
//        settings.set("Front.FlavorText", flavourText);
//        settings.set("Front.Victory", victory);
//        settings.set("Front.Keywords", keywords);
//        settings.set("Front.Traits", traits);
//        settings.set("Front.Copyright", copyright);
//        settings.set("Front.Artist", artist);
//        settings.set("Front.WeaknessType", weaknessType.name());
    }

    @Override
    public void afterSettingsWrite(ObjectOutputStream objectOutputStream) {
//        try {
//            objectOutputStream.writeObject(artPortraitModel);
//            objectOutputStream.writeObject(collectionPortrait);
//            objectOutputStream.writeObject(encounterPortrait);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
