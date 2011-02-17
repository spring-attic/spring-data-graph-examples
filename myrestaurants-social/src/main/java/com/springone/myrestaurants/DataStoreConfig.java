package com.springone.myrestaurants;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.data.graph.neo4j.config.AbstractNeo4jConfiguration;

/**
 * @author mh
 * @since 01.02.11
 */
public class DataStoreConfig extends AbstractNeo4jConfiguration {
    @Override
    public boolean isUsingCrossStorePersistence() {
        return true;
    }

    @Override
    public GraphDatabaseService graphDatabaseService() {
        return new EmbeddedGraphDatabase("target/data/recommendation");
    }
}
