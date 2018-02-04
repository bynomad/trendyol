package com.trendyol.configservice.service;

import com.trendyol.configservice.converter.ConfigConverter;
import com.trendyol.configservice.model.cassandra.Config;
import com.trendyol.configservice.model.rest.ConfigResponse;
import com.trendyol.configservice.repository.cassandra.ConfigRepository;
import com.trendyol.configservice.utils.CacheType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class MyCacheService {
    private final CacheManager cacheManager;
    private final ConfigRepository configRepository;


    @Autowired
    public MyCacheService(CacheManager cacheManager, ConfigRepository configRepository) {
        this.cacheManager = cacheManager;
        this.configRepository = configRepository;
    }

    @PostConstruct
    public void init() {
        Set<String> applicationNames = new HashSet<>();
        for (Config config : configRepository.findAllApplicationNames()) {
            applicationNames.add(config.getApplicationName());
        }

        createCacheNamesAndKeys(applicationNames);
        putCacheValues(applicationNames);
    }

    public <T extends Serializable> void putValueToCache(String cacheName, String key, T value) {
        if (isKeyAvailable(cacheName, key)) {
            T cacheValue = getValue(cacheName, key);
            if (cacheValue instanceof Set) {
                if (((Set) cacheValue).contains(value)) {
                    ((Set) cacheValue).remove(value);
                    ((Set) cacheValue).add(value);
                } else {
                    ((Set) cacheValue).add(value);
                }
                putValue(cacheName, key, cacheValue);
            } else {
                putValue(cacheName, key, value);
            }
        } else {
            putValue(cacheName, key, value);
        }
    }

    public <T extends Serializable> void deleteCacheValue(String cacheName, String key, T value) {
        if (isKeyAvailable(cacheName, key)) {
            T cacheValue = getValue(cacheName, key);
            if (cacheValue instanceof Set) {
                ((Set) cacheValue).remove(value);
                putValue(cacheName, key, cacheValue);
            } else {
                putValue(cacheName, key, value);
            }
        }
    }

    private <T extends Serializable> void prepareCache(String cacheName, String key, T type) {
        if (isKeyNotAvailable(cacheName, key)) {
            putValue(cacheName, key, type);
        }
    }

    public <T extends Serializable> T getValue(String cacheName, String key) {
        return (T) cacheManager.getCache(cacheName).get(key).get();
    }

    private boolean isKeyAvailable(String cacheName, String key) {
        return cacheManager.getCache(cacheName).get(key) != null ? true : false;
    }

    private boolean isKeyNotAvailable(String cacheName, String key) {
        return !isKeyAvailable(cacheName, key);
    }

    private <T extends Serializable> void putValue(String cacheName, String key, T value) {
        cacheManager.getCache(cacheName).put(key, value);
    }

    public void createCacheNamesAndKeys(Set<String> applicationNames) {
        for (String applicationName : applicationNames) {
            for (CacheType cacheType : CacheType.values()) {
                cacheManager.getCache(cacheType.cacheName()).clear();
                if (cacheType.equals(CacheType.CACHE_NAME_FOR_SERVER)) {
                    prepareCache(cacheType.cacheName(), "allConfigs", cacheType.getCacheValueType());
                } else {
                    prepareCache(cacheType.cacheName(), applicationName, cacheType.getCacheValueType());
                }
            }
        }
    }

    public void putCacheValues(Set<String> applicationNames) {
        for (String applicationName : applicationNames) {
            ArrayList<Config> configs = new ArrayList<>();
            configRepository.findAllByApplicationName(applicationName).forEach(configs::add);
            for (Config config : configs) {
                ConfigResponse configResponse = ConfigConverter.convertConfig(config);
                putValueToCache(CacheType.CACHE_NAME_FOR_SERVER.cacheName(), "allConfigs", config);
                putValueToCache(CacheType.CACHE_NAME_FOR_CLIENT.cacheName(), applicationName, configResponse);
            }
        }
    }
}
