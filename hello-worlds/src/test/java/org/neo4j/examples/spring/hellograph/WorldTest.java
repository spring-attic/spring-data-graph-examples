package org.neo4j.examples.spring.hellograph;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * Exploratory unit-tests for the Spring Data Graph annotated World entity.
 */
public class WorldTest
{
    private GraphBackedGalaxy galaxy;

    /**
     * Since the World is a @NodeEntity, the SpringDataGraph must
     * be setup before you can even create instances of the POJO.
     *
     * The GraphBackedGalaxy takes care of all the setup, so use that.
     */
    @Before
    public void setupSpringDataGraph()
    {
        galaxy = new GraphBackedGalaxy();
    }

    @After
    public void shutdownSpringDataGraph()
    {
        galaxy.shutdownEverythingAndLeaveNoTrace();
    }

    @Test
    public void shouldBeSimpleToCreateNewEntities()
    {
        World w = new World();
    }

    @Test
    public void shouldHaveDefaultName()
    {
        final String expectedName = "world";
        World w = new World();

        assertThat( expectedName, containsString( w.getName() ) );
    }
}
