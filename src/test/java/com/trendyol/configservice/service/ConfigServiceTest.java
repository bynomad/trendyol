package com.trendyol.configservice.service;

import com.datastax.driver.core.utils.UUIDs;
import com.trendyol.configservice.model.cassandra.Config;
import com.trendyol.configservice.model.rest.ConfigResponse;
import com.trendyol.configservice.repository.cassandra.ConfigRepository;
import com.trendyol.configservice.utils.CacheType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ConfigServiceTest {

    @InjectMocks
    private ConfigService configService;

    @Mock
    private ConfigRepository configRepository;

    @Mock
    private MyCacheService myCacheService;

    @Test
    public void shouldFindByName() {
        Config config = Config.builder().id(UUIDs.timeBased()).applicationName("TestApplication").isActive((byte) 1).name("Test").type("String").value("trendyol.com").build();

        when(configRepository.findByName(config.getApplicationName(), config.getName())).thenReturn(config);

        ConfigResponse configResponse = configService.findByName(config.getApplicationName(), config.getName());

        Mockito.verify(configRepository).findByName(config.getApplicationName(), config.getName());
        Assert.assertThat(config.getId(), equalTo(configResponse.getId()));
    }

    @Test
    public void shouldCreateConfig(){
        Config config = Config.builder().id(UUIDs.timeBased()).applicationName("TestApplication").isActive((byte) 1).name("Test").type("String").value("trendyol.com").build();

        ConfigResponse configResponse = configService.createConfig(config);

        verify(configRepository).save(config);
        verify(myCacheService).putValueToCache(CacheType.CACHE_NAME_FOR_CLIENT.cacheName(), config.getApplicationName(), configResponse);
        verify(myCacheService).putValueToCache(CacheType.CACHE_NAME_FOR_SERVER.cacheName(), "allConfigs", config);

        Assert.assertThat(config.getId(), equalTo(configResponse.getId()));

    }

    @Test
    public void shouldUpdateConfig(){
        Config config = Config.builder().id(UUIDs.timeBased()).applicationName("TestApplication").isActive((byte) 1).name("Test").type("String").value("trendyol.com").build();
        ArgumentCaptor<ConfigResponse> captor = ArgumentCaptor.forClass(ConfigResponse.class);

        configService.updateConfig(config);

        verify(configRepository).save(config);
        verify(myCacheService).putValueToCache(eq(CacheType.CACHE_NAME_FOR_CLIENT.cacheName()), eq(config.getApplicationName()), captor.capture());
        verify(myCacheService).putValueToCache(CacheType.CACHE_NAME_FOR_CLIENT.cacheName(), config.getApplicationName(), captor.getValue());
        verify(myCacheService).putValueToCache(CacheType.CACHE_NAME_FOR_SERVER.cacheName(), "allConfigs", config);

        Assert.assertThat(config.getId(), equalTo(captor.getValue().getId()));

    }

    @Test
    public void shouldDeleteConfig(){
        Config config = Config.builder().id(UUIDs.timeBased()).applicationName("TestApplication").isActive((byte) 1).name("Test").type("String").value("trendyol.com").build();
        ArgumentCaptor<ConfigResponse> captor = ArgumentCaptor.forClass(ConfigResponse.class);

        when(configRepository.findById(config.getId(), config.getApplicationName())).thenReturn(config);

        configService.deleteConfig(config.getId(),config.getApplicationName());

        verify(configRepository).deleteConfig(config.getId(),config.getApplicationName());
        verify(myCacheService).deleteCacheValue(eq(CacheType.CACHE_NAME_FOR_CLIENT.cacheName()), eq(config.getApplicationName()), captor.capture());
        verify(myCacheService).deleteCacheValue(CacheType.CACHE_NAME_FOR_CLIENT.cacheName(), config.getApplicationName(), captor.getValue());
        verify(myCacheService).deleteCacheValue(CacheType.CACHE_NAME_FOR_SERVER.cacheName(), "allConfigs", config);

        Assert.assertThat(config.getId(), equalTo(captor.getValue().getId()));

    }
}
