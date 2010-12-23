package org.neo4j.examples.imdb.domain;

import org.neo4j.graphdb.RelationshipType;

public enum ImdbSearchRelTypes implements RelationshipType
{
    PART_OF_NAME, PART_OF_TITLE
}
