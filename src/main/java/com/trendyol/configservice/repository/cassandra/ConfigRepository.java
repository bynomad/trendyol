package com.trendyol.configservice.repository.cassandra;

import com.trendyol.configservice.model.cassandra.Config;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ConfigRepository extends CassandraRepository<Config> {

    @Query("Select * from config where applicationname = ?0 and name = ?1 ALLOW FILTERING")
    Config findByName(String applicationName, String name);

    @Query("Select * from config where applicationname = ?0 ALLOW FILTERING")
    Set<Config> findAllByApplicationName(String applicationName);

    @Query("Delete from config where id = ?0 and applicationname = ?1")
    void deleteConfig(UUID id, String applicationName);

    @Query("Select * from config where id = ?0 and applicationname = ?1")
    Config findById(UUID id, String applicationName);

    @Query("Select applicationname from config")
    ArrayList<Config> findAllApplicationNames();
}
