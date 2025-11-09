package triangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Arrays;

import static triangle.TriangleRender.ERROR_CONST;

public class Triangle {
    private final PixelWriter pw;
    private final FloatPoint p1, p2, p3;
    private final float area;

    public Triangle(float x1, float y1, Color c1, float x2, float y2, Color c2,
                    float x3, float y3, Color c3, GraphicsContext gc) {
        this.pw = gc.getPixelWriter();
        this.p1 = new FloatPoint(x1, y1, new ColorVector(c1.getRed(), c1.getGreen(), c1.getBlue()));
        this.p2 = new FloatPoint(x2, y2, new ColorVector(c2.getRed(), c2.getGreen(), c2.getBlue()));
        this.p3 = new FloatPoint(x3, y3, new ColorVector(c3.getRed(), c3.getGreen(), c3.getBlue()));
        this.area = edgeFunction(this.p1, this.p2, this.p3);
    }

    public void draw() {
        // not triangle - 3 vertices on one line
        if (Math.abs(this.area) < ERROR_CONST) return;

        FloatPoint[] vertices = {this.p1, this.p2, this.p3};
        Arrays.sort(vertices, (p1, p2) -> Float.compare(p1.getY(), p2.getY()));

        var topPoint = vertices[0];
        var middlePoint = vertices[1];
        var bottomPoint = vertices[2];

        if (Math.abs(middlePoint.getY() - bottomPoint.getY()) < ERROR_CONST) {
            fillTrianglePart(topPoint, middlePoint, bottomPoint);
        } else if (Math.abs(topPoint.getY() - middlePoint.getY()) < ERROR_CONST) {
            fillTrianglePart(bottomPoint, topPoint, middlePoint);
        } else {
            // (y - y1)/(y2 - y1)
            float y_difference = (middlePoint.getY() - topPoint.getY()) / (bottomPoint.getY() - topPoint.getY());
            FloatPoint secondMiddlePoint = new FloatPoint(
                    topPoint.getX() + y_difference * (bottomPoint.getX() - topPoint.getX()),
                    middlePoint.getY(), null);
            fillTrianglePart(topPoint, middlePoint, secondMiddlePoint);
            fillTrianglePart(bottomPoint, middlePoint, secondMiddlePoint);
        }
    }
    private void fillTrianglePart(FloatPoint singlePoint, FloatPoint leftPairPoint, FloatPoint rightPairPoint) {
        if (leftPairPoint.getX() > rightPairPoint.getX()) {
            FloatPoint temp = leftPairPoint;
            leftPairPoint = rightPairPoint;
            rightPairPoint = temp;
        }

        int startY = (int) Math.ceil(Math.min(singlePoint.getY(), leftPairPoint.getY()) - 0.5f);
        int endY = (int) Math.ceil(Math.max(singlePoint.getY(), leftPairPoint.getY()) - 0.5f);

        for (int y = startY; y < endY; y++) {
            float yc = y + 0.5f;
            float currentXLeft = getXOnLine(singlePoint, leftPairPoint, yc);
            float currentXRight = getXOnLine(singlePoint, rightPairPoint, yc);
            drawScanLine((int) Math.ceil(currentXLeft - 0.5f), (int) Math.ceil(currentXRight - 0.5f), y);
        }
    }

    private void drawScanLine(int xStart, int xEnd, int y) {
        for (int x = xStart; x < xEnd; x++) {
            FloatPoint p = new FloatPoint(x + 0.5f, y + 0.5f, null);

            float w1 = edgeFunction(this.p2, this.p3, p) / this.area;
            float w2 = edgeFunction(this.p3, this.p1, p) / this.area;
            float w3 = edgeFunction(this.p1, this.p2, p) / this.area;

            ColorVector finalColor = this.p1.getColorVector().multiply(w1)
                    .add(this.p2.getColorVector().multiply(w2))
                    .add(this.p3.getColorVector().multiply(w3));

            this.pw.setColor(x, y, finalColor.toFxColor());
        }
    }

    private float getXOnLine(FloatPoint p1, FloatPoint p2, float y) {
        float dx = p2.getX() - p1.getX();
        float dy = p2.getY() - p1.getY();
        if (Math.abs(dy) < ERROR_CONST) return p1.getX();
        return p1.getX() + (y - p1.getY()) * dx / dy;
    }


    private static float edgeFunction(FloatPoint p1, FloatPoint p2, FloatPoint p3) {
        return (p3.getX() - p1.getX()) * (p2.getY() - p1.getY()) -
                (p3.getY() - p1.getY()) * (p2.getX() - p1.getX());
    }
}
