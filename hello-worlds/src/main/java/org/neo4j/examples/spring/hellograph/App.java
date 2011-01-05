package org.neo4j.examples.spring.hellograph;

/**
 * Hello world(s)!
 * <p/>
 * An example application for exploring Spring Data Graph.
 */
public class App
{

    public static void main( String[] args )
    {
        GraphBackedGalaxy galaxy = new GraphBackedGalaxy();

        Iterable<World> worlds = galaxy.makeSomeWorlds();

        World homeWorld = worlds.iterator().next();
        System.out.println("At home on: " + homeWorld);

        World foundHomeWorld = galaxy.findWorldNamed( homeWorld.getName() );
        System.out.println( "found home world: " + foundHomeWorld );

        Iterable<World> foundWorlds = galaxy.exploreWorldsBeyond( homeWorld );

        galaxy.shutdownEverythingAndLeaveNoTrace();
    }

}
