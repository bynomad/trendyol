package com.trendyol.configservice.service;

import com.trendyol.configservice.converter.ConfigConverter;
import com.trendyol.configservice.model.cassandra.Config;
import com.trendyol.configservice.model.rest.ConfigResponse;
import com.trendyol.configservice.repository.cassandra.ConfigRepository;
import com.trendyol.configservice.utils.CacheType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@EnableCaching
@Slf4j
public class ConfigService {
    private final String CACHE_NAME_FOR_CLIENT = "configListForClient";
    private final String CACHE_NAME_FOR_SERVER = "configListForServer";

    private final ConfigRepository configRepository;
    private final MyCacheService myCacheService;

    @Autowired
    public ConfigService(ConfigRepository configRepository,MyCacheService myCacheService){
        this.configRepository = configRepository;
        this.myCacheService = myCacheService;
    }

    public Config findById(UUID id, String applicationName) {
        return configRepository.findById(id, applicationName);
    }

    public ConfigResponse findByName(String applicationName, String name) {
        Config config = configRepository.findByName(applicationName, name);
        ConfigResponse configResponse = ConfigConverter.convertConfig(config);
        return configResponse;
    }

    @Cacheable(value = CACHE_NAME_FOR_CLIENT, key = "#applicationName")
    public Set<ConfigResponse> findAllByApplicationName(String applicationName) {
        Set<ConfigResponse> configResponses = new HashSet<>();
        ArrayList<Config> configs = new ArrayList<>();
        configRepository.findAllByApplicationName(applicationName).forEach(configs::add);
        for (Config config : configs) {
            ConfigResponse configResponse = ConfigConverter.convertConfig(config);
            configResponses.add(configResponse);
        }
        return configResponses;
    }

    public ConfigResponse createConfig(Config config) {
        configRepository.save(config);
        ConfigResponse configResponse = ConfigConverter.convertConfig(config);
        putValueToCache(config, configResponse);

        return configResponse;
    }

    @Cacheable(value = CACHE_NAME_FOR_SERVER, key = "'allConfigs'")
    public Set<Config> findAll() {
        Set<Config> configList = new HashSet<>();
        configRepository.findAll().forEach(configList::add);
        return configList;
    }

    public void updateConfig(Config config) {
        configRepository.save(config);
        ConfigResponse configResponse = ConfigConverter.convertConfig(config);
        putValueToCache(config, configResponse);
    }

    public void deleteConfig(UUID id, String applicationName) {
        Config config = configRepository.findById(id, applicationName);
        ConfigResponse configResponse = ConfigConverter.convertConfig(config);
        configRepository.deleteConfig(id, applicationName);

        myCacheService.deleteCacheValue(CACHE_NAME_FOR_CLIENT, config.getApplicationName(), configResponse);
        myCacheService.deleteCacheValue(CACHE_NAME_FOR_SERVER, "allConfigs", config);
    }

    public void putValueToCache(Config config, ConfigResponse configResponse) {
        myCacheService.putValueToCache(CacheType.CACHE_NAME_FOR_CLIENT.cacheName(), config.getApplicationName(), configResponse);
        myCacheService.putValueToCache(CacheType.CACHE_NAME_FOR_SERVER.cacheName(), "allConfigs", config);
    }
}
