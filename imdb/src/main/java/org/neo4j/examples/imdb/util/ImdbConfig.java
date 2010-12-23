package org.neo4j.examples.imdb.util;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.data.graph.neo4j.config.AbstractNeo4jConfiguration;

public class ImdbConfig extends AbstractNeo4jConfiguration {
    @Override
    public boolean isUsingCrossStorePersistence() {
        return false;
    }

    @Bean(destroyMethod = "shutDown")
    public GraphDatabaseService graphDatabaseService() {
        return new EmbeddedGraphDatabase("target/neo4j-db");
    }
}
