package com.mickeytheq.hades.core.model.cardfaces;

import com.mickeytheq.hades.core.model.CardFaceModel;
import com.mickeytheq.hades.core.model.Model;
import com.mickeytheq.hades.core.model.common.PortraitWithArtistModel;
import com.mickeytheq.hades.core.model.entity.Property;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Model(typeCode = "InvestigatorBack")
public class InvestigatorBack implements CardFaceModel {
    public static class InvestigatorBackSection {
        private String header;
        private String text;
        private int afterSpacing = 0;

        @Property("Header")
        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        @Property("Text")
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Property("AfterSpacing")
        public int getAfterSpacing() {
            return afterSpacing;
        }

        public void setAfterSpacing(int afterSpacing) {
            this.afterSpacing = afterSpacing;
        }
    }

    private String story;
    private InvestigatorBackSection section1 = new InvestigatorBackSection();
    private InvestigatorBackSection section2 = new InvestigatorBackSection();
    private InvestigatorBackSection section3 = new InvestigatorBackSection();
    private InvestigatorBackSection section4 = new InvestigatorBackSection();
    private InvestigatorBackSection section5 = new InvestigatorBackSection();
    private InvestigatorBackSection section6 = new InvestigatorBackSection();
    private InvestigatorBackSection section7 = new InvestigatorBackSection();
    private InvestigatorBackSection section8 = new InvestigatorBackSection();
    private final PortraitWithArtistModel portraitWithArtistModel;

    public InvestigatorBack() {
        portraitWithArtistModel = new PortraitWithArtistModel();
    }

    @Property("Section1")
    public InvestigatorBackSection getSection1() {
        return section1;
    }

    public void setSection1(InvestigatorBackSection section1) {
        this.section1 = section1;
    }

    @Property("Section2")
    public InvestigatorBackSection getSection2() {
        return section2;
    }

    public void setSection2(InvestigatorBackSection section2) {
        this.section2 = section2;
    }

    @Property("Section3")
    public InvestigatorBackSection getSection3() {
        return section3;
    }

    public void setSection3(InvestigatorBackSection section3) {
        this.section3 = section3;
    }

    @Property("Section4")
    public InvestigatorBackSection getSection4() {
        return section4;
    }

    public void setSection4(InvestigatorBackSection section4) {
        this.section4 = section4;
    }

    @Property("Section5")
    public InvestigatorBackSection getSection5() {
        return section5;
    }

    public void setSection5(InvestigatorBackSection section5) {
        this.section5 = section5;
    }

    @Property("Section6")
    public InvestigatorBackSection getSection6() {
        return section6;
    }

    public void setSection6(InvestigatorBackSection section6) {
        this.section6 = section6;
    }

    @Property("Section7")
    public InvestigatorBackSection getSection7() {
        return section7;
    }

    public void setSection7(InvestigatorBackSection section7) {
        this.section7 = section7;
    }

    @Property("Section8")
    public InvestigatorBackSection getSection8() {
        return section8;
    }

    public void setSection8(InvestigatorBackSection section8) {
        this.section8 = section8;
    }

    public List<InvestigatorBackSection> getSections() {
        return Stream.of(section1, section2, section3, section4, section5, section6, section7, section8)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Property("Story")
    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    @Property("ArtPortrait")
    public PortraitWithArtistModel getPortraitWithArtistModel() {
        return portraitWithArtistModel;
    }
}
