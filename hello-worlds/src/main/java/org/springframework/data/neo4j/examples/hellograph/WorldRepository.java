package org.springframework.data.neo4j.examples.hellograph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.graph.neo4j.finder.FinderFactory;
import org.springframework.data.graph.neo4j.finder.NodeFinder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Spring Data Graph backed application context for Worlds.
 */
@Repository
public class WorldRepository
{
	@Autowired
	private FinderFactory finderFactory;

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
        return (World) createFinder().findById(id);
    }

    public Iterable<World> findAllWorlds()
    {
        return (Iterable<World>) createFinder().findAll();
    }

    public long countWorlds()
    {
        return createFinder().count();
    }

    public World findWorldNamed( String name )
    {
        return (World) createFinder().findByPropertyValue("node","name", name);
    }

    public World findWorldWithMoons( long moonCount )
    {
        return createFinder().findById(moonCount);
    }
    public Iterable<World> findWorldsWithMoons( int moonCount )
    {
        return (Iterable<World>) createFinder().findAllByPropertyValue("node","moons", moonCount);
    }

    private NodeFinder<World> createFinder() {
        return finderFactory.createNodeEntityFinder( World.class );
    }

    public Iterable<World> exploreWorldsBeyond( World homeWorld )
    {
        return null;
    }

}
