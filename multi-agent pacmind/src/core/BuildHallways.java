package core;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BuildHallways {
    private WeightedQuickUnionUF wqu;
    private TETile[][] world;
    private Random rand;
    private int width;
    private int height;
    private ArrayList<Node> rooms;
    private int numHallways;

    public BuildHallways(Random rand, TETile[][] world, WeightedQuickUnionUF wqu, ArrayList<Node> rooms) {
        this.world = world;
        this.wqu = wqu;
        this.rand = rand;
        this.width = world.length;
        this.height = world[0].length;
        this.rooms = rooms;
        numHallways = 0;
    }

    //create a brute force minimal spanning tree
    public TETile[][] connectAllRooms() {// could add in world
        ArrayList<Edge> edges = new ArrayList<>();

        for (int i = 0; i < rooms.size(); i++) {
            for (int j = i+1; j < rooms.size(); j++) {
                Node r1 = rooms.get(i);
                Node r2 = rooms.get(j);
                edges.add(new Edge(r1, r2));
            }
        }

        Collections.sort(edges);

        for (Edge edge : edges) {
            if (!wqu.connected(edge.r1.id, edge.r2.id)) {
                addHallway(edge.r1, edge.r2);
                wqu.union(edge.r1.id, edge.r2.id);
            }
        }

        return world;
    }

    public void addHallway(Node r1, Node r2) { //connect room 1 and room 2 with hallway
        int[] start = getClosestWall(r1, r2);
        int[] end = getClosestWall(r2, r1);
        int startX = start[0], startY = start[1];
        int endX = end[0], endY = end[1];

        drawLShapeHallways(startX, startY, endX, endY, r2);
        numHallways++;
    }

    public void drawLShapeHallways(int startX, int startY, int endX, int endY, Node target) {
        int currX = startX;
        int currY = startY;

        boolean horizontalFirst = rand.nextBoolean();
        world[startX][startY] = Tileset.FLOOR;
        addWalls(startX, startY, horizontalFirst);
        //randomize path shape

        if (horizontalFirst) {
            currX = drawLine(currX, currY, endX, true, target);
            addWalls(currX, currY, false);// move horizontally
            drawLine(currX, currY, endY, false,  target); // then move vertically
        } else {
            currY = drawLine(currX, currY, endY, false, target);
            addWalls(currX, currY, true);// move vertically
            drawLine(currX, currY, endX, true, target); // then horizontally
        }
    }

    private int drawLine(int x, int y, int target, boolean horizontal, Node targetRoom) {

        while ((horizontal && x != target) || (!horizontal && y != target)) {
            int originalX = x;
            int originalY = y;

            if (horizontal) {
                x += (target > x) ? 1 : -1;
            } else {
                y += (target > y) ? 1 : -1;
            }

            //Check bounds
            if (x < 0 || x >= width || y < 0 || y >= height) {
                break;  // Exit loop if out of bounds
            }
            //Mark as floor if it was nothing or wall
            if (world[x][y] == Tileset.NOTHING || world[x][y] == Tileset.WALL) {
                world[x][y] = Tileset.FLOOR;
                addWalls(x, y, horizontal);
            }

            if (x == originalX && y == originalY) {
                break;
            }
        }

        world[x][y] = Tileset.FLOOR;
        addWalls(x, y, horizontal);

        if (horizontal) {
            return x;
        } else {
            return y;
        }
    }

    public void addWalls (int x, int y, boolean horizontal) {
        int[][] wallOffset;

        if (horizontal) {
            wallOffset = new int[][] {{0, 1}, {0, -1}}; // above and below
        } else {
            wallOffset = new int[][] {{1, 0}, {-1, 0}}; // left and right
        }

        for (int[] adjust: wallOffset) {
            int nx = x + adjust[0];
            int ny = y + adjust[1];
            if (nx >= 0 && nx < width && ny >= 0 && ny < height && world[nx][ny] == Tileset.NOTHING) {
                world[nx][ny] = Tileset.WALL;
            }
        }
    }

    public int[] getClosestWall(Node from, Node to) {

        int fromCenterX = from.topLeftCoord[0] + from.width / 2;
        int fromCenterY = from.topLeftCoord[1] - from.height / 2;
        int toCenterX = to.topLeftCoord[0] + to.width / 2;
        int toCenterY = to.topLeftCoord[1] - to.height / 2;

        int dx = toCenterX - fromCenterX;
        int dy = toCenterY - fromCenterY;

        int x, y;
        int top = from.topLeftCoord[1] - 1;
        int bottom = from.topLeftCoord[1] - from.height;
        int left = from.topLeftCoord[0];
        int right = from.topLeftCoord[0] + from.width - 1;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx < 0) {
                x = left;
                y = RandomUtils.uniform(rand, bottom + 1, top - 1);
            } else {
                x = right;
                y = RandomUtils.uniform(rand, bottom + 1, top - 1);
            }
        } else {
            if (dy < 0) {
                x = RandomUtils.uniform(rand, left + 1, right - 1);
                y = bottom;
            } else {
                x = RandomUtils.uniform(rand, left + 1, right - 1);
                y = top;
            }
        }
        return new int[]{x, y};
    }
}

