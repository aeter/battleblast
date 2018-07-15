package me.battleblast.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class PathFinding {
    private PathFinder<Node> pathfinder; 
    private Heuristic<Node> heuristic;
    public GraphPath<Connection<Node>> path;
    private AGraph graph;

    public PathFinding(Array<Vector2> walls) {
        setupHeuristic();
        this.graph = new AGraph(walls);
        this.pathfinder = new IndexedAStarPathFinder<Node>(graph);
        this.path = new DefaultGraphPath<Connection<Node>>();
    }

    // TODO - see if we should keep this unused method
    public Node getNextNode(Vector2 source, Vector2 target) {
        Node sourceNode = graph.getNodeAt(source);
        Node targetNode = graph.getNodeAt(target);
        path.clear();
        pathfinder.searchConnectionPath(sourceNode, targetNode, heuristic, path);
        return path.getCount() == 0 ? null : path.get(0).getToNode();
    }

    public Array<Vector2> getFullPath(Vector2 source, Vector2 target) {
        Node sourceNode = graph.getNodeAt(source);
        Node targetNode = graph.getNodeAt(target);
        path.clear();
        Array<Vector2> fullPath = new Array<Vector2>();
        pathfinder.searchConnectionPath(sourceNode, targetNode, heuristic, path);
        for (Connection<Node> c: path) {
            Node node = c.getToNode();
            fullPath.add(new Vector2(node.x, node.y));
        }
        return fullPath;
    }

    private void setupHeuristic() {
        this.heuristic = new Heuristic<Node>() {
            @Override
            public float estimate(Node start, Node end) {
                // Manhattan distance
                return Math.abs(end.x - start.x) + Math.abs(end.y - start.y);
            }
        };
    }
}
