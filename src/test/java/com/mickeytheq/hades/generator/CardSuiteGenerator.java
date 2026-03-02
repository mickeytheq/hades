package com.mickeytheq.hades.generator;

import ca.cgjennings.graphics.ImageUtilities;
import com.mickeytheq.hades.core.Cards;
import com.mickeytheq.hades.core.model.Card;
import com.mickeytheq.hades.core.model.cardfaces.*;
import com.mickeytheq.hades.core.model.common.*;
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.core.model.image.NothingImagePersister;
import com.mickeytheq.hades.core.model.image.SingleDirectoryUuidEncodedFilenamesImagePersister;
import com.mickeytheq.hades.core.project.HadesProject;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.project.ProjectContexts;
import com.mickeytheq.hades.core.project.StandardProjectContext;
import com.mickeytheq.hades.core.project.configuration.*;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.core.view.utils.ImageUtils;
import com.mickeytheq.hades.serialise.CardIO;
import com.mickeytheq.hades.strangeeons.util.ProjectUtils;
import com.mickeytheq.hades.util.shape.Unit;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;

// generate a fixed set of cards
public class CardSuiteGenerator {
    public static final BufferedImage GEAR_IMAGE = ImageUtils.loadImageReadOnly("/icons/application/gear.png");

    private final ProjectContext projectContext;
    private CollectionConfiguration gearCollectionConfiguration;
    private EncounterSetConfiguration gearEncounterSetConfiguration;

    public static void main(String[] args) {
        generateProject(Paths.get("D:\\temp\\trash\\hades_generated_project"));
    }

    public static void generateProject(Path rootDirectory) {
        CardSuiteGenerator cardSuiteGenerator = new CardSuiteGenerator(rootDirectory);

        CardIO.writeCard(rootDirectory.resolve("asset.hades"), cardSuiteGenerator.asset(), cardSuiteGenerator.projectContext);
        CardIO.writeCard(rootDirectory.resolve("event.hades"), cardSuiteGenerator.event(), cardSuiteGenerator.projectContext);
        CardIO.writeCard(rootDirectory.resolve("skill.hades"), cardSuiteGenerator.skill(), cardSuiteGenerator.projectContext);
        CardIO.writeCard(rootDirectory.resolve("enemy.hades"), cardSuiteGenerator.enemy(), cardSuiteGenerator.projectContext);
        CardIO.writeCard(rootDirectory.resolve("treachery.hades"), cardSuiteGenerator.treachery(), cardSuiteGenerator.projectContext);
        CardIO.writeCard(rootDirectory.resolve("scenario-reference.hades"), cardSuiteGenerator.scenarioReference(), cardSuiteGenerator.projectContext);
        CardIO.writeCard(rootDirectory.resolve("agenda1.hades"), cardSuiteGenerator.agenda1(), cardSuiteGenerator.projectContext);
        CardIO.writeCard(rootDirectory.resolve("act1.hades"), cardSuiteGenerator.act1(), cardSuiteGenerator.projectContext);
    }

    public CardSuiteGenerator(Path rootDirectory) {
        ProjectUtils.createProjectSkeleton(rootDirectory);

        ProjectConfiguration projectConfiguration = new ProjectConfiguration();

        HadesProject hadesProject = HadesProject.getFromPath(rootDirectory);

        ProjectContexts.withContext(new StandardProjectContext(null, new SingleDirectoryUuidEncodedFilenamesImagePersister(hadesProject.getImagesDirectory())), () -> {
            gearCollectionConfiguration = new CollectionConfiguration();
            gearCollectionConfiguration.setImage(ImageProxy.createPrimed(GEAR_IMAGE));
            gearCollectionConfiguration.setTag("gear");
            gearCollectionConfiguration.setDisplayName("Gear");
            projectConfiguration.getCollectionConfigurations().add(gearCollectionConfiguration);

            gearEncounterSetConfiguration = new EncounterSetConfiguration();
            gearEncounterSetConfiguration.setImage(ImageProxy.createPrimed(GEAR_IMAGE));
            gearEncounterSetConfiguration.setTag("gear");
            gearEncounterSetConfiguration.setDisplayName("Gear");
            projectConfiguration.getEncounterSetConfigurations().add(gearEncounterSetConfiguration);
        });

        ProjectConfigurationProvider provider = new ProjectConfigurationProviderJson(hadesProject.getProjectConfigurationFile());
        provider.save(projectConfiguration);

        this.projectContext = new StandardProjectContext(projectConfiguration, new NothingImagePersister());
    }

    public Card asset() {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            Asset asset = new Asset();
            asset.initialiseNew(projectContext, CardFaceSide.Front);

            asset.getCommonCardFieldsModel().setTitle("Kitchen Knife");
            asset.getCommonCardFieldsModel().setSubtitle("Unexceptional");
            asset.getCommonCardFieldsModel().setTraits("Item. Weapon. Melee.");
            asset.getCommonCardFieldsModel().setKeywords("Exceptional.");
            asset.getCommonCardFieldsModel().setRules("<act>: <b>Fight</b>. You get +1 <com> and deal +1 damage for this attack.");
            asset.getCommonCardFieldsModel().setFlavourText("It's just a knife.");

            asset.getCommonCardFieldsModel().setCopyright("MickeyTheQ");

            asset.getAssetFieldsModel().setSlot1(Asset.AssetSlot.Hand);

            asset.getPlayerCardFieldsModel().setCardType(PlayerCardType.Standard);
            asset.getPlayerCardFieldsModel().setCardClass1(PlayerCardClass.Guardian);
            asset.getPlayerCardFieldsModel().setLevel(0);
            asset.getPlayerCardFieldsModel().setCost("1");
            asset.getPlayerCardFieldsModel().setSkillIcon1(PlayerCardSkillIcon.Combat);
            asset.getPlayerCardFieldsModel().setSkillIcon2(PlayerCardSkillIcon.Agility);

            asset.getCollectionModel().setCollectionConfiguration(gearCollectionConfiguration);
            asset.getCollectionModel().setNumber("1");

            return Cards.createCardModel(asset, new PlayerCardBack());
        });
    }

    public Card event() {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            Event event = new Event();
            event.initialiseNew(projectContext, CardFaceSide.Front);

            event.getCommonCardFieldsModel().setTitle("Cake or Death");
            event.getCommonCardFieldsModel().setTraits("Gambit.");
            event.getCommonCardFieldsModel().setKeywords("Fast.");
            event.getCommonCardFieldsModel().setRules("Play when you would be defeated by damage and/or horror. Draw a random chaos token. If is a symbol set your remaining health and sanity to 1. Otherwise suffer 1 trauma of your choice.");
            event.getCommonCardFieldsModel().setFlavourText("Don't run out of cake.");

            event.getCommonCardFieldsModel().setCopyright("MickeyTheQ");

            event.getPlayerCardFieldsModel().setCardType(PlayerCardType.Standard);
            event.getPlayerCardFieldsModel().setCardClass1(PlayerCardClass.Survivor);
            event.getPlayerCardFieldsModel().setLevel(4);
            event.getPlayerCardFieldsModel().setCost("3");
            event.getPlayerCardFieldsModel().setSkillIcon1(PlayerCardSkillIcon.Willpower);
            event.getPlayerCardFieldsModel().setSkillIcon2(PlayerCardSkillIcon.Willpower);

            event.getCollectionModel().setCollectionConfiguration(gearCollectionConfiguration);
            event.getCollectionModel().setNumber("2");

            return Cards.createCardModel(event, new PlayerCardBack());
        });
    }

    public Card skill() {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            Skill skill = new Skill();
            skill.initialiseNew(projectContext, CardFaceSide.Front);

            skill.getCommonCardFieldsModel().setTitle("Caution to the wind");
            skill.getCommonCardFieldsModel().setTraits("Gambit.");
            skill.getCommonCardFieldsModel().setRules("Play when you would fail a skill test. Reveal a chaos token. If you draw a <sku> or <ten> suffer 1 physical trauma. Otherwise you automatically succeed the skill test.");
            skill.getCommonCardFieldsModel().setFlavourText("Don't run out of cake.");

            skill.getCommonCardFieldsModel().setCopyright("MickeyTheQ");

            skill.getPlayerCardFieldsModel().setCardType(PlayerCardType.Standard);
            skill.getPlayerCardFieldsModel().setCardClass1(PlayerCardClass.Rogue);
            skill.getPlayerCardFieldsModel().setCardClass1(PlayerCardClass.Mystic);
            skill.getPlayerCardFieldsModel().setLevel(0);
            skill.getPlayerCardFieldsModel().setCost("4");
            skill.getPlayerCardFieldsModel().setSkillIcon1(PlayerCardSkillIcon.Agility);
            skill.getPlayerCardFieldsModel().setSkillIcon2(PlayerCardSkillIcon.Wild);

            skill.getCollectionModel().setCollectionConfiguration(gearCollectionConfiguration);
            skill.getCollectionModel().setNumber("3");

            return Cards.createCardModel(skill, new PlayerCardBack());
        });
    }

    public Card enemy() {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            Enemy enemy = new Enemy();
            enemy.initialiseNew(projectContext, CardFaceSide.Front);

            enemy.getCommonCardFieldsModel().setTitle("Dubious Salesman");
            enemy.getCommonCardFieldsModel().setSubtitle("Sixteen times the detail");
            enemy.getCommonCardFieldsModel().setTraits("Yithian.");
            enemy.getCommonCardFieldsModel().setKeywords("Aloof. Hunter.");
            enemy.getCommonCardFieldsModel().setRules("<for> After you succeed at a skill test by 2 or more at <title>'s location, it attacks you.");
            enemy.getCommonCardFieldsModel().setFlavourText("All of this. Just. Works.");

            enemy.getCommonCardFieldsModel().setCopyright("MickeyTheQ");

            enemy.getEnemyFieldsModel().setCombat(new Statistic("1", false));
            enemy.getEnemyFieldsModel().setEvade(new Statistic("5", false));
            enemy.getEnemyFieldsModel().setHealth(new Statistic("2", false));
            enemy.getEnemyFieldsModel().setDamage(1);
            enemy.getEnemyFieldsModel().setHorror(1);

            enemy.getCollectionModel().setCollectionConfiguration(gearCollectionConfiguration);
            enemy.getCollectionModel().setNumber("4");

            enemy.getEncounterSetModel().setEncounterSetConfiguration(gearEncounterSetConfiguration);
            enemy.getEncounterSetModel().setNumber("1-3");
            enemy.getEncounterSetModel().setTotal("6");

            return Cards.createCardModel(enemy, new EncounterCardBack());
        });
    }

    public Card treachery() {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            Treachery treachery = new Treachery();
            treachery.initialiseNew(projectContext, CardFaceSide.Front);

            treachery.getCommonCardFieldsModel().setTitle("Rat Swarm");
            treachery.getCommonCardFieldsModel().setTraits("Terror.");
            treachery.getCommonCardFieldsModel().setRules("<rev> Put <title> into play in your threat area.\n<vs><for> When you would take any amount of damage: Take 1 additional damage.");
            treachery.getCommonCardFieldsModel().setAfterRulesSpacing(new Distance(20, Unit.Point));
            treachery.getCommonCardFieldsModel().setFlavourText("We've had one rat yes. But what about 723rd rat?");

            treachery.getCommonCardFieldsModel().setCopyright("MickeyTheQ");

            treachery.getCollectionModel().setCollectionConfiguration(gearCollectionConfiguration);
            treachery.getCollectionModel().setNumber("5");

            treachery.getEncounterSetModel().setEncounterSetConfiguration(gearEncounterSetConfiguration);
            treachery.getEncounterSetModel().setNumber("4-6");
            treachery.getEncounterSetModel().setTotal("6");

            return Cards.createCardModel(treachery, new EncounterCardBack());
        });
    }

    public Card scenarioReference() {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            ScenarioReference front = new ScenarioReference();

            front.getCommonCardFieldsModel().setTitle("Hades Rising");

            front.getCommonCardFieldsModel().setCopyright("MickeyTheQ");

            front.getScenarioReferenceFieldsModel().setDifficulty(ScenarioReference.Difficulty.EasyStandard);
            front.getScenarioReferenceFieldsModel().getSkull().setRules("-X. X is the amount of cake at your location.");
            front.getScenarioReferenceFieldsModel().getCultist().setRules("-1. If you fail, eat 1 cake.");
            front.getScenarioReferenceFieldsModel().getTablet().setRules("-2. If you fail, lose all your resources.");
            front.getScenarioReferenceFieldsModel().getElderThing().setRules("-3. If you fail, take 1 damage and 1 horror.");

            front.getCollectionModel().setCollectionConfiguration(gearCollectionConfiguration);
            front.getCollectionModel().setNumber("6");

            front.getEncounterSetModel().setEncounterSetConfiguration(gearEncounterSetConfiguration);
            front.getEncounterSetModel().setNumber("1");
            front.getEncounterSetModel().setTotal("20");


            ScenarioReference back = new ScenarioReference();
            back.initialiseNew(projectContext, CardFaceSide.Back);

            back.getCommonCardFieldsModel().setTitle("Hades Rising");

            back.getCommonCardFieldsModel().setCopyright("MickeyTheQ");

            back.getScenarioReferenceFieldsModel().getSkull().setRules("-X. X is the amount of cake in play.");
            back.getScenarioReferenceFieldsModel().getCultist().setRules("-2. Eat 1 cake.");
            back.getScenarioReferenceFieldsModel().getTablet().setRules("-3. Lose all your resources.");
            back.getScenarioReferenceFieldsModel().getElderThing().setRules("-4. Take 1 damage and 1 horror.");

            return Cards.createCardModel(front, back);
        });
    }

    public Card agenda1() {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            Agenda front = new Agenda();

            front.getCommonCardFieldsModel().setTitle("Hades Falling");

            front.getStorySectionModel().setStory("Many words. Many many words.");
            front.getStorySectionModel().setRules("<for> When any amount of cake leaves play: Place 1 doom on this agenda.");

            front.getAgendaFieldsModel().setNumber("1");
            front.getAgendaFieldsModel().setDeckId("a");
            front.getAgendaFieldsModel().setDoom(new Statistic("8", false));

            front.getCommonCardFieldsModel().setCopyright("MickeyTheQ");

            front.getCollectionModel().setCollectionConfiguration(gearCollectionConfiguration);
            front.getCollectionModel().setNumber("7");

            front.getEncounterSetModel().setEncounterSetConfiguration(gearEncounterSetConfiguration);
            front.getEncounterSetModel().setNumber("2");
            front.getEncounterSetModel().setTotal("20");

            AgendaBack back = new AgendaBack();
            back.initialiseNew(projectContext, CardFaceSide.Back);

            back.getCommonCardFieldsModel().setTitle("Choices to make");

            back.getCommonCardFieldsModel().setCopyright("MickeyTheQ");

            back.getSection1().setHeader("Header 1");
            back.getSection1().setStory("Some story");
            back.getSection1().setRules("Some rules");

            back.getSection2().setHeader("Header 2");
            back.getSection2().setStory("Some story");
            back.getSection2().setRules("Some rules");

            back.getSection3().setHeader("Header 3");
            back.getSection3().setStory("Some story");
            back.getSection3().setRules("Some rules");

            return Cards.createCardModel(front, back);
        });
    }

    public Card act1() {
        return ProjectContexts.withContextReturn(projectContext, () -> {
            Act front = new Act();

            front.getCommonCardFieldsModel().setTitle("Hades Falling");

            front.getStorySectionModel().setStory("Many words. Many many words.");
            front.getStorySectionModel().setRules("<for> When any amount of cake leaves play: Place 1 doom on this agenda.");

            front.getActFieldsModel().setNumber("1");
            front.getActFieldsModel().setDeckId("a");
            front.getActFieldsModel().setClues(new Statistic("3", true));

            front.getCommonCardFieldsModel().setCopyright("MickeyTheQ");

            front.getCollectionModel().setCollectionConfiguration(gearCollectionConfiguration);
            front.getCollectionModel().setNumber("8");

            front.getEncounterSetModel().setEncounterSetConfiguration(gearEncounterSetConfiguration);
            front.getEncounterSetModel().setNumber("3");
            front.getEncounterSetModel().setTotal("20");

            ActBack back = new ActBack();
            back.initialiseNew(projectContext, CardFaceSide.Back);

            back.getCommonCardFieldsModel().setTitle("Choices to make");

            back.getCommonCardFieldsModel().setCopyright("MickeyTheQ");

            back.getSection1().setStory("Some story");
            back.getSection1().setRules("Some rules");

            return Cards.createCardModel(front, back);
        });
    }
}
