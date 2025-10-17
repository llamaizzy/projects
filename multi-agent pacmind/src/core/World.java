package core;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.Random;

public class World {
    private TETile[][] world;
    private WeightedQuickUnionUF wqu;
    private Long SEED;
    private static Random rand;
    int numRooms;
    private ArrayList<Node> roomsList;


    public World(TETile[][] world, Long SEED) { //initialize world
        rand = new Random(SEED);
        this.world = world;
        numRooms = RandomUtils.uniform(rand, 30, 45);
        this.SEED = SEED;
        wqu = new WeightedQuickUnionUF(numRooms * numRooms);

    }

    public WeightedQuickUnionUF getWqu() {
        return wqu;
    }

    /*
    Quits the entire game and closes the application. Useful for Main Menu and Interactivity.
     */
    public void quit() {
        System.exit(0);
    }

    public void buildRooms() {
        BuildRooms rooms = new BuildRooms(numRooms, rand, world, wqu);
        world = rooms.drawRooms(world); // create method that returns the modified world;

        roomsList = rooms.getAllRoomsList();
        int roomCount = roomsList.size();
        //update ids
        for (int i = 0; i < roomCount; i++) {
            Node currRoom = roomsList.get(i);
            currRoom.id = i;
        }
    }

    public void buildHallways() {
        BuildHallways halls = new BuildHallways(rand, world, wqu, roomsList);
        world = halls.connectAllRooms();

        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                int[] tiles = countTile(i, j);
                if (world[i][j].equals(Tileset.FLOOR) && tiles[0] == 2 && tiles[1] == 1 && tiles[2] == 1) {
                    world[i][j] = Tileset.WALL;
                }
            }
        }
    }
    public int[] countTile(int x, int y) {
        int wall = 0;
        int floor = 0;
        int nothing = 0;
        int[][] wallOffset = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir: wallOffset) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 0 && nx < world.length && ny >= 0 && ny < world[0].length) {
                TETile curr = world[nx][ny];
                if (curr.equals(Tileset.WALL)) {
                    wall++;
                } else if (curr.equals(Tileset.FLOOR)) {
                    floor++;
                } else if (curr.equals(Tileset.NOTHING)) {
                    nothing++;
                }
            }
        }
        return new int[] {wall, floor, nothing};
    }

}
