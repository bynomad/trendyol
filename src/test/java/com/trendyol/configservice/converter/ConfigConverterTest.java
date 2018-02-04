package com.trendyol.configservice.converter;

import com.datastax.driver.core.utils.UUIDs;
import com.trendyol.configservice.model.cassandra.Config;
import com.trendyol.configservice.model.rest.ConfigResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class ConfigConverterTest {

    private ConfigConverter configConverter;

    @Test
    public void shouldConvertCorrect(){
        Config config = Config.builder().id(UUIDs.timeBased()).applicationName("TestApplication").isActive((byte) 1).name("Test").type("String").value("trendyol.com").build();
        ConfigResponse configResponse = ConfigConverter.convertConfig(config);

        assertThat(config.getApplicationName(),equalTo(configResponse.getApplicationName()));
        assertThat(config.getName(),equalTo(configResponse.getName()));
        assertThat(config.getId(),equalTo(configResponse.getId()));
        assertThat(config.getIsActive(),equalTo(configResponse.getIsActive()));
        assertThat(config.getType(),equalTo(configResponse.getType()));
        assertThat(config.getValue(),equalTo(configResponse.getValue()));
    }

}