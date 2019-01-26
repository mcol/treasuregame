package mcol.treasuregame.utils;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.BinaryHeap.Node;

public class PathFinder {

    public static class PathNode extends Node {

        /** Tile coordinates. */
        public final Vector3 tile;

        /** Parent node. */
        public PathNode parent;

        /** Heuristic cost to the target. */
        public final float hCost;

        /** Cost up to this node. */
        public float gCost;

        /** Estimated cost to the target. */
        public float fCost;

        /** Iteration number. */
        public final int iter;

        /** Whether the node has been examined. */
        public boolean closed;

        /** Constructor. */
        public PathNode(Vector3 tile, PathNode parent, float hCost, int iter) {
            super(0);
            this.tile = tile;
            this.parent = parent;
            this.hCost = hCost;
            this.iter = iter;
            this.closed = false;
            gCost = (parent == null) ? 0 : parent.gCost + heuristic(tile, parent.tile);
            fCost = gCost + hCost;
        }

        /** Compares with another tile. */
        public boolean equals(Vector3 target) {
            return tile.equals(target);
        }
    }

    /** Nodes to be examined. */
    private static BinaryHeap<PathNode> bhopen = new BinaryHeap<>(200, false);

    /** Correction factor for diagonal movements. */
    private static final float oct = (float) Math.sqrt(2) - 2;

    /** Octile distance heuristic. */
    private static float heuristic(Vector3 a, Vector3 b) {
        float dx = Math.abs(a.x - b.x);
        float dy = Math.abs(a.y - b.y);
        return dx + dy + oct * Math.min(dx, dy);
    }

    /** Finds a path in the map using A*. */
    public static List<Vector3> findPath(TiledMapTileLayer obstacles,
                                         Vector3 start, Vector3 target) {
        int iter = 0;
        int ttx = (int) target.x, tty = (int) target.y;
        int width = obstacles.getWidth();
        int height = obstacles.getHeight();
        PathNode current = new PathNode(start, null, heuristic(start, target), iter);
        PathNode currentBestNode = current;
        float currentBestCost = 10000000f;

        PathNode[] mapNodes = new PathNode[width * height];
        mapNodes[(int) (start.x + start.y * width)] = current;

        bhopen.clear();
        bhopen.add(current, current.fCost);

        while (!bhopen.isEmpty()) {
            iter++;
            current = bhopen.pop();
            current.closed = true;

            // create the path if the target has been reached
            if (current.equals(target))
                return makePath(current);

            // check all neighbours of this node
            for (int i = 0; i < 9; i++) {

                // skip the current node
                if (i == 4)
                    continue;

                int tx = (int) current.tile.x + i / 3 - 1;
                int ty = (int) current.tile.y + i % 3 - 1;

                // don't go outside of the map
                if (tx < 0 || tx >= width || ty < 0 || ty >= height)
                    continue;

                // avoid nodes corresponding to obstacles
                if (obstacles.getCell(tx, ty) != null) {
                    if (tx == ttx && ty == tty)
                        return makePath(current);
                    continue;
                }

                Vector3 adj = new Vector3(tx, ty, 0);
                PathNode adjNode = new PathNode(adj, current, heuristic(adj, target), iter);
                int index = tx + ty * width;
                PathNode node = mapNodes[index];

                if (node != null) {
                    if (!node.closed && adjNode.gCost < node.gCost) {
                        bhopen.setValue(node, adjNode.fCost);
                        node.parent = current;
                        node.fCost = adjNode.fCost;
                        node.gCost = adjNode.gCost;
                    }
                }

                else {
                    mapNodes[index] = adjNode;
                    bhopen.add(adjNode, adjNode.fCost);
                    if (adjNode.hCost < currentBestCost) {
                        currentBestCost = adjNode.hCost;
                        currentBestNode = adjNode;
                    }
                }
            }
        }

        return makePath(currentBestNode);
    }

    /** Creates the path from the target to the current node. */
    private static List<Vector3> makePath(PathNode node) {
        List<Vector3> path = new ArrayList<>();
        while (node.parent != null) {
            path.add(node.tile);
            node = node.parent;
        }
        return path;
    }
}
