package com.mickeytheq.ahlcg4j.core.model.common;

import com.mickeytheq.ahlcg4j.core.model.entity.Property;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerCardFieldsModel {
    private PlayerCardType playerCardType;
    private PlayerCardClass playerCardClass1;
    private PlayerCardClass playerCardClass2;
    private PlayerCardClass playerCardClass3;
    private String cost;
    private Integer level;
    private PlayerCardSkillIcon skillIcon1;
    private PlayerCardSkillIcon skillIcon2;
    private PlayerCardSkillIcon skillIcon3;
    private PlayerCardSkillIcon skillIcon4;
    private PlayerCardSkillIcon skillIcon5;

    public PlayerCardFieldsModel() {
        playerCardType = PlayerCardType.Standard;
        playerCardClass1 = PlayerCardClass.Guardian;
        cost = "0";
        level = 0;
    }

    @Property("PlayerCardType")
    public PlayerCardType getPlayerCardType() {
        return playerCardType;
    }

    public void setPlayerCardType(PlayerCardType playerCardType) {
        this.playerCardType = playerCardType;
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

    @Property("PlayerCardClass1")
    public PlayerCardClass getPlayerCardClass1() {
        return playerCardClass1;
    }

    public void setPlayerCardClass1(PlayerCardClass playerCardClass1) {
        this.playerCardClass1 = playerCardClass1;
    }

    @Property("PlayerCardClass2")
    public PlayerCardClass getPlayerCardClass2() {
        return playerCardClass2;
    }

    public void setPlayerCardClass2(PlayerCardClass playerCardClass2) {
        this.playerCardClass2 = playerCardClass2;
    }

    @Property("PlayerCardClass3")
    public PlayerCardClass getPlayerCardClass3() {
        return playerCardClass3;
    }

    public void setPlayerCardClass3(PlayerCardClass playerCardClass3) {
        this.playerCardClass3 = playerCardClass3;
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

    public List<PlayerCardClass> getPlayerCardClasses() {
        List<PlayerCardClass> cardClasses = Stream.of(
                        getPlayerCardClass1(),
                        getPlayerCardClass2(),
                        getPlayerCardClass3())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        return cardClasses;
    }

    public List<PlayerCardSkillIcon> getSkillIcons() {
        List<PlayerCardSkillIcon> skillIcons = Stream.of(
                        getSkillIcon1(),
                        getSkillIcon2(),
                        getSkillIcon3(),
                        getSkillIcon4(),
                        getSkillIcon5()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return skillIcons;
    }
}
