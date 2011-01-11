package org.neo4j.examples.spring.hellograph;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.graph.neo4j.config.AbstractNeo4jConfiguration;

/**
 * A custom Neo4jConfiguration
 */
@Configuration
//@Component
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
    //@Bean(destroyMethod = "shutdown")
    
//    @Bean 
//    String someBean() {
//    	return "DUMMY";
//    }

    @Override
    public GraphDatabaseService graphDatabaseService() {
        return new EmbeddedGraphDatabase( storeDir );
    }

    public String getStoreDir()
    {
        return storeDir;
    }
}

