package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.discriminator.BooleanEmptyWhenFalseDiscriminator;

public class CommonCardFieldsModel {
    private String title;
    private String subtitle;
    private boolean copyOtherFaceTitles = false;
    private boolean unique = false;
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

    @Property("Subtitle")
    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Property(value = CardModelUtils.COPY_OTHER_FACE + "Titles", discriminator = BooleanEmptyWhenFalseDiscriminator.class)
    public boolean getCopyOtherFaceTitles() {
        return copyOtherFaceTitles;
    }

    public void setCopyOtherFaceTitles(boolean copyOtherFaceTitles) {
        this.copyOtherFaceTitles = copyOtherFaceTitles;
    }

    @Property(value = "Unique", discriminator = BooleanEmptyWhenFalseDiscriminator.class)
    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
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
