package com.trendyol.configservice.controller;

import com.trendyol.configservice.model.rest.ConfigResponse;
import com.trendyol.configservice.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(value = "/api")
public class ConfigClientController {

    private final ConfigService configService;

    @Autowired
    public ConfigClientController(ConfigService configService){
        this.configService = configService;
    }

    @GetMapping(value = "/config/{applicationName}/{key}")
    ConfigResponse findByName(@PathVariable String applicationName, @PathVariable String key) {
        ConfigResponse configResponse = configService.findByName(applicationName, key);
        return configResponse;
    }

    @GetMapping(value = "/config/{applicationName}")
    Set<ConfigResponse> findByName(@PathVariable String applicationName) {
        Set<ConfigResponse> configResponse = configService.findAllByApplicationName(applicationName);
        return configResponse;
    }
}
