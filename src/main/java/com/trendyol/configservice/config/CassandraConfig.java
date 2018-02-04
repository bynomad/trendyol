package com.trendyol.configservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@PropertySource(value = "classpath:cassandra.properties")
@EnableCassandraRepositories(basePackages = "com.trendyol.configservice.repository.cassandra")
@Slf4j
public class CassandraConfig extends AbstractCassandraConfiguration {

    private final Environment environment;

    @Autowired
    public CassandraConfig(Environment environment){
        this.environment = environment;
    }

    @Override
    protected String getKeyspaceName() {
        return environment.getProperty("cassandra.keyspace");
    }

    @Override
    @Bean
    public CassandraCqlClusterFactoryBean cluster() {
        final CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setKeyspaceCreations(getKeySpaces());
        cluster.setContactPoints(environment.getProperty("cassandra.contactpoints"));
        cluster.setPort(Integer.parseInt(environment.getProperty("cassandra.port")));
        log.info("Cassandra cluster created succesfully");
        return cluster;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    public String[] getEntityBasePackages() {
        String[] basePackages = {"com.trendyol.configservice.model.cassandra"};
        return basePackages;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        String keySpacename = environment.getProperty("cassandra.keyspace");
        if (Boolean.parseBoolean(environment.getProperty("cassandra.keyspace"))) {
            return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(keySpacename).withNetworkReplication().ifNotExists());
        } else {
            return super.getKeyspaceCreations();
        }
    }

    protected List<CreateKeyspaceSpecification> getKeySpaces() {
        List<CreateKeyspaceSpecification> keySpaceSpecifications = new ArrayList<>();
        keySpaceSpecifications.add(getKeySpaceNames());
        return keySpaceSpecifications;
    }

    private CreateKeyspaceSpecification getKeySpaceNames() {
        CreateKeyspaceSpecification keySpace = new CreateKeyspaceSpecification();
        keySpace.name(getKeyspaceName());
        keySpace.ifNotExists(true).createKeyspace();
        return keySpace;
    }
}
