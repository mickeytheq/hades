package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.core.project.ProjectContext;
import com.mickeytheq.hades.core.view.CardFaceSide;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerCardFieldsModel {
    private PlayerCardType cardType;
    private PlayerCardClass cardClass1;
    private PlayerCardClass cardClass2;
    private PlayerCardClass cardClass3;
    private String cost;
    private Integer level;
    private PlayerCardSkillIcon skillIcon1;
    private PlayerCardSkillIcon skillIcon2;
    private PlayerCardSkillIcon skillIcon3;
    private PlayerCardSkillIcon skillIcon4;
    private PlayerCardSkillIcon skillIcon5;
    private PlayerCardSkillIcon skillIcon6;

    public void initialiseNew(ProjectContext projectContext, CardFaceSide cardFaceSide) {
        cardType = PlayerCardType.Standard;
        cardClass1 = PlayerCardClass.Guardian;
        level = 0;
        cost = "0";
    }

    @Property("CardType")
    public PlayerCardType getCardType() {
        return cardType;
    }

    public void setCardType(PlayerCardType cardType) {
        this.cardType = cardType;
    }

    @Property("Cost")
    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Property("Level")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Property("CardClass1")
    public PlayerCardClass getCardClass1() {
        return cardClass1;
    }

    public void setCardClass1(PlayerCardClass cardClass1) {
        this.cardClass1 = cardClass1;
    }

    @Property("CardClass2")
    public PlayerCardClass getCardClass2() {
        return cardClass2;
    }

    public void setCardClass2(PlayerCardClass cardClass2) {
        this.cardClass2 = cardClass2;
    }

    @Property("CardClass3")
    public PlayerCardClass getCardClass3() {
        return cardClass3;
    }

    public void setCardClass3(PlayerCardClass cardClass3) {
        this.cardClass3 = cardClass3;
    }

    @Property("SkillIcon1")
    public PlayerCardSkillIcon getSkillIcon1() {
        return skillIcon1;
    }

    public void setSkillIcon1(PlayerCardSkillIcon skillIcon1) {
        this.skillIcon1 = skillIcon1;
    }

    @Property("SkillIcon2")
    public PlayerCardSkillIcon getSkillIcon2() {
        return skillIcon2;
    }

    public void setSkillIcon2(PlayerCardSkillIcon skillIcon2) {
        this.skillIcon2 = skillIcon2;
    }

    @Property("SkillIcon3")
    public PlayerCardSkillIcon getSkillIcon3() {
        return skillIcon3;
    }

    public void setSkillIcon3(PlayerCardSkillIcon skillIcon3) {
        this.skillIcon3 = skillIcon3;
    }

    @Property("SkillIcon4")
    public PlayerCardSkillIcon getSkillIcon4() {
        return skillIcon4;
    }

    public void setSkillIcon4(PlayerCardSkillIcon skillIcon4) {
        this.skillIcon4 = skillIcon4;
    }

    @Property("SkillIcon5")
    public PlayerCardSkillIcon getSkillIcon5() {
        return skillIcon5;
    }

    public void setSkillIcon5(PlayerCardSkillIcon skillIcon5) {
        this.skillIcon5 = skillIcon5;
    }

    @Property("SkillIcon6")
    public PlayerCardSkillIcon getSkillIcon6() {
        return skillIcon6;
    }

    public void setSkillIcon6(PlayerCardSkillIcon skillIcon6) {
        this.skillIcon6 = skillIcon6;
    }

    // for convenience accessing as a list
    public List<PlayerCardClass> getPlayerCardClasses() {
        List<PlayerCardClass> cardClasses = Stream.of(
                        getCardClass1(),
                        getCardClass2(),
                        getCardClass3())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        return cardClasses;
    }

    public boolean isMultiClass() {
        if (cardType != PlayerCardType.Standard)
            return false;

        return getPlayerCardClasses().size() > 1;
    }

    // returns true if this card has a level and the level is valid in this context
    public boolean hasLevel() {
        // story and weakness cards never have a level
        if (cardType == PlayerCardType.Story || cardType.isWeakness())
            return false;

        return level != null;
    }

    // primarily for testing
    public void setPlayerCardClasses(List<PlayerCardClass> playerCardClasses) {
        if (playerCardClasses.size() > 3)
            throw new RuntimeException("Max of 3 player card classes");
        
        if (playerCardClasses.isEmpty())
            return;
        
        setCardClass1(playerCardClasses.get(0));

        if (playerCardClasses.size() < 2)
            return;

        setCardClass2(playerCardClasses.get(1));

        if (playerCardClasses.size() < 3)
            return;

        setCardClass3(playerCardClasses.get(2));
    }

    // for convenience accessing as a list
    public List<PlayerCardSkillIcon> getSkillIcons() {
        List<PlayerCardSkillIcon> skillIcons = Stream.of(
                        getSkillIcon1(),
                        getSkillIcon2(),
                        getSkillIcon3(),
                        getSkillIcon4(),
                        getSkillIcon5(),
                        getSkillIcon6()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return skillIcons;
    }

    // primarily for testing
    public void setSkillIcons(List<PlayerCardSkillIcon> skillIcons) {
        if (skillIcons.size() > 6)
            throw new RuntimeException("Max of 6 skill icons");

        if (skillIcons.isEmpty())
            return;

        setSkillIcon1(skillIcons.get(0));

        if (skillIcons.size() < 2)
            return;

        setSkillIcon2(skillIcons.get(1));

        if (skillIcons.size() < 3)
            return;

        setSkillIcon3(skillIcons.get(2));

        if (skillIcons.size() < 4)
            return;

        setSkillIcon3(skillIcons.get(3));

        if (skillIcons.size() < 5)
            return;

        setSkillIcon3(skillIcons.get(4));

        if (skillIcons.size() < 6)
            return;

        setSkillIcon3(skillIcons.get(5));
    }
}
