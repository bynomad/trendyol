package com.trendyol.configservice.controller;

import com.datastax.driver.core.utils.UUIDs;
import com.trendyol.configservice.model.cassandra.Config;
import com.trendyol.configservice.service.ConfigService;
import com.trendyol.configservice.utils.ConfigType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/config")
@Slf4j
public class ConfigManagementController {

    private final ConfigService configService;

    @Autowired
    public ConfigManagementController(ConfigService configService){
        this.configService = configService;
    }

    @GetMapping("/create")
    public String getCreatePage(Map<String, Object> model) {
        Config config = new Config();
        model.put("config", config);
        model.put("configTypes", ConfigType.values());
        return "addConfig";
    }

    @GetMapping("/update/{id}/{applicationName}")
    public String getUpdatePage(@PathVariable("id") UUID id, @PathVariable("applicationName") String applicationName, Map<String, Object> model) {
        Config config = configService.findById(id, applicationName);
        model.put("config", config);
        model.put("configTypes", ConfigType.values());
        return "updateConfig";
    }

    @PostMapping("/addConfig")
    public RedirectView addConfig(@ModelAttribute("config") Config config) {
        config.setId(UUIDs.timeBased());
        configService.createConfig(config);
        return new RedirectView("/config/list");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RedirectView updateConfig(@ModelAttribute("config") Config config) {
        configService.updateConfig(config);
        return new RedirectView("/config/list");
    }

    @PostMapping("/delete/{id}/{applicationName}")
    public RedirectView deleteConfig(@PathVariable("id") UUID id, @PathVariable("applicationName") String applicationName) {
        configService.deleteConfig(id, applicationName);
        return new RedirectView("/config/list");
    }

    @GetMapping("/list")
    public String list(Map<String, Object> model) {
        Set<Config> configList = configService.findAll();
        model.put("configList", configList);

        return "listConfig";
    }
}

