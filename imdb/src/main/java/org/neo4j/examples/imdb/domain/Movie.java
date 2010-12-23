package org.neo4j.examples.imdb.domain;

import java.util.Set;

import org.springframework.datastore.graph.annotation.GraphProperty;
import org.springframework.datastore.graph.annotation.NodeEntity;
import org.springframework.datastore.graph.annotation.RelatedTo;
import org.springframework.datastore.graph.annotation.GraphInactive.Relationship;
import org.springframework.datastore.graph.api.Direction;

// START SNIPPET: MovieClass
@NodeEntity
public class Movie {
    @GraphProperty(index = true)
    String title;
    int year;

    @RelatedTo(type="ACTS_IN",elementClass = Actor.class, direction = Direction.INCOMING)
    Set<Actor> actors;
    static final String TITLE_INDEX = "title";

    public Iterable<Actor> getActors() {
        return actors;
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", getTitle(), getYear());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
// END SNIPPET: MovieClass
