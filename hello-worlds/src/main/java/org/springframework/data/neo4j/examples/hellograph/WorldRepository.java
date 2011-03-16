package org.springframework.data.neo4j.examples.hellograph;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.graph.neo4j.finder.FinderFactory;
import org.springframework.data.graph.neo4j.finder.NodeFinder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.springframework.data.neo4j.examples.hellograph.RelationshipTypes.REACHABLE_BY_ROCKET;

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
        newWorlds.add( world( "Mercury", 0 ) );
        newWorlds.add( world( "Venus", 0 ) );
        World earth = world( "Earth", 1 );
        newWorlds.add( earth );
        World mars = world( "Mars", 2 );
        mars.addRocketRouteTo( earth );
        newWorlds.add( mars );
        newWorlds.add( world( "Jupiter", 63 ) );
        newWorlds.add( world( "Saturn", 62 ) );
        newWorlds.add( world( "Uranus", 27 ) );
        newWorlds.add( world( "Neptune", 13 ) );

        // Norse worlds
        newWorlds.add( world( "Alfheimr", 0 ) );
        newWorlds.add( world( "Midgard", 1 ) );
        newWorlds.add( world( "Muspellheim", 2 ) );
        newWorlds.add( world( "Asgard", 63 ) );
        newWorlds.add( world( "Hel", 62 ) );

        return newWorlds;
    }


    @Transactional
    public World world(String name, int moons) {
        return new World(name,moons).persist();
    }

    public World findWorldIdentifiedBy( long id )
    {
        return finder().findById(id);
    }

    private NodeFinder<World> finder() {
        return finderFactory.createNodeEntityFinder( World.class );
    }

    public Iterable<World> findAllWorlds()
    {
        return finder().findAll();
    }

    public long countWorlds()
    {
        return finder().count();
    }

    public World findWorldNamed( String name )
    {
        return finder().findByPropertyValue( null, "name", name );
    }

    public World findWorldWithMoons( long moonCount )
    {
        return finder().findByPropertyValue( "moon-index","moons",moonCount );
    }
    public Iterable<World> findWorldsWithMoons( int moonCount )
    {
        return  finder().findAllByPropertyValue( "moon-index","moons", moonCount );
    }

    public Iterable<World> exploreWorldsBeyond( World homeWorld )
    {
        TraversalDescription traversal = Traversal.description().relationships(withName(REACHABLE_BY_ROCKET), Direction.OUTGOING);
        return finder().findAllByTraversal(homeWorld, traversal);
    }

}
