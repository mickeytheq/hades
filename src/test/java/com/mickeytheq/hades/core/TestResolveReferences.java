package com.mickeytheq.hades.core;

import com.google.common.collect.Lists;
import com.mickeytheq.hades.core.global.carddatabase.BasicCardDatabase;
import com.mickeytheq.hades.core.global.carddatabase.CardDatabases;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.cardfaces.*;
import com.mickeytheq.hades.core.model.common.LocationFieldsModel;
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.core.model.image.NothingImagePersister;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.project.configuration.CollectionConfiguration;
import com.mickeytheq.hades.core.project.configuration.EncounterSetConfiguration;
import com.mickeytheq.hades.core.project.configuration.ProjectConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestResolveReferences {
    @BeforeMethod
    public void setup() {
        ProjectContexts.setCurrentContext(new StandardProjectContext(new ProjectConfiguration(), new NothingImagePersister()));
    }

    @AfterMethod
    public void tearDown() {
        ProjectContexts.unsetCurrentContext();
    }

    @Test
    public void testLocationCopyAll() {
        Location location = new Location();
        location.getLocationFieldsModel().setLocationIcon("ABC");
        location.getLocationFieldsModel().setConnectionIcon1("DEF");

        LocationBack locationBack = new LocationBack();
        locationBack.getLocationFieldsModel().setCopyOtherFace(true);

        Card card = Cards.createCardModel(location, locationBack);

        Cards.resolveReferences(card);

        Assert.assertEquals(locationBack.getLocationFieldsModel().getLocationIcon(), "ABC");
        Assert.assertEquals(locationBack.getLocationFieldsModel().getConnectionIcon1(), "DEF");
    }

    @Test
    public void testLocationCopySpecific() {
        Location location = new Location();
        location.getLocationFieldsModel().setLocationIcon("ABC");
        location.getLocationFieldsModel().setConnectionIcon1("DEF");

        LocationBack locationBack = new LocationBack();
        locationBack.getLocationFieldsModel().setLocationIcon(LocationFieldsModel.COPY_OTHER_VALUE);
        locationBack.getLocationFieldsModel().setConnectionIcon1(LocationFieldsModel.COPY_OTHER_VALUE);
        locationBack.getLocationFieldsModel().setConnectionIcon2(LocationFieldsModel.COPY_OTHER_VALUE);
        locationBack.getLocationFieldsModel().setConnectionIcon3("XYZ");

        Card card = Cards.createCardModel(location, locationBack);

        Cards.resolveReferences(card);

        Assert.assertEquals(locationBack.getLocationFieldsModel().getLocationIcon(), "ABC");
        Assert.assertEquals(locationBack.getLocationFieldsModel().getConnectionIcon1(), "DEF");
        Assert.assertNull(locationBack.getLocationFieldsModel().getConnectionIcon2());
        Assert.assertEquals(locationBack.getLocationFieldsModel().getConnectionIcon3(), "XYZ");
    }

    @Test
    public void testAct() {
        Act act = new Act();
        act.getActFieldsModel().setNumber("1");
        act.getActFieldsModel().setDeckId("a");

        ActBack actBack = new ActBack();
        actBack.getActFieldsModel().setCopyOtherFace(true);

        Card card = Cards.createCardModel(act, actBack);

        Cards.resolveReferences(card);

        Assert.assertEquals(actBack.getActFieldsModel().getNumber(), "1");
        Assert.assertEquals(actBack.getActFieldsModel().getDeckId(), "b");
    }

    @Test
    public void testAgenda() {
        Agenda agenda = new Agenda();
        agenda.getAgendaFieldsModel().setNumber("1");
        agenda.getAgendaFieldsModel().setDeckId("a");

        AgendaBack agendaBack = new AgendaBack();
        agendaBack.getAgendaFieldsModel().setCopyOtherFace(true);

        Card card = Cards.createCardModel(agenda, agendaBack);

        Cards.resolveReferences(card);

        Assert.assertEquals(agendaBack.getAgendaFieldsModel().getNumber(), "1");
        Assert.assertEquals(agendaBack.getAgendaFieldsModel().getDeckId(), "b");
    }

    @Test
    public void testTitles() {
        Location location = new Location();
        location.getCommonCardFieldsModel().setTitle("Location");
        location.getCommonCardFieldsModel().setSubtitle("Sub");

        LocationBack locationBack = new LocationBack();
        locationBack.getCommonCardFieldsModel().setCopyOtherFaceTitles(true);

        Card card = Cards.createCardModel(location, locationBack);

        Cards.resolveReferences(card);

        Assert.assertEquals(locationBack.getCommonCardFieldsModel().getTitle(), "Location");
        Assert.assertEquals(locationBack.getCommonCardFieldsModel().getSubtitle(), "Sub");
    }

    @Test
    public void testEncounterSet() {
        EncounterSetConfiguration encounterSetConfiguration = new EncounterSetConfiguration();

        Act act = new Act();
        act.getEncounterSetModel().setEncounterSetConfiguration(encounterSetConfiguration);
        act.getEncounterSetModel().setNumber("2");
        act.getEncounterSetModel().setTotal("3");

        ActBack actBack = new ActBack();
        actBack.getEncounterSetModel().setCopyOtherFace(true);

        Card card = Cards.createCardModel(act, actBack);

        Cards.resolveReferences(card);

        Assert.assertEquals(actBack.getEncounterSetModel().getEncounterSetConfiguration(), encounterSetConfiguration);
        Assert.assertEquals(actBack.getEncounterSetModel().getNumber(), "2");
        Assert.assertEquals(actBack.getEncounterSetModel().getTotal(), "3");
    }

    @Test
    public void testCollection() {
        CollectionConfiguration collectionConfiguration = new CollectionConfiguration();

        Asset asset = new Asset();
        asset.getCollectionModel().setCollectionConfiguration(collectionConfiguration);
        asset.getCollectionModel().setNumber("100");

        Event event = new Event();
        event.getCollectionModel().setCopyOtherFace(true);

        Card card = Cards.createCardModel(asset, event);

        Cards.resolveReferences(card);

        Assert.assertEquals(event.getCollectionModel().getCollectionConfiguration(), collectionConfiguration);
        Assert.assertEquals(event.getCollectionModel().getNumber(), "100");
    }

    @Test
    public void testPortrait() {
        ImageProxy imageProxy = ImageProxy.createEmptyReadOnly();

        Asset asset = new Asset();
        asset.getPortraitModel().setImage(imageProxy);
        asset.getPortraitModel().setPanX(2.0);
        asset.getPortraitModel().setPanY(12.0);
        asset.getPortraitModel().setScale(1.5);
        asset.getPortraitModel().setRotation(90.0);

        Event event = new Event();
        event.getPortraitModel().setCopyOtherFace(true);

        Card card = Cards.createCardModel(asset, event);

        Cards.resolveReferences(card);

        Assert.assertEquals(event.getPortraitModel().getImage(), imageProxy);
        Assert.assertEquals(event.getPortraitModel().getPanX(), asset.getPortraitModel().getPanX());
        Assert.assertEquals(event.getPortraitModel().getPanY(), asset.getPortraitModel().getPanY());
        Assert.assertEquals(event.getPortraitModel().getScale(), asset.getPortraitModel().getScale());
        Assert.assertEquals(event.getPortraitModel().getRotation(), asset.getPortraitModel().getRotation());
    }

    @Test
    public void testShadow() {
        LocationBack sharedLocationBack = new LocationBack();
        sharedLocationBack.getCommonCardFieldsModel().setTitle("Dark Woods");

        Card referencedCard = Cards.createCardModel(sharedLocationBack, null);

        CardDatabases.set(new BasicCardDatabase(Lists.newArrayList(referencedCard)));

        Location location = new Location();
        location.getCommonCardFieldsModel().setTitle("Location");
        location.getCommonCardFieldsModel().setSubtitle("Sub");

        Shadow shadow = new Shadow();
        shadow.setShadowCardId(referencedCard.getId());

        Card card = Cards.createCardModel(location, shadow);

        Cards.resolveReferences(card);

        Assert.assertTrue(card.getBackFaceModel() instanceof LocationBack);

        LocationBack dereferencedShadow = (LocationBack)card.getBackFaceModel();

        Assert.assertEquals(dereferencedShadow.getCommonCardFieldsModel().getTitle(), sharedLocationBack.getCommonCardFieldsModel().getTitle());
    }
}
