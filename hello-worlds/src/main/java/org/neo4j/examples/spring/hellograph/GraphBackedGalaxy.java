package org.neo4j.examples.spring.hellograph;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.graph.neo4j.finder.FinderFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Spring Data Graph backed application context for Worlds.
 */
public class GraphBackedGalaxy
{
    private ConfigurableApplicationContext applicationContext;
    private FinderFactory finderFactory;

    public GraphBackedGalaxy()
    {
        createApplicationBeansFrom( "/spring/helloWorldContext.xml" );
    }

    public void createApplicationBeansFrom( String springApplicationContextFilename )
    {
        // open/read the application context file
        applicationContext = new ClassPathXmlApplicationContext( springApplicationContextFilename );

        finderFactory = (FinderFactory) applicationContext.getBean( "finderFactory" );

    }

    @Transactional
    public Collection<World> makeSomeWorlds()
    {
        ArrayList<World> newWorlds = new ArrayList<World>();

        // solar worlds
        newWorlds.add( new World( "Mercury", 0 ) );
        newWorlds.add( new World( "Venus", 0 ) );
        World earth = new World( "Earth", 1 );
        newWorlds.add( earth );
        World mars = new World( "Mars", 2 );
        mars.addRocketRouteTo( earth );
        newWorlds.add( mars );
        newWorlds.add( new World( "Jupiter", 63 ) );
        newWorlds.add( new World( "Saturn", 62 ) );
        newWorlds.add( new World( "Uranus", 27 ) );
        newWorlds.add( new World( "Neptune", 13 ) );

        // Norse worlds
        newWorlds.add( new World( "Alfheimr", 0 ) );
        newWorlds.add( new World( "Midgard", 1 ) );
        newWorlds.add( new World( "Muspellheim", 2 ) );
        newWorlds.add( new World( "Asgard", 63 ) );
        newWorlds.add( new World( "Hel", 62 ) );

        return newWorlds;
    }

    public World findWorldIdentifiedBy( long id )
    {
        return (World) finderFactory.getFinderForClass( World.class ).findById( id );
    }

    public Iterable<World> findAllWorlds()
    {
        return (Iterable<World>) finderFactory.getFinderForClass( World.class ).findAll();
    }

    public long countWorlds()
    {
        return finderFactory.getFinderForClass( World.class ).count();
    }

    public World findWorldNamed( String name )
    {
        return (World) finderFactory.getFinderForClass( World.class ).findByPropertyValue( "name", name );
    }

    public Iterable<World> findWorldsWithMoons( Integer moonCount )
    {
        return (Iterable<World>) finderFactory.getFinderForClass( World.class ).findAllByPropertyValue( "moons", moonCount );
    }

    public Iterable<World> exploreWorldsBeyond( World homeWorld )
    {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public void shutdownEverythingAndLeaveNoTrace()
    {
        // explicitly grab the GraphDatabaseService and shutdown.
        GraphDatabaseService graphDB = (GraphDatabaseService) applicationContext.getBean( "graphDatabaseService" );
        graphDB.shutdown();

        HelloWorldNeo4jConfiguration neo4jConfiguration = (HelloWorldNeo4jConfiguration) applicationContext.getBean( "neo4jConfiguration" );
        deleteDirectory( new File( neo4jConfiguration.getStoreDir() ) );

        applicationContext.stop();
    }

    private void deleteDirectory( File path )
    {
        if ( path.exists() )
        {
            File[] files = path.listFiles();
            for ( int i = 0; i < files.length; i++ )
            {
                if ( files[i].isDirectory() )
                {
                    deleteDirectory( files[i] );
                }
                else
                {
                    files[i].delete();
                }
            }
        }
    }

}
