package core;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;

public class BuildRooms {
    Random rand;
    private TETile[][] world;
    private WeightedQuickUnionUF wqu;
    private int worldHeight;
    private int worldWidth;
    private int roomQty;
    private ArrayList<Node> allRooms;

    public BuildRooms(int roomQty, Random rand, TETile[][] world, WeightedQuickUnionUF wqu) {
        this.rand = rand; //this is the random object generated from the seed in World.java
        this.world = world;
        this.wqu = wqu;
        worldWidth = world.length;
        worldHeight = world[0].length;
        this.roomQty = roomQty; //set bound to 15 rooms
        allRooms = new ArrayList<>();
    }



    public TETile[][] drawRooms(TETile[][] world) {
        int count = 0;
        while (count < roomQty) {
            count += drawRandomRoomHelper(world);
        }
        return world;
    }

    public Node buildOneRoom(){
        int id = RandomUtils.uniform(rand, 1);
        int height = RandomUtils.uniform(rand, 5, 11);  //hardcoding width and height bound
        int width = RandomUtils.uniform(rand, 5, 11);
        int xCoord = RandomUtils.uniform(rand, 0, worldWidth - width - 1);
        int yCoord = RandomUtils.uniform(rand, height - 1, worldHeight - height);
        int[] topLeftCoord = new int[2];
        topLeftCoord[0] = xCoord;
        topLeftCoord[1] = yCoord;
        return new Node(id, height, width, topLeftCoord);
    }

    public ArrayList<Node> getAllRoomsList() {
        return allRooms;
    }


    public void drawRoom(Node currRoom){
        for (int x = currRoom.topLeftCoord[0]; x < currRoom.width + currRoom.topLeftCoord[0]; x++) {
            for (int y = currRoom.topLeftCoord[1] - currRoom.height; y < currRoom.topLeftCoord[1]; y++) {
                if (x < worldWidth && y < worldHeight) {
                    world[x][y] = Tileset.WALL;
                }
                if (x < worldWidth - 1 && y < worldHeight - 1) {
                    if (x != currRoom.topLeftCoord[0] && y != currRoom.topLeftCoord[1] - currRoom.height && x != (currRoom.topLeftCoord[0] + currRoom.width - 1) && y != currRoom.topLeftCoord[1] - 1) {
                        world[x][y] = Tileset.FLOOR;
                    }
                }
            }
        }
    }


    public int drawRandomRoomHelper(TETile[][] world) {
        Node newRoom = buildOneRoom();
        int xLoc = newRoom.topLeftCoord[0];
        int yLoc = newRoom.topLeftCoord[1];
        int height = newRoom.height;
        int width = newRoom.width;
        int count = 0;
        boolean safe = true;
        if ((width + xLoc) >= worldWidth || (yLoc - height) < 0) {
            safe = false;
        }
        else {
            for (int i = xLoc; i < width + xLoc; i++) {
                for (int j = yLoc - height; j < yLoc; j++) {
                    if (world[i][j].equals(Tileset.WALL) || world[i][j].equals(Tileset.FLOOR)) { //hit a boundary of another room
                        safe = false;
                    }
                }
            }
        }
        if (safe){
            drawRoom(newRoom);
            allRooms.add(newRoom);
            count = 1;
        }
        return count;
    }
}
