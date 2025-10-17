package core;

import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class PlayWorld {
    private static int[] avatar;
    private static int[] coin;
    private static final int WIDTH = 90; // for world
    private static final int HEIGHT = 50;
    private static Random rand;
    private static TETile[][] worldArray;
    private static int num_coins;
    private static long seed;
    private static ArrayList seeds;
    private static TETile AVATAR1 = null;
    private static String avatar_name;


    public PlayWorld(long SEED, TERenderer ter, ArrayList seeds, String avatar_name, int[] avatar) {
        rand = new Random(SEED);
        num_coins = 0;
        seed = SEED;
        this.seeds = seeds;
        this.avatar_name = avatar_name;
        Tileset.update_avatar(avatar_name);
        AVATAR1 = Tileset.AVATAR;
        this.avatar = avatar;

        //initialize new world
        TETile[][] worldArray = new TETile[WIDTH][HEIGHT - 5];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT - 5; y++) {
                worldArray[x][y] = Tileset.NOTHING;
            }
        }

        World world = new World(worldArray, SEED);
        world.buildRooms();
        world.buildHallways();

        ter.renderFrame(worldArray);

        //initialize avatar
        //set avatar to first valid floor location
        if (avatar[0] == 0 && avatar[1] == 0) {
            for (int i = 0; i < worldArray[0].length; i++) { //height
                for (int j = 0; j < worldArray.length; j++) { //width
                    if (worldArray[j][i].equals(Tileset.FLOOR)) {
                        avatar[0] = j;
                        avatar[1] = i;
                        break;
                    }
                }
            }
        }

        coin = new int[2];
        //randomly spawn in coin
        ArrayList coin_locs = new ArrayList<>();
        for (int i = 0; i < worldArray.length; i++) {
            for (int j = 0; j < worldArray[0].length; j++) {
                if (worldArray[i][j].equals(Tileset.FLOOR)) {
                    int[] loc = new int[2];
                    loc[0] = i;
                    loc[1] = j;
                    coin_locs.add(loc);
                }
            }
        }
        int coin_loc_actual = rand.nextInt(coin_locs.size());
        int[] coin_loc_array = (int[]) coin_locs.get(coin_loc_actual);
        coin[0] = coin_loc_array[0];
        coin[1] = coin_loc_array[1];

        worldArray[coin[0]][coin[1]] = Tileset.FLOWER;
        worldArray[avatar[0]][avatar[1]] = AVATAR1;
