package com.trendyol.configservice.utils;

import com.trendyol.configservice.model.cassandra.Config;
import com.trendyol.configservice.model.rest.ConfigResponse;

import java.util.LinkedHashSet;
import java.util.Set;

public enum CacheType {
    CACHE_NAME_FOR_CLIENT("configListForClient"),
    CACHE_NAME_FOR_SERVER("configListForServer");

    private String cacheName;

    CacheType(String cacheName) {
        this.cacheName = cacheName;
    }

    public String cacheName() {
        return cacheName;
    }

    public <T> T getCacheValueType() {
        switch (name()) {
            case "CACHE_NAME_FOR_CLIENT":
                Set<ConfigResponse> configResponses = new LinkedHashSet<>();
                return (T) configResponses;
            case "CACHE_NAME_FOR_SERVER":
                Set<Config> configs = new LinkedHashSet<>();
                return (T) configs;
            default:
                return null;
        }
    }
}
