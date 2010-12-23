package org.neo4j.examples.imdb.util;

import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

public interface PathFinder
{
    List<Node> shortestPath( Node startNode, Node endNode, RelationshipType relType );
}