package com.mickeytheq.hades.core.model.common;

import com.mickeytheq.hades.core.model.entity.Property;
import com.mickeytheq.hades.serialise.EmptyEntityDiscriminator;
import org.apache.commons.lang3.StringUtils;

public class ActAgendaCommonFieldsModel implements EmptyEntityDiscriminator {
    private String header;
    private Integer afterHeaderSpace = 0;
    private String story;
    private Integer afterStorySpace = 0;
    private String rules;

    @Property("Header")
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Property("AfterHeaderSpace")
    public Integer getAfterHeaderSpace() {
        return afterHeaderSpace;
    }

    public void setAfterHeaderSpace(Integer afterHeaderSpace) {
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
    public Integer getAfterStorySpace() {
        return afterStorySpace;
    }

    public void setAfterStorySpace(Integer afterStorySpace) {
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
