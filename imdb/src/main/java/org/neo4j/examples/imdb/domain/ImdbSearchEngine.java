package org.neo4j.examples.imdb.domain;

import org.neo4j.graphdb.Node;

public interface ImdbSearchEngine
{
    void indexActor( Actor actor );

    void indexMovie( Movie movie );

    Node searchActor( String name );

    Node searchMovie( String title );
}
