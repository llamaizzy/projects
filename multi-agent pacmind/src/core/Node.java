package core;

public class Node {
    int height;
    int width;
    int[] topLeftCoord;
    boolean isRoom = true;
    int id;

    public Node(int id, int height, int width, int[] topLeft) {
        this.id = id;
        this.height =  height;
        this.width = width;
        topLeftCoord = topLeft;
        if (height == 1 || width == 1) {
          isRoom = false; //this is a hallway
      }
    }
}
