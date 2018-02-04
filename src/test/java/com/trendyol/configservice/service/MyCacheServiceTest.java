package com.trendyol.configservice.service;

import com.trendyol.configservice.model.cassandra.Config;
import com.trendyol.configservice.model.rest.ConfigResponse;
import com.trendyol.configservice.repository.cassandra.ConfigRepository;
import com.trendyol.configservice.utils.CacheType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MyCacheServiceTest {

    @InjectMocks
    private MyCacheService myCacheService;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private ConfigRepository configRepository;

    @Mock
    private Cache cacheForClient;
    @Mock
    private Cache cacheForServer;

    private Set<String> applicationNames = new HashSet<>();

    private ArrayList<Config> allConfigList = new ArrayList<>();

    private Collection<String> cacheNames = new ArrayList<>();

    @Before
    public void init(){
        applicationNames.add("Application1");
        applicationNames.add("Application2");
        applicationNames.add("Application3");

        Config config1 = Config.builder().id(UUID.randomUUID()).applicationName("Application1").isActive((byte)1).name("SiteName").type("String").value("trendyol.com").build();
        Config config2 = Config.builder().id(UUID.randomUUID()).applicationName("Application2").isActive((byte)1).name("maxItemCount").type("Integer").value("50").build();
        Config config3 = Config.builder().id(UUID.randomUUID()).applicationName("Application3").isActive((byte)1).name("isBasketEnabled").type("Boolean").value("true").build();

        allConfigList.add(config1);
        allConfigList.add(config2);
        allConfigList.add(config3);

        cacheNames.add(CacheType.CACHE_NAME_FOR_CLIENT.cacheName());
        cacheNames.add(CacheType.CACHE_NAME_FOR_SERVER.cacheName());


        when(configRepository.findAllApplicationNames()).thenReturn(allConfigList);
        when(cacheManager.getCache(CacheType.CACHE_NAME_FOR_CLIENT.cacheName())).thenReturn(cacheForClient);
        when(cacheManager.getCache(CacheType.CACHE_NAME_FOR_SERVER.cacheName())).thenReturn(cacheForServer);
        when(cacheManager.getCacheNames()).thenReturn(cacheNames);





        Set<Config> configSet = new HashSet<>();
        configSet.add(config1);
        when(configRepository.findAllByApplicationName("Application1")).thenReturn(configSet);

        configSet = new HashSet<>();
        configSet.add(config2);
        when(configRepository.findAllByApplicationName("Application2")).thenReturn(configSet);

        configSet = new HashSet<>();
        configSet.add(config3);
        when(configRepository.findAllByApplicationName("Application3")).thenReturn(configSet);

//        assertThat(myCacheService.getValue(CacheType.CACHE_NAME_FOR_CLIENT.cacheName(),"Application1"),equalTo(config1));

        myCacheService.init();

        System.out.println("asd");
    }



    @Test
    public void shouldCreateCacheNamesAndKeys() {

//        myCacheService.init();

//        ArgumentCaptor<Config> captor = ArgumentCaptor.forClass(Config.class);
//
//        verify(cacheManager).getCache(CacheType.CACHE_NAME_FOR_CLIENT.cacheName()).put("Application1",captor.capture());

        System.out.println("");


    }

    @Test
    public void deleteCacheValue() {
    }

    @Test
    public void createCacheNamesAndKeys() {
    }

    @Test
    public void putCacheValues() {
    }
}