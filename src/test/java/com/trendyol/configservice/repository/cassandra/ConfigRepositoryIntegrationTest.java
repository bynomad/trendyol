package com.trendyol.configservice.repository.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.trendyol.configservice.config.CassandraConfig;
import com.trendyol.configservice.model.cassandra.Config;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CassandraConfig.class)
public class ConfigRepositoryIntegrationTest {

    @Autowired
    private ConfigRepository configRepository;

    private Config config = Config.builder().id(UUIDs.timeBased()).applicationName("TestApplication").isActive((byte) 1).name("Test").type("String").value("trendyol.com").build();

    @BeforeClass
    public static void startCassandraEmbedded() throws Exception {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
        Cluster cluster = Cluster.builder()
                .addContactPoints("localhost").withPort(9142).build();
        Session session = cluster.connect();
    }

    @AfterClass
    public static void stopCassandraEmbedded() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }

    @Before
    public void init() {
        configRepository.save(config);
    }

    @After
    public void clearDatabase() {
        configRepository.delete(config);
    }

    @Test
    public void shouldFindByName() {
        Config configTest = configRepository.findByName(config.getApplicationName(), config.getName());
        assertThat(configTest, equalTo(config));
    }

    @Test
    public void shouldFindAllByApplicationName() {
        Set<Config> configs = configRepository.findAllByApplicationName(config.getApplicationName());
        assertThat(configs.size(), equalTo(1));
    }

    @Test
    public void shouldFindById() {
        Config configTest = configRepository.findById(config.getId(), config.getName());
        assertThat(configTest, equalTo(config));
    }

}