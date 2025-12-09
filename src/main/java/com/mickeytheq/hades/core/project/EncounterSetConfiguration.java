package com.mickeytheq.hades.core.project;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EncounterSetConfiguration {
    @JsonProperty("SetInfos")
    private List<EncounterSetInfo> encounterSetInfos = new ArrayList<>();

    public List<EncounterSetInfo> getEncounterSetInfos() {
        return encounterSetInfos;
    }

    public void setEncounterSetInfos(List<EncounterSetInfo> encounterSetInfos) {
        this.encounterSetInfos = encounterSetInfos;
    }

    public Optional<EncounterSetInfo> findEncounterSetInfo(String key) {
        return encounterSetInfos.stream()
                .filter(o -> o.getTag().equals(key))
                .findAny();
    }
}
