package me.battleblast.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import me.battleblast.BattleBlast;


public class AGraph implements IndexedGraph<Node> {
    private static final int MAP_WIDTH = BattleBlast.MAP_WIDTH;
    private static final int MAP_HEIGHT = BattleBlast.MAP_HEIGHT;

    private Node[][] map;

    public AGraph(Array<Vector2> walls) {
        initNodes();
        setupWalls(walls);
        setupNodeConnections();
    }

    @Override
    public int getIndex(Node node) {
        return node.x * MAP_HEIGHT + node.y;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node node) {
        return node.connections;
    }

    @Override
    public int getNodeCount() {
        return MAP_WIDTH * MAP_HEIGHT;
    }

    public Node getNodeAt(Vector2 position) {
        return map[toInt(position.x)][toInt(position.y)];
    }

    private void setupNodeConnections() {
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                Node node = map[x][y];
                // right
                if (withinMap(x + 1, y) && tankFitsIn(x + 1, y)) {
                    node.connections.add(new DefaultConnection<Node>(node, map[x+1][y]));
                }
                // top
                if (withinMap(x, y + 1) && tankFitsIn(x, y + 1)) {
                    node.connections.add(new DefaultConnection<Node>(node, map[x][y + 1]));
                }
                // left
                if (withinMap(x - 1, y) && tankFitsIn(x - 1, y)) {
                    node.connections.add(new DefaultConnection<Node>(node, map[x - 1][y]));
                }
                // bottom
                if (withinMap(x, y - 1) && tankFitsIn(x, y - 1)) {
                    node.connections.add(new DefaultConnection<Node>(node, map[x][y - 1]));
                }
            }
        }
    }

    private boolean withinMap(int x, int y) {
        // a tank takes 2 tiles in each direction. this is the reason 
        // we add "-1" to the MAP_WIDTH and MAP_HEIGHT here.
        return x < MAP_WIDTH - 1 && y < MAP_HEIGHT - 1 && x >= 0 && y >= 0; 
    }

    private boolean tankFitsIn(int x, int y) {
        // a tank image takes 2 tiles ( @ ) in each direction; if any of these
        // tiles is a wall ( & ), the tank doesn't fit in the position
        // @@
        // @@   <--- fits in this position
        //
        // @&
        // @@   <--- doesn't fit, top-right tile is a wall.
        //
        // we use the bottom-left tile as the tank's position
        return !map[x][y].isWall && !map[x+1][y].isWall && !map[x][y + 1].isWall && !map[x+1][y+1].isWall;
    }

    private void initNodes() {
        map = new Node[MAP_WIDTH][MAP_HEIGHT];
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                map[x][y] = new Node(x, y);
            }
        }
    }

    private void setupWalls(Array<Vector2> walls) {
        for (Vector2 wall: walls) {
            map[toInt(wall.x)][toInt(wall.y)].isWall = true;
        }
    }

    private int toInt(float f) {
        return MathUtils.floor(f);
    }
}
