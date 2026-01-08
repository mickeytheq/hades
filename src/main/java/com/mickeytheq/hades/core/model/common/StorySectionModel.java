package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.EmptyEntityDiscriminator;
import org.apache.commons.lang3.StringUtils;

public class StorySectionModel implements EmptyEntityDiscriminator {
    private String header;
    private Distance afterHeaderSpace = new Distance();
    private String story;
    private Distance afterStorySpace = new Distance();
    private String rules;

    @Property("Header")
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Property("AfterHeaderSpace")
    public Distance getAfterHeaderSpace() {
        return afterHeaderSpace;
    }

    public void setAfterHeaderSpace(Distance afterHeaderSpace) {
        this.afterHeaderSpace = afterHeaderSpace;
    }

    @Property("Story")
    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    @Property("AfterStorySpace")
    public Distance getAfterStorySpace() {
        return afterStorySpace;
    }

    public void setAfterStorySpace(Distance afterStorySpace) {
        this.afterStorySpace = afterStorySpace;
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
