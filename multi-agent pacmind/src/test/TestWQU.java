package test;

import core.World;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import org.junit.jupiter.api.Test;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;


public class TestWQU {

    /**
     * This is an example from the spec.
     */
    @Test
    public void testWQUconnectedness() {
        TERenderer ter = new TERenderer();
        ter.initialize(30, 20);


        TETile[][] worldArray = new TETile[30][20];
        for (int x = 0; x < 30; x++) {
            for (int y = 0; y < 20; y++) {
                worldArray[x][y] = Tileset.NOTHING;
            }
        }
        World world = new World(worldArray, 304814395080L);
        world.buildRooms();
        //world.buildHallways();
        WeightedQuickUnionUF wqu = world.getWqu();
        ter.renderFrame(worldArray);

    }

    @Test
    public void testGivenExample() {

    }
}