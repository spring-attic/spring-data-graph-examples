package org.springframework.data.neo4j.examples.hellograph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.graph.neo4j.finder.FinderFactory;
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

    public World findWorldWithMoons( long moonCount )
    {
        return finderFactory.getFinderForClass( World.class ).findById( moonCount );
    }
    public Iterable<World> findWorldsWithMoons( int moonCount )
    {
        return (Iterable<World>) finderFactory.getFinderForClass( World.class ).findAllByPropertyValue( "moons", moonCount );
    }

    public Iterable<World> exploreWorldsBeyond( World homeWorld )
    {
        return null;
    }

}
