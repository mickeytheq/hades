package com.mickeytheq.hades.core.project.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class ScenarioConfiguration {
    @JsonProperty("UniqueId")
    private String uniqueId = UUID.randomUUID().toString();

    @JsonProperty("DisplayName")
    private String displayName;

    @JsonProperty("Cycle")
    private String cycleUniqueId;

    @JsonProperty("EncounterSets")
    private List<String> encounterSetUniqueIds;

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

    public String getCycleUniqueId() {
        return cycleUniqueId;
    }

    public void setCycleUniqueId(String cycleUniqueId) {
        this.cycleUniqueId = cycleUniqueId;
    }

    public List<String> getEncounterSetUniqueIds() {
        return encounterSetUniqueIds;
    }

    public void setEncounterSetUniqueIds(List<String> encounterSetUniqueIds) {
        this.encounterSetUniqueIds = encounterSetUniqueIds;
    }
}
