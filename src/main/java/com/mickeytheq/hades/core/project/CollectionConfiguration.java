package com.mickeytheq.hades.core.project;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class CollectionConfiguration {
    @JsonProperty("CollectionInfos")
    private List<CollectionInfo> collectionInfos = new ArrayList<>();
}
