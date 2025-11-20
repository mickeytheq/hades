package com.mickeytheq.strangeeons.ahlcg4j.cardfaces;

public class CommonCardFieldsModel {
    private String title;
    private String traits;
    private String keywords;
    private String rules;
    private String flavourText;
    private String victory;
    private String copyright;

    private PortraitModel artPortraitModel = new PortraitModel();
    private String artist;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTraits() {
        return traits;
    }

    public void setTraits(String traits) {
        this.traits = traits;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getFlavourText() {
        return flavourText;
    }

    public void setFlavourText(String flavourText) {
        this.flavourText = flavourText;
    }

    public String getVictory() {
        return victory;
    }

    public void setVictory(String victory) {
        this.victory = victory;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public PortraitModel getArtPortraitModel() {
        return artPortraitModel;
    }

    public void setArtPortraitModel(PortraitModel artPortraitModel) {
        this.artPortraitModel = artPortraitModel;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
