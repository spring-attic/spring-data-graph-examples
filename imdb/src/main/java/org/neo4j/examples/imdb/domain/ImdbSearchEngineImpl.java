package org.neo4j.examples.imdb.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.index.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.graph.neo4j.support.GraphDatabaseContext;

public class ImdbSearchEngineImpl implements ImdbSearchEngine
{
    private static final String NAME_PART_INDEX = "name.part";
    private static final String WORD_PROPERTY = "word";
    private static final String COUNT_PROPERTY = "count_uses";
    private static final String TITLE_PART_INDEX = "title.part";

    @Autowired
    private GraphDatabaseContext graphDatabaseContext;

    public void indexActor( Actor actor )
    {
        index( actor.getName(), ((Actor) actor).getUnderlyingState(),
            NAME_PART_INDEX, ImdbSearchRelTypes.PART_OF_NAME );
    }

    public void indexMovie( Movie movie )
    {
        index( movie.getTitle(), ((Movie) movie).getUnderlyingState(),
            TITLE_PART_INDEX, ImdbSearchRelTypes.PART_OF_TITLE );
    }


    private Node getSingleNode(String property, String value) {
        for (Node node : nodeIndex().get(property, value)) {
            return node;
        }
        return null;
    }

    private Index<Node> nodeIndex() {
        return graphDatabaseContext.getNodeIndex(null);
    }

    public Node searchActor( String name )
    {
        return searchSingle( name, NAME_PART_INDEX, ImdbSearchRelTypes.PART_OF_NAME );
    }

    public Node searchMovie( String title )
    {
        return searchSingle( title, TITLE_PART_INDEX, ImdbSearchRelTypes.PART_OF_TITLE );
    }

    private String[] splitSearchString( final String value )
    {
        return value.toLowerCase( Locale.ENGLISH ).split( "[^\\w]+" );
    }

    private void index( final String value, final Node node,
        final String partIndexName, final ImdbSearchRelTypes relType )
    {
        for ( String part : splitSearchString( value ) )
        {
            Node wordNode = getSingleNode( partIndexName, part );
            if ( wordNode == null )
            {
                wordNode = graphDatabaseContext.createNode();
                // not needed for the functionality
                nodeIndex().add( wordNode, partIndexName, part );

                wordNode.setProperty( WORD_PROPERTY, part );
            }
            wordNode.createRelationshipTo( node, relType );
            wordNode.setProperty( COUNT_PROPERTY, ((Integer) wordNode
                .getProperty( COUNT_PROPERTY, 0 )) + 1 );
        }
    }

    private Node searchSingle( final String value, final String indexName,
        final ImdbSearchRelTypes wordRelType )
    {
        // get the words in the search
        final List<Node> wordList = findSearchWords( value, indexName );
        if ( wordList.isEmpty() )
        {
            return null;
        }
        final Node startNode = wordList.remove( 0 );
        // set up a match to use if everything else fails
        Node match = startNode.getRelationships( wordRelType ).iterator()
            .next().getEndNode();
        // check if there is only one node in the list
        if ( wordList.isEmpty() )
        {
            return match;
        }
        int bestCount = 0;
        final int listSize = wordList.size();
        for ( Relationship targetRel : startNode.getRelationships( wordRelType ) )
        {
            Node targetNode = targetRel.getEndNode();
            int hitCount = 0;
            for ( Relationship wordRel : targetNode
                .getRelationships( wordRelType ) )
            {
                if ( wordList.contains( wordRel.getStartNode() ) )
                {
                    if ( ++hitCount == listSize )
                    {
                        return targetNode;
                    }
                }
            }
            if ( hitCount > bestCount )
            {
                match = targetNode;
                bestCount = hitCount;
            }
        }
        return match;
    }

    private List<Node> findSearchWords( final String userInput,
        final String partIndexName )
    {
        final List<Node> wordList = new ArrayList<Node>();
        // prepare search terms
        for ( String part : splitSearchString( userInput ) )
        {
            Node wordNode = getSingleNode( partIndexName, part );
            if ( wordNode == null || !wordNode.hasRelationship()
                || wordList.contains( wordNode ) )
            {
                continue;
            }
            wordList.add( wordNode );
        }
        if ( wordList.isEmpty() )
        {
            return Collections.emptyList();
        }
        // sort words according to the number of relationships (ascending)
        Collections.sort( wordList, new Comparator<Node>()
        {
            public int compare( final Node left, final Node right )
            {
                int leftCount = (Integer) left.getProperty( COUNT_PROPERTY, 0 );
                int rightCount = (Integer) right
                    .getProperty( COUNT_PROPERTY, 0 );
                return leftCount - rightCount;
            }
        } );
        return wordList;
    }
}
