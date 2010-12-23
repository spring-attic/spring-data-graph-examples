package org.neo4j.examples.imdb.domain;

import org.springframework.datastore.graph.annotation.EndNode;
import org.springframework.datastore.graph.annotation.RelationshipEntity;
import org.springframework.datastore.graph.annotation.StartNode;

@RelationshipEntity
public class Role {
    String role;
    @StartNode
    Actor actor;
    @EndNode
    Movie movie;

    @Override

    public String toString() {
        return String.format("%s-[%s]->%s", this.getActor(), role, this.getMovie());
    }

    public Actor getActor() {
        return actor;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getName() {
        return role;
    }

    public void setName(String name) {
        this.role = name;
    }
}
