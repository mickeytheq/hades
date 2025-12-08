package com.mickeytheq.hades.core.project;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CollectionConfiguration {
    @JsonProperty("CollectionInfos")
    private List<CollectionInfo> collectionInfos = new ArrayList<>();

    public List<CollectionInfo> getCollectionInfos() {
        return collectionInfos;
    }

    public Optional<CollectionInfo> findCollectionInfo(String key) {
        return collectionInfos.stream()
                .filter(o -> o.getKey().equals(key))
                .findAny();
    }
}
