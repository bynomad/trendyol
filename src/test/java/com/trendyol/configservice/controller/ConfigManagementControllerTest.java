package com.trendyol.configservice.controller;

import com.datastax.driver.core.utils.UUIDs;
import com.trendyol.configservice.model.cassandra.Config;
import com.trendyol.configservice.service.ConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ConfigManagementControllerTest {

    @InjectMocks
    private ConfigManagementController configManagementController;

    @Mock
    private ConfigService configService;


    @Test
    public void shouldAddConfig() {
        Config config = Config.builder().applicationName("TestApplication").isActive((byte) 1).name("Test").type("String").value("trendyol.com").build();

        configManagementController.addConfig(config);

        ArgumentCaptor<Config> configCaptor = ArgumentCaptor.forClass(Config.class);
        verify(configService).createConfig(configCaptor.capture());

        assertNotNull(configCaptor.getValue().getId());
    }

    @Test
    public void shouldGetCreatePage() {
        Map<String, Object> model = new HashMap<>();

        String pageUrl = configManagementController.getCreatePage(model);

        assertThat(model.size(), equalTo(2));
        assertThat(pageUrl, equalTo("addConfig"));
    }

    @Test
    public void shouldGetUpdatePage() {
        Map<String, Object> model = new HashMap<>();
        UUID uuid = UUIDs.timeBased();

        String pageUrl = configManagementController.getUpdatePage(uuid, "Application1", model);

        verify(configService).findById(uuid, "Application1");

        assertThat(model.size(), equalTo(2));
        assertThat(pageUrl, equalTo("updateConfig"));
    }

    @Test
    public void shouldUpdateConfig() {
        Config config = Config.builder().applicationName("TestApplication").isActive((byte) 1).name("Test").type("String").value("trendyol.com").build();

        configManagementController.updateConfig(config);

        verify(configService).updateConfig(config);

    }

    @Test
    public void shouldDeleteConfig() {
        Config config = Config.builder().applicationName("TestApplication").isActive((byte) 1).name("Test").type("String").value("trendyol.com").build();

        configManagementController.deleteConfig(config.getId(), config.getApplicationName());

        verify(configService).deleteConfig(config.getId(), config.getApplicationName());

    }

    @Test
    public void shouldListPage() {
        Map<String, Object> model = new HashMap<>();
        Set<Config> configList = new HashSet<>();

        when(configService.findAll()).thenReturn(configList);

        configManagementController.list(model);

        verify(configService).findAll();

        assertThat(configList, equalTo(model.get("configList")));
    }
}