package org.neo4j.examples.spring.hellograph;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.graph.core.NodeBacked;

import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.junit.internal.matchers.StringContains.containsString;

/**
 * Exploratory testing of Spring Data Graph using
 * the GraphBackedGalaxy.
 */
public class GraphBackedGalaxyTest
{

    private GraphBackedGalaxy galaxy;

    @Before
    public void createGalaxy()
    {
        galaxy = new GraphBackedGalaxy();
    }

    @After
    public void destroyGalaxy()
    {
        galaxy.shutdownEverythingAndLeaveNoTrace();
    }

    @Test
    public void shouldAllowDirectWorldCreation()
    {
        World myWorld = new World( "mine" );
    }

    @Test
    public void shouldPopulateGalaxyWithWorlds()
    {
        Iterable<World> worlds = galaxy.makeSomeWorlds();
        assertNotNull( worlds );
    }

    @Test
    public void shouldFindWorldsById()
    {
        for ( World w : galaxy.makeSomeWorlds() )
        {
            assertNotNull( galaxy.findWorldIdentifiedBy( ((NodeBacked) w).getNodeId() ) );
        }
    }

    @Test
    public void shouldFindAllWorlds()
    {
        Collection<World> madeWorlds = galaxy.makeSomeWorlds();
        Iterable<World> foundWorlds = galaxy.findAllWorlds();

        int countOfFoundWorlds = 0;
        for ( World foundWorld : foundWorlds )
        {
            assertTrue( madeWorlds.contains( foundWorld ) );
            countOfFoundWorlds++;
        }

        assertEquals( madeWorlds.size(), countOfFoundWorlds );
    }

    @Test
    public void shouldKnowCountOfWorlds()
    {
        Collection<World> madeWorlds = galaxy.makeSomeWorlds();

        assertEquals( madeWorlds.size(), galaxy.countWorlds() );

    }

    @Test
    public void shouldFindWorldsByName()
    {
        for ( World w : galaxy.makeSomeWorlds() )
        {
            assertNotNull( galaxy.findWorldNamed( w.getName() ) );
        }
    }

    @Test
    public void shouldFindWorldsWith1Moon()
    {
        Collection<World> knownWorlds = galaxy.makeSomeWorlds();

        for ( World worldWithOneMoon : galaxy.findWorldsWithMoons( 1 ) )
        {
            assertThat( worldWithOneMoon.getName(), is( anyOf( containsString( "Earth" ), containsString( "Midgard" ) ) ) );
        }
    }

    @Test
    public void shouldReachMarsFromEarth()
    {
        Collection<World> knownWorlds = galaxy.makeSomeWorlds();

        World earth = galaxy.findWorldNamed( "Earth" );
        World mars = galaxy.findWorldNamed( "Mars" );

        assertTrue( mars.canBeReachedFrom( earth ) );
        // assertFalse( earth.canBeReachedFrom( mars ) ); // ABKNOTE: why is the relationship one way?
    }

}
