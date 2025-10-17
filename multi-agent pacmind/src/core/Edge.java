package core;

public class Edge implements Comparable<Edge> {
    Node r1;
    Node r2;
    double weight;

    public Edge(Node r1, Node r2) {
        this.r1 = r1;
        this.r2 = r2;
        this.weight = getDistance(r1, r2);

    }

    private double getDistance(Node a, Node b) {
        int ax = a.topLeftCoord[0] + a.width / 2;
        int ay = a.topLeftCoord[1] - a.height / 2;

        int bx = b.topLeftCoord[0] + b.width / 2;
        int by = b.topLeftCoord[1] - b.height / 2;

        // return Math.abs(ax - bx) + Math.abs(ay - by); // Manhattan
        return Math.sqrt(Math.pow(ax - bx, 2) + Math.pow(ay - by, 2)); // Euclidean
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }
}
