package org.neo4j.examples.imdb.domain;

import org.springframework.data.annotation.Indexed;
import org.springframework.data.graph.annotation.NodeEntity;
import org.springframework.data.graph.annotation.RelatedTo;
import org.springframework.data.graph.core.Direction;

import java.util.Set;

// START SNIPPET: MovieClass
@NodeEntity
public class Movie {
    @Indexed
    String title;
    int year;

    @RelatedTo(type = "ACTS_IN", elementClass = Actor.class, direction = Direction.INCOMING)
    Set<Actor> actors;

    public Set<Actor> getActors() {
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

    int getActorsCount() {
        return getActors().size();
    }
}
// END SNIPPET: MovieClass
