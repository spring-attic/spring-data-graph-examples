package org.neo4j.examples.spring.hellograph;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.graph.neo4j.config.AbstractNeo4jConfiguration;

import java.io.File;

/**
 * A custom Neo4jConfiguration
 */
public class HelloWorldNeo4jConfiguration extends AbstractNeo4jConfiguration
{
    private String storeDir = "target/neo4j-db";

    @Override
    public boolean isUsingCrossStorePersistence() {
        return false;
    }

    // ABKNOTE
    // - @Bean duplicated in overridden parent method. why both places?
    // - destroyMethod does not seem to do anything.
    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new EmbeddedGraphDatabase( storeDir );
    }

    public String getStoreDir()
    {
        return storeDir;
    }
}

