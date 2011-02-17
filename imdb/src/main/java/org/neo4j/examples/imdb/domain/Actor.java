package org.neo4j.examples.imdb.domain;

import org.springframework.data.annotation.Indexed;
import org.springframework.data.graph.annotation.NodeEntity;
import org.springframework.data.graph.annotation.RelatedTo;

import java.util.Set;

// START SNIPPET: ActorClass
@NodeEntity
public class Actor {
    @Indexed
    private String name;

    @RelatedTo(type="ACTS_IN",elementClass = Movie.class)
    private Set<Movie> movies;
    static final String NAME_INDEX = "name";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Iterable<Movie> getMovies() {
        return movies;
    }

    public Role getRole(final Movie inMovie) {
        return (Role)getRelationshipTo((Movie)inMovie, Role.class, RelTypes.ACTS_IN.name());
    }

    @Override
    public String toString() {
        return "Actor '" + this.getName() + "'";
    }
}
// END SNIPPET: ActorClass
