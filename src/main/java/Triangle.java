
import java.util.Arrays;

public class Triangle {

    public static void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
//        int[] yVertices = {y1, y2, y3};
//        Arrays.sort(yVertices);

        Point[] vertices = {new Point(x1, y1), new Point(x2, y2), new Point(x3, y3)};
        Arrays.sort(vertices);

        var topPoint = vertices[0];
        var middlePoint = vertices[1];
        var bottomPoint = vertices[2];




    }

    private static void drawLine(){

    }
}
