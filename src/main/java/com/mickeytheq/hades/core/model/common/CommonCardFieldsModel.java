package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.ZeroNumberDiscriminator;

public class CommonCardFieldsModel {
    private String title;
    private String subtitle;
    private Boolean unique;
    private String traits;
    private Integer afterTraitsSpacing = 0;
    private String keywords;
    private Integer afterKeywordsSpacing = 0;
    private String rules;
    private Integer afterRulesSpacing = 0;
    private String flavourText;
    private Integer afterFlavourTextSpacing = 0;
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

    @Property(value = "AfterTraitsSpacing", discriminator = ZeroNumberDiscriminator.class)
    public Integer getAfterTraitsSpacing() {
        return afterTraitsSpacing;
    }

    public void setAfterTraitsSpacing(Integer afterTraitsSpacing) {
        this.afterTraitsSpacing = afterTraitsSpacing;
    }

    @Property(value = "AfterKeywordsSpacing", discriminator = ZeroNumberDiscriminator.class)
    public Integer getAfterKeywordsSpacing() {
        return afterKeywordsSpacing;
    }

    public void setAfterKeywordsSpacing(Integer afterKeywordsSpacing) {
        this.afterKeywordsSpacing = afterKeywordsSpacing;
    }

    @Property(value = "AfterRulesSpacing", discriminator = ZeroNumberDiscriminator.class)
    public Integer getAfterRulesSpacing() {
        return afterRulesSpacing;
    }

    public void setAfterRulesSpacing(Integer afterRulesSpacing) {
        this.afterRulesSpacing = afterRulesSpacing;
    }

    @Property(value = "AfterFlavourTextSpacing", discriminator = ZeroNumberDiscriminator.class)
    public Integer getAfterFlavourTextSpacing() {
        return afterFlavourTextSpacing;
    }

    public void setAfterFlavourTextSpacing(Integer afterFlavourTextSpacing) {
        this.afterFlavourTextSpacing = afterFlavourTextSpacing;
    }
}
