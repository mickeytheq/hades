package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.BooleanFalseDiscriminator;

public class CommonCardFieldsModel {
    private String title;
    private String subtitle;
    private Boolean unique;
    private String traits;
    private Distance afterTraitsSpacing = Distance.createZeroPoint();
    private String keywords;
    private Distance afterKeywordsSpacing = Distance.createZeroPoint();
    private String rules;
    private Distance afterRulesSpacing = Distance.createZeroPoint();
    private String flavourText;
    private Distance afterFlavourTextSpacing = Distance.createZeroPoint();
    private String victory;
    private String copyright;


    @Property("Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Property(value = "Unique", discriminator = BooleanFalseDiscriminator.class)
    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    // return true only if the unique is set to true, false if the value is false or null
    public boolean isUniqueSafe() {
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

    @Property(value = "AfterTraitsSpacing")
    public Distance getAfterTraitsSpacing() {
        return afterTraitsSpacing;
    }

    public void setAfterTraitsSpacing(Distance afterTraitsSpacing) {
        this.afterTraitsSpacing = afterTraitsSpacing;
    }

    @Property(value = "AfterKeywordsSpacing")
    public Distance getAfterKeywordsSpacing() {
        return afterKeywordsSpacing;
    }

    public void setAfterKeywordsSpacing(Distance afterKeywordsSpacing) {
        this.afterKeywordsSpacing = afterKeywordsSpacing;
    }

    @Property(value = "AfterRulesSpacing")
    public Distance getAfterRulesSpacing() {
        return afterRulesSpacing;
    }

    public void setAfterRulesSpacing(Distance afterRulesSpacing) {
        this.afterRulesSpacing = afterRulesSpacing;
    }

    @Property(value = "AfterFlavourTextSpacing")
    public Distance getAfterFlavourTextSpacing() {
        return afterFlavourTextSpacing;
    }

    public void setAfterFlavourTextSpacing(Distance afterFlavourTextSpacing) {
        this.afterFlavourTextSpacing = afterFlavourTextSpacing;
    }
}
