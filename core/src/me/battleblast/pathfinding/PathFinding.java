package me.battleblast.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;


public class PathFinding {
    private Node[][] map;
    private PathFinder<Node> pathfinder; 
    private Heuristic<Node> heuristic;
    private GraphPath<Connection<Node>> path;

    public void PathFinding() {
        populateMap();
        setupHeuristic();
        this.pathfinder = new IndexedAStarPathFinder<Node>(makeGraph());
        this.path = new DefaultGraphPath<Connection<Node>>();
    }

    private BetterGraph makeGraph() {
        return new BetterGraph();
        // TODO - setup the graph with info from the tiled map
        // TODO - use the impassable nodes from the tiled map layers...
        // TODO - whenever a node is boomed when the tank shoots, update the map
        // too for the pathfinding
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

    private void populateMap() {
        // the map is 20 quadrangle tiles * 32 pixels each.
        // TODO -calculate this from the tiled map instead of hardcoding
        int tileWidth = 32;
        int mapWidth = 20;
        int mapHeight = 20;
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                map[x][y] = new Node(x * tileWidth, y * tileWidth);
            }
        }
    }

    private static class BetterGraph implements IndexedGraph<Node> {
        @Override
        public int getIndex(Node node) {
            return node.index;
        }

        @Override
        public Array<Connection<Node>> getConnections(Node node) {
            return node.connections;
        }

        @Override
        public int getNodeCount() {
            int mapWidth = 20;
            int mapHeight = 20;
            return mapWidth * mapHeight;
        }
    }
}
