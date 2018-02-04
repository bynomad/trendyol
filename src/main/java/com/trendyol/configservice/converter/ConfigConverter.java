package com.trendyol.configservice.converter;

import com.trendyol.configservice.model.cassandra.Config;
import com.trendyol.configservice.model.rest.ConfigResponse;

public class ConfigConverter {

    public static ConfigResponse convertConfig(Config config){
        ConfigResponse configResponse = new ConfigResponse();
        configResponse.setName(config.getName());
        configResponse.setValue(config.getValue());
        configResponse.setApplicationName(config.getApplicationName());
        configResponse.setId(config.getId());
        configResponse.setIsActive(config.getIsActive());
        configResponse.setType(config.getType());

        return configResponse;
    }
}
