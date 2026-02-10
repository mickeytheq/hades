package com.mickeytheq.hades.core.project.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class CycleConfiguration {
    @JsonProperty("UniqueId")
    private String uniqueId = UUID.randomUUID().toString();

    @JsonProperty("DisplayName")
    private String displayName;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
