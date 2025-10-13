package triangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class Triangle {


//    public Triangle(GraphicsContext graphicsContext) {
//        this.graphicsContext = graphicsContext;
//    }

    public static void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
//        int[] yVertices = {y1, y2, y3};
//        Arrays.sort(yVertices);

        Point[] vertices = {new Point(x1, y1), new Point(x2, y2), new Point(x3, y3)};
        Arrays.sort(vertices);

        var topPoint = vertices[0];
        var middlePoint = vertices[1];
        var bottomPoint = vertices[2];

    }

    public static void test(final GraphicsContext graphicsContext) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                pixelWriter.setColor(x, y, Color.BLACK);
            }
        }
    }


    // TODO: replace
    public static void drawLineDDA(final GraphicsContext graphicsContext, int x1, int y1, int x2, int y2) {

        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        float deltaX = x2 - x1;
        float deltaY = y2 - y1;

        int i = 0;
        float step;

        if (Math.abs(deltaX) >= Math.abs(deltaY)) {
            step = Math.abs(deltaX);
        } else {
            step = Math.abs(deltaY);
        }
        deltaX = deltaX / step;
        deltaY = deltaY / step;
        float x = x1;
        float y = y1;

        while (i <= step) {
            pixelWriter.setColor(Math.round(x), Math.round(y), Color.BLACK);
            x += deltaX;
            y += deltaY;
            i += 1;
        }
    }

}
