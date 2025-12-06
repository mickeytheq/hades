package com.mickeytheq.ahlcg4j.core.model.common;

import com.mickeytheq.ahlcg4j.core.model.entity.Property;

public class CommonCardFieldsModel {
    private String title;
    private String subtitle;
    private Boolean unique;
    private String traits;
    private Integer afterTraitsSpace = 0;
    private String keywords;
    private Integer afterKeywordsSpace = 0;
    private String rules;
    private Integer afterRulesSpace = 0;
    private String flavourText;
    private Integer afterFlavourTextSpace = 0;
    private String victory;
    private String copyright;


    @Property("Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Property("Unique")
    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    // return true only if the unique is set to true, false if the value is false or null
    public boolean isUnique() {
        return Boolean.TRUE.equals(unique);
    }

    @Property("Subtitle")
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Property("Traits")
    public String getTraits() {
        return traits;
    }

    public void setTraits(String traits) {
        this.traits = traits;
    }

    @Property("Keywords")
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Property("Rules")
    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    @Property("Flavor")
    public String getFlavourText() {
        return flavourText;
    }

    public void setFlavourText(String flavourText) {
        this.flavourText = flavourText;
    }

    @Property("Victory")
    public String getVictory() {
        return victory;
    }

    public void setVictory(String victory) {
        this.victory = victory;
    }

    @Property("Copyright")
    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @Property("AfterTraitsSpace")
    public Integer getAfterTraitsSpace() {
        return afterTraitsSpace;
    }

    public void setAfterTraitsSpace(Integer afterTraitsSpace) {
        this.afterTraitsSpace = afterTraitsSpace;
    }

    @Property("AfterKeywordsSpace")
    public Integer getAfterKeywordsSpace() {
        return afterKeywordsSpace;
    }

    public void setAfterKeywordsSpace(Integer afterKeywordsSpace) {
        this.afterKeywordsSpace = afterKeywordsSpace;
    }

    @Property("AfterRulesSpace")
    public Integer getAfterRulesSpace() {
        return afterRulesSpace;
    }

    public void setAfterRulesSpace(Integer afterRulesSpace) {
        this.afterRulesSpace = afterRulesSpace;
    }

    @Property("AfterFlavourTextSpace")
    public Integer getAfterFlavourTextSpace() {
        return afterFlavourTextSpace;
    }

    public void setAfterFlavourTextSpace(Integer afterFlavourTextSpace) {
        this.afterFlavourTextSpace = afterFlavourTextSpace;
    }
}
