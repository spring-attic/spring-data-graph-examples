package org.neo4j.examples.imdb.util;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.helpers.Predicate;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.impl.traversal.TraversalDescriptionImpl;

import java.util.*;

public class SimplePathFinder implements PathFinder {
    private static final int MAXIMUM_DEPTH = 5;

    public List<Node> shortestPath(final Node startNode, final Node endNode, final RelationshipType relType) {
        return findPath(startNode, endNode, relType);
    }

    private List<Node> findPath(final Node startNode, final Node endNode, final RelationshipType relType) {
        final Map<Node, Node> forwardTraversedNodes = new HashMap<Node, Node>();
        final Map<Node, Node> backwardTraversedNodes = new HashMap<Node, Node>();
        final PathReturnEval forwardReturnEvaluator = new PathReturnEval(forwardTraversedNodes, backwardTraversedNodes);
        final PathReturnEval backwardReturnEvaluator = new PathReturnEval(backwardTraversedNodes, forwardTraversedNodes);
        Iterator<Node> forwardIterator = traversePath(startNode, relType, forwardReturnEvaluator);
        Iterator<Node> backwardIterator = traversePath(endNode, relType, backwardReturnEvaluator);

        while (forwardIterator.hasNext() || backwardIterator.hasNext()) {
            if (forwardIterator.hasNext()) {
                forwardIterator.next();
            }
            List<Node> forwardPath = forwardReturnEvaluator.getMatch();
            if (forwardPath != null) {
                Collections.reverse(forwardPath);
                return forwardPath;
            }
            if (backwardIterator.hasNext()) {
                backwardIterator.next();
            }
            List<Node> backwardPath = backwardReturnEvaluator.getMatch();
            if (backwardPath != null) {
                return backwardPath;
            }
        }
        return Collections.emptyList();
    }

    private Iterator<Node> traversePath(Node startNode, RelationshipType relType, PathReturnEval returnEval) {
        TraversalDescription traversalDescription = new TraversalDescriptionImpl()
                .order(Traversal.postorderBreadthFirst())
                .prune(Traversal.pruneAfterDepth(MAXIMUM_DEPTH))
                .filter(returnEval)
                .expand(Traversal.expanderForTypes(relType, Direction.BOTH));
        final Traverser traverser = traversalDescription
                .traverse(startNode);
        return traverser.nodes().iterator();
    }

    private static class PathReturnEval implements Predicate<Path> {
        private final Map<Node, Node> myNodes;
        private final Map<Node, Node> otherNodes;
        private LinkedList<Node> match = null;

        public PathReturnEval(final Map<Node, Node> myNodes, final Map<Node, Node> otherNodes) {
            this.myNodes = myNodes;
            this.otherNodes = otherNodes;
        }

        public boolean accept(Path currentPos) {
            Node currentNode = currentPos.endNode();
            Node prevNode = currentPos.lastRelationship().getOtherNode(currentNode);
            if (!otherNodes.containsKey(currentNode)) {
                myNodes.put(currentNode, prevNode);
            } else {
                match = new LinkedList<Node>();
                match.add(currentNode);
                while (prevNode != null) {
                    match.add(prevNode);
                    prevNode = myNodes.get(prevNode);
                }
                Node otherNode = otherNodes.get(currentNode);
                while (otherNode != null) {
                    match.addFirst(otherNode);
                    otherNode = otherNodes.get(otherNode);
                }
            }
            return true;
        }

        protected List<Node> getMatch() {
            return match;
        }

    }
}