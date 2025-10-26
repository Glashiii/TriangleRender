package triangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Comparator;

public class Triangle {
    private static final Double ERROR_CONST = 1e-6;
    private static GraphicsContext gc = null;
    private static PixelWriter pw = null;


//    public Triangle(GraphicsContext graphicsContext) {
//        this.gc = graphicsContext;
//    }

    public static void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3, Color color, GraphicsContext newGraphicContext) {
//        int[] yVertices = {y1, y2, y3};
//        Arrays.sort(yVertices);
        gc = newGraphicContext;
        pw = gc.getPixelWriter();
        DoublePoint[] vertices = {new DoublePoint(x1, y1), new DoublePoint(x2, y2), new DoublePoint(x3, y3)};

        Arrays.sort(vertices, (p1, p2) -> Double.compare(p1.getY(), p2.getY()));

        var topPoint = vertices[0];
        var middlePoint = vertices[1];
        var bottomPoint = vertices[2];


        if (Math.abs(topPoint.getY() - bottomPoint.getY()) < ERROR_CONST) return;
        if (Math.abs(middlePoint.getY() - bottomPoint.getY()) < ERROR_CONST) {
            fillTrianglePart(topPoint, middlePoint, bottomPoint, color);
        } else if (Math.abs(topPoint.getY() - middlePoint.getY()) < ERROR_CONST) {
            fillTrianglePart(middlePoint, topPoint, bottomPoint, color);
        } else {
            DoublePoint secondMiddlePoint = new DoublePoint(topPoint.getX() +
                    (middlePoint.getY() - topPoint.getY()) / (bottomPoint.getY() - topPoint.getY())
                            * (bottomPoint.getX() - topPoint.getX()),
                    middlePoint.getY());

            fillTrianglePart(topPoint, middlePoint, secondMiddlePoint, color);
            fillTrianglePart(middlePoint, secondMiddlePoint, bottomPoint, color);
        }
    }

    public static void test(final GraphicsContext graphicsContext) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                pixelWriter.setColor(x, y, Color.BLACK);
            }
        }
    }

    private static void fillTrianglePart(DoublePoint p1, DoublePoint p2, DoublePoint p3, Color color) {
        DoublePoint singlePoint, leftPairPoint, rightPairPoint;

        if (Math.abs(p1.getY() - p2.getY()) < ERROR_CONST) {
            if (p1.getX() < p2.getX()) {
                leftPairPoint = p1;
                rightPairPoint = p2;
            } else {
                leftPairPoint = p2;
                rightPairPoint = p1;
            }
            singlePoint = p3;
        } else if (Math.abs(p2.getY() - p3.getY()) < ERROR_CONST) {
            if (p2.getX() < p3.getX()) {
                leftPairPoint = p2;
                rightPairPoint = p3;
            } else {
                leftPairPoint = p3;
                rightPairPoint = p2;
            }
            singlePoint = p1;
        } else {
            if (p3.getX() < p1.getX()) {
                leftPairPoint = p3;
                rightPairPoint = p1;
            } else {
                leftPairPoint = p1;
                rightPairPoint = p3;
            }
            singlePoint = p1;
        }

        double dy = leftPairPoint.getY() - singlePoint.getY();
        // TODO check DY
        double dx_left = leftPairPoint.getX() - singlePoint.getX();
        double dx_right = rightPairPoint.getX() - singlePoint.getX();

        double step_left = dx_left / dy;
        double step_right = dx_right / dy;

        int startY = (int) Math.round(singlePoint.getY());
        int endY = (int) Math.round(leftPairPoint.getY());

        double currentXLeft, currentXRight;
        currentXLeft = currentXRight = singlePoint.getX();

        if (startY < endY) {
            for (int y = startY; y <= endY; y++) {
                drawHorizontalLine((int) Math.round(currentXLeft), (int) Math.round(currentXRight), y, color);
                currentXLeft += step_left;
                currentXRight += step_right;
            }
        } else {
            for (int y = startY; y >= endY; y--) {
                drawHorizontalLine((int) Math.round(currentXLeft), (int) Math.round(currentXRight), y, color);
                currentXLeft -= step_left;
                currentXRight -= step_right;
            }
        }


    }

    private static void drawHorizontalLine(int leftX, int rightX, int y, Color color) {
        for (int x = leftX; x <= rightX; x++) {
            pw.setColor(x, y, color);
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