//        int temp = avatar[0];
//        avatar[0] = avatar[1];
//        avatar[1] = temp;


        StringBuilder keys = new StringBuilder();

        //initialize timer
        long startTime = System.currentTimeMillis();

        while (true) {

            long elapsedTime = System.currentTimeMillis() - startTime;
            long elapsedSeconds = elapsedTime / 1000;
            long secondsDisplay = 30 - elapsedSeconds % 60;
            long remainingSeconds = 30 - elapsedSeconds;

            while (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toLowerCase(c);

                keys.append(c);
                if (keys.length() >= 2) {
                    String lastTwo = keys.substring(keys.length() - 2);
                    if (lastTwo.equals(":q") || lastTwo.equals(":Q")) {
                        saveAndQuit(SEED, avatar);
                    }
                }

                switch (c) {
                    case 'w':
                        toggleUp(worldArray, avatar[0], avatar[1]);
                        break;
                    case 's':
                        toggleDown(worldArray, avatar[0], avatar[1]);
                        break;
                    case 'd':
                        toggleRight(worldArray, avatar[0], avatar[1]);
                        break;
                    case 'a':
                        toggleLeft(worldArray, avatar[0], avatar[1]);
                        break;
                    case '1':
                        new PlayWorld((Long.parseLong((String) seeds.getFirst())), ter, seeds, avatar_name, new int[2]);
                        break;
                    case '2':
                        new PlayWorld((Long.parseLong((String) seeds.get(1))), ter, seeds, avatar_name, new int[2]);
                        break;
                    case '3':
                        new PlayWorld((Long.parseLong((String) seeds.get(2))), ter, seeds, avatar_name, new int[2]);
                        break;
                    case '4':
                        new PlayWorld((Long.parseLong((String) seeds.get(3))), ter, seeds, avatar_name, new int[2]);
                        break;
                    case '5':
                        new PlayWorld((Long.parseLong((String) seeds.get(4))), ter, seeds, avatar_name, new int[2]);
                        break;
                    default:
                        break;
                }
            }

            int mouseX = (int) StdDraw.mouseX();
            int mouseY = (int) StdDraw.mouseY();
            String hud_text = "";
            String levels_text = "Press 1 for L1, 2 for L2, 3 for L3, 4 for L4, or 5 for L5. Toggle between" +
                    " at any time.";

            if (mouseX > WIDTH - 1 || mouseY > HEIGHT - 6) {
                // text = "nothing";
                //  break;
                hud_text = "";
            } else {
                TETile block = worldArray[mouseX][mouseY];

                if (block.equals(Tileset.NOTHING)) {
                    //System.out.println("over nothing");
                    hud_text = "nothing";
                } else if (block.equals(Tileset.FLOOR)) {
                    //System.out.println("over floor");
                    hud_text = "floor";
                } else if (block.equals(Tileset.WALL)) {
                    //System.out.println("over wall");
                    hud_text = "wall";
                } else if (block.equals(Tileset.AVATAR)) {
                    hud_text = "avatar";
                } else if (block.equals(Tileset.FLOWER)) {
                    hud_text = "coin";
                } else {
                    hud_text = "";
                }
            }
            StdDraw.clear(Color.BLACK);
            ter.drawTiles(worldArray);

            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setPenRadius(100.0);
            //write hud display to screen
            StdDraw.textLeft(6, 47, hud_text);
            //write level menu to screen
            StdDraw.textLeft(25, 47, levels_text);
           // StdDraw.pause(2);
            StdDraw.enableDoubleBuffering();
            String timer_text = "Time Remaining: " + String.valueOf(secondsDisplay);
            StdDraw.textLeft(72, 47, timer_text);
            StdDraw.show();

            //System.out.println(num_coins);

            if (num_coins == 1) {
                gameOver(ter, true);
            } else if (remainingSeconds <= 0) {
                gameOver(ter, false);
            }
        }
    }
    public static void toggleUp(TETile[][] worldArray, int x, int y) {

        if (!worldArray[x][y + 1].equals(Tileset.WALL)) {
            if (worldArray[x][y + 1].equals(Tileset.FLOWER)) {
                num_coins++;
            }
            worldArray[x][y + 1] = AVATAR1;
            worldArray[x][y] = Tileset.FLOOR;
            avatar[1]++;
        }
    }

    public static void toggleRight(TETile[][] worldArray, int x, int y) {

        if (!worldArray[x + 1][y].equals(Tileset.WALL)) {
            if (worldArray[x + 1][y].equals(Tileset.FLOWER)) {
                num_coins++;
            }
            worldArray[x + 1][y] = AVATAR1;
            worldArray[x][y] = Tileset.FLOOR;
            avatar[0]++;
        }
    }

    public static void toggleLeft(TETile[][] worldArray, int x, int y) {

        if (!worldArray[x - 1][y].equals(Tileset.WALL)) {
            if (worldArray[x - 1][y].equals(Tileset.FLOWER)) {
                num_coins++;
            }
            worldArray[x - 1][y] = AVATAR1;
            worldArray[x][y] = Tileset.FLOOR;
            avatar[0]--;

        }
    }

    public static void toggleDown(TETile[][] worldArray, int x, int y) {

        if (!worldArray[x][y - 1].equals(Tileset.WALL)) {
            if (worldArray[x][y - 1].equals(Tileset.FLOWER)) {
                num_coins++;
            }
            worldArray[x][y] = Tileset.FLOOR;
            worldArray[x][y - 1] = AVATAR1;
            avatar[1]--;
        }
    }

    public static void gameOver(TERenderer ter, Boolean win) {
        while (true) {
            if (win) {
                StdDraw.clear(StdDraw.BLACK);
                StdDraw.textLeft(42, 45, "CS61B: BYOW");
                StdDraw.textLeft(39, 30, "Good job: Level Complete!");
                StdDraw.textLeft(33, 25, "(R) Restart");
                StdDraw.textLeft(42, 25, "(N) Next Level");
                StdDraw.textLeft(53, 25, "(Q) Quit");
                StdDraw.show();

                while (StdDraw.hasNextKeyTyped()) {
                    char n = StdDraw.nextKeyTyped();
                    switch (n) {
                        case 'R', 'r':
                            new PlayWorld(seed, ter, seeds, avatar_name, new int[2]);
                            break;
                        case 'N', 'n':
                            new PlayWorld(seed * 2, ter, seeds, avatar_name, new int[2]);
                            break;
                        case 'Q', 'q':
                            System.exit(0);
                            return;
                    }
                }

            } else {
                StdDraw.clear(StdDraw.BLACK);
                StdDraw.textLeft(42, 45, "CS61B: BYOW");
                StdDraw.textLeft(42, 30, "Aw No! Game Over!");
                StdDraw.textLeft(33, 25, "(R) Restart");
                StdDraw.textLeft(53, 25, "(Q) Quit");
                StdDraw.show();

                while (StdDraw.hasNextKeyTyped()) {
                    char n = StdDraw.nextKeyTyped();
                    switch (n) {
                        case 'R', 'r':
                            new PlayWorld(seed, ter, seeds, avatar_name, new int[2]);
                            break;
                        case 'Q', 'q':
                            System.exit(0);
                            return;
                    }
                }
            }
        }
    }

    public static void saveAndQuit(long seed, int[] avatar) {
        String filename = "seed.txt";
        Out out = new Out(filename);
        out.println(seed + "*" + Arrays.toString(avatar) + "*" + avatar_name);
        out.close();
        System.exit(0);
    }
}
