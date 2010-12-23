package org.neo4j.examples.imdb.domain;

import org.neo4j.examples.imdb.util.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.index.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.datastore.graph.api.NodeBacked;
import org.springframework.persistence.support.EntityInstantiator;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

class ImdbServiceImpl implements ImdbService {
    @Autowired
    private GraphDatabaseService graphDbService;
    @Autowired
    private EntityInstantiator<NodeBacked,Node> graphEntityInstantiator;
    @Autowired
    private IndexService indexService;
    @Autowired
    private PathFinder pathFinder;
    @Autowired
    private ImdbSearchEngine searchEngine;

    public Actor createActor(final String name) {
        final Actor actor = new Actor();
        actor.setName(name);
        searchEngine.indexActor(actor);
        return actor;
    }

    public Movie createMovie(final String title, final int year) {
        final Movie movie = new Movie();
        movie.setTitle(title);
        movie.setYear(year);
        searchEngine.indexMovie(movie);
        return movie;
    }

    public Role createRole(final Actor actor, final Movie movie,
                           final String roleName) {
        if (actor == null) throw new IllegalArgumentException("Null actor");
        if (movie == null) throw new IllegalArgumentException("Null movie");
        Role role = (Role) actor.relateTo(movie, Role.class, RelTypes.ACTS_IN.name());
        role.setName(roleName);
        return role;
    }

    public Actor getActor(final String name) {
        Node actorNode = indexService.getSingleNode(Actor.NAME_INDEX, name);
        if (actorNode == null) {
            actorNode = searchEngine.searchActor(name);
        }
        if (actorNode != null) {
            return graphEntityInstantiator.createEntityFromState(actorNode, Actor.class);
        }
        return null;
    }

    public Movie getMovie(final String title) {
        Node movieNode = getExactMovieNode(title);
        if (movieNode == null) {
            movieNode = searchEngine.searchMovie(title);
        }
        if (movieNode != null) {
            return graphEntityInstantiator.createEntityFromState(movieNode, Movie.class);
        }
        return null;
    }

    public Movie getExactMovie(final String title) {
        Node movieNode = getExactMovieNode(title);
        if (movieNode != null) {
            return graphEntityInstantiator.createEntityFromState(movieNode, Movie.class);
        }
        return null;
    }

    private Node getExactMovieNode(final String title) {
        Node movieNode = null;
        try {
            movieNode = indexService.getSingleNode(Movie.TITLE_INDEX, title);
        } catch (RuntimeException e) {
            System.out.println("Duplicate index for movie title: " + title);
            Iterator<Node> movieNodes = indexService.getNodes(Movie.TITLE_INDEX,
                    title).iterator();
            if (movieNodes.hasNext()) {
                movieNode = movieNodes.next();
            }
        }
        return movieNode;
    }

    @Transactional
    public void setupReferenceRelationship() {
        Node baconNode = indexService.getSingleNode("name", "Bacon, Kevin");
        if (baconNode == null) {
            throw new NoSuchElementException(
                    "Unable to find Kevin Bacon actor");
        }
        Node referenceNode = graphDbService.getReferenceNode();
        referenceNode.createRelationshipTo(baconNode, RelTypes.IMDB);
    }

    public List<?> getBaconPath(final Actor actor) {
        final Node baconNode;
        if (actor == null) {
            throw new IllegalArgumentException("Null actor");
        }
        try {
            baconNode = graphDbService.getReferenceNode().getSingleRelationship(
                    RelTypes.IMDB, Direction.OUTGOING).getEndNode();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(
                    "Unable to find Kevin Bacon actor");
        }
        final Node actorNode = ((Actor) actor).getUnderlyingState();
        final List<Node> list = pathFinder.shortestPath(actorNode, baconNode,
                RelTypes.ACTS_IN);
        return convertNodesToActorsAndMovies(list);
    }

    private List<?> convertNodesToActorsAndMovies(final List<Node> list) {
        final List<Object> actorAndMovieList = new LinkedList<Object>();
        int mod = 0;
        for (Node node : list) {
            if (mod++ % 2 == 0) {
                actorAndMovieList.add(graphEntityInstantiator.createEntityFromState(node, Actor.class));
            } else {
                actorAndMovieList.add(graphEntityInstantiator.createEntityFromState(node, Movie.class));
            }
        }
        return actorAndMovieList;
    }
}