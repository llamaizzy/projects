package core;

import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class Main {
    private static final int WIDTH = 90; // for world
    private static final int HEIGHT = 50;
    private static ArrayList seeds;
    private static TETile AVATAR1;
    private static String avatar_name;

    public static void load(TERenderer ter) {
        String filename = "seed.txt";
        File file = new File(filename);
        In in = new In(file);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] savedLine = line.split("\\*");
            long savedSeed = Long.parseLong(savedLine[0]);
            int[] avatar = parseArray(savedLine[1]);
            avatar_name = savedLine[2];

            seeds = new ArrayList<>();
            seeds.add(savedSeed);
            seeds.add(savedSeed + "1");
            seeds.add(savedSeed + "2");
            seeds.add(savedSeed + "3");
            seeds.add(savedSeed + "4");

            new PlayWorld(savedSeed, ter, seeds, avatar_name, avatar);
        }
    }

    public static String avatarMenu() {
        String filename = "avatar.txt";
        StringBuilder input = new StringBuilder();

        while (true) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.textLeft(38, 45, "Enter avatar # followed by S");
            StdDraw.picture(45, 25, "./images/all_avatars.png");
            StdDraw.textLeft(44, 5, input.toString());
            StdDraw.show();
            while (StdDraw.hasNextKeyTyped()) {
                char n = StdDraw.nextKeyTyped();

                switch (n) {
                    case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                        input.append(n);
                        break;
                    case 'S', 's':
                        Out out = new Out(filename);
                        String selection = input.toString();
                        out.println(selection);
                        out.close();
                        return "./images/avatar_" + selection + ".png";
                }
            }

        }
    }

    public static void promptSave(TERenderer ter) {
        String filename = "seed.txt";
        StringBuilder input = new StringBuilder();

        while (true) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textLeft(42, 45, "CS61B: BYOW");
            StdDraw.textLeft(40, 30, "Enter seed followed by S");
            StdDraw.setPenColor(StdDraw.YELLOW);
            StdDraw.text(45, 20, input.toString());
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.textLeft(35, 15, "Collect the coin before the timer runs out!");
            StdDraw.show();

            while (StdDraw.hasNextKeyTyped()) {
                char n = StdDraw.nextKeyTyped();

                switch (n) {
                    case '0', '1', '2','3','4','5','6','7','8','9':
                        input.append(n);
                        break;
                    case 'S','s':
                        Out out = new Out(filename);
                        String seed1  = input.toString();
                        String seed2 = seed1 + "1";
                        String seed3 = seed1 + "2";
                        String seed4 = seed1 + "3";
                        String seed5 = seed1 + "4";
                        out.println(seed1);
                        out.println(seed2);
                        out.println(seed3);
                        out.println(seed4);
                        out.println(seed5);
                        seeds.add(seed1);
                        seeds.add(seed2);
                        seeds.add(seed3);
                        seeds.add(seed4);
                        seeds.add(seed5);

                        out.close();
                        new PlayWorld(Long.parseLong(seed1), ter, seeds, avatar_name, new int[2]);
                        return;
                }
            }
        }
    }

    public static int[] parseArray(String s) {
        String nums = s.substring(1, s.length() - 1);
        String[] coords = nums.split(", ");
        return new int[] {Integer.parseInt(coords[0]), Integer.parseInt(coords[1])};
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();

        //initialize menu
        ter.initialize(WIDTH, HEIGHT);

        //initialize seed list
        seeds = new ArrayList<>();

        //draw menu
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setPenRadius(1000);
        StdDraw.textLeft(42, 45, "CS61B: BYOW");
        StdDraw.textLeft(42, 30, "(N) New Game");
        StdDraw.textLeft(42, 25, "(L) Load Game");
        StdDraw.textLeft(42, 20, "(Q) Quit Game");
        StdDraw.show();

        char k;
        while (true) {
            while (StdDraw.hasNextKeyTyped()) {
                k = StdDraw.nextKeyTyped();
                k = Character.toLowerCase(k);

                switch (k) {
                    case 'N', 'n':
                        avatar_name = avatarMenu();
                        System.out.println(avatar_name);
                        //AVATAR1 = new TETile('@', Color.white, Color.black, "you", avatar_name,0);
                        Tileset.update_avatar(avatar_name);
                        AVATAR1 = Tileset.GRASS;
                        promptSave(ter);
                        break;
                    case 'L', 'l':
                        Tileset.update_avatar(avatar_name);
                        load(ter);
                        break;
                    case 'Q', 'q':
                        System.exit(0);
                }
            }
        }
    }
}
