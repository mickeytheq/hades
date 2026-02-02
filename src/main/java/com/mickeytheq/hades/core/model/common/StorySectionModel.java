package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.discriminator.EmptyEntityDiscriminator;
import org.apache.commons.lang3.StringUtils;

public class StorySectionModel implements EmptyEntityDiscriminator {
    private String header;
    private Distance afterHeaderSpacing = Distance.createZeroPoint();
    private String story;
    private Distance afterStorySpacing = Distance.createZeroPoint();
    private String rules;

    @Property("Header")
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Property("AfterHeaderSpacing")
    public Distance getAfterHeaderSpacing() {
        return afterHeaderSpacing;
    }

    public void setAfterHeaderSpacing(Distance afterHeaderSpacing) {
        this.afterHeaderSpacing = afterHeaderSpacing;
    }

    @Property("Story")
    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    @Property("AfterStorySpacing")
    public Distance getAfterStorySpacing() {
        return afterStorySpacing;
    }

    public void setAfterStorySpacing(Distance afterStorySpacing) {
        this.afterStorySpacing = afterStorySpacing;
    }

    @Property("Rules")
    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    @Override
    public boolean isEmpty() {
        if (!StringUtils.isEmpty(header))
            return false;

        if (!StringUtils.isEmpty(story))
            return false;

        if (!StringUtils.isEmpty(rules))
            return false;

        return true;
    }
}
