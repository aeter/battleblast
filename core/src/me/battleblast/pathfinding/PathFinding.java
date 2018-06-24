package me.battleblast.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.PathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class PathFinding {
    private Node[][] map;
    private PathFinder<Node> pathfinder; 
    private Heuristic<Node> heuristic;
    public GraphPath<Connection<Node>> path;

    public PathFinding() {
        setupHeuristic();
        initNodes();
        //Gdx.app.log("asd", "asd");
        //Gdx.app.log("x", String.format("%d", map[0][0].index));
        setupNodeConnections();
        this.pathfinder = new IndexedAStarPathFinder<Node>(new BetterGraph());
        this.path = new DefaultGraphPath<Connection<Node>>();
    }

    public Node getNextNode(Vector2 source, Vector2 target) {
        Node sourceNode = map[MathUtils.floor(source.x)][MathUtils.floor(source.y)];
        Node targetNode = map[MathUtils.floor(target.x)][MathUtils.floor(target.y)];
        path.clear();
        pathfinder.searchConnectionPath(sourceNode, targetNode, heuristic, path);
        return path.getCount() == 0 ? null : path.get(0).getToNode();
    }

    

    private void setupNodeConnections() {
        // TODO - setup the graph with info from the tiled map
        // TODO - use the impassable nodes from the tiled map layers...
        // TODO - whenever a node is boomed when the tank shoots, update the map
        // too for the pathfinding (i.e. change in the map)
        for (int x = 0; x < 20; x++) { // TODO -don't hardcode
            for (int y = 0; y < 20; y++) { // TODO - dont hardcode
                Node node = map[x][y];
                if (x < 19)
                    node.connections.add(new DefaultConnection<Node>(node, map[x+1][y])); // right
                if (y < 19)
                    node.connections.add(new DefaultConnection<Node>(node, map[x][y+1])); // top
                if (x > 0)
                    node.connections.add(new DefaultConnection<Node>(node, map[x-1][y])); // left
                if (y > 0)
                    node.connections.add(new DefaultConnection<Node>(node, map[x][y-1])); // bottom
            }
        }
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

    private void initNodes() {
        // the map is 20 quadrangle tiles * 32 pixels each.
        // TODO -calculate this from the tiled map instead of hardcoding
        int mapWidth = 20;
        int mapHeight = 20;
        map = new Node[mapWidth][mapHeight];
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                map[x][y] = new Node(x, y);
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
