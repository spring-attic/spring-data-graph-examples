package org.springframework.data.neo4j.examples.hellograph;

import org.springframework.data.graph.annotation.GraphProperty;
import org.springframework.data.graph.annotation.NodeEntity;
import org.springframework.data.graph.annotation.RelatedTo;
import org.springframework.data.graph.core.Direction;
import org.springframework.data.graph.core.NodeBacked;

import java.util.Set;

/**
 * A Spring Data Graph enhanced World entity.
 * <p/>
 * This is the initial POJO in the Universe.
 */
@NodeEntity
public class World
{
    @GraphProperty
    private String name;

    @GraphProperty
    private int moons;

    @RelatedTo(type = "REACHABLE_BY_ROCKET", elementClass = World.class, direction = Direction.BOTH)
    private Set<World> reachableByRocket;

    public World( String name, int moons )
    {
        this.name = name;
        this.moons = moons;
    }

//    public World( String name )
//    {
//        this( name, 0 );
//    }

    public World()
    {
//        this( "world" );
    }

    public String getName()
    {
        return name;
    }

    public int getMoons()
    {
        return moons;
    }

    @Override
    public String toString()
    {
        return "World{" +
                "name='" + name + '\'' +
                ", moons=" + moons +
                '}';
    }

    public void addRocketRouteTo( World otherWorld )
    {
        ((NodeBacked) this).relateTo( otherWorld, RelationshipTypes.REACHABLE_BY_ROCKET );
    }

    public boolean canBeReachedFrom( World otherWorld )
    {
        return this.reachableByRocket.contains( otherWorld );
    }
}
