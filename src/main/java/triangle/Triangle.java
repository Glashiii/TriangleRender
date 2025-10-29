package triangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class Triangle {
    private static final Float ERROR_CONST = 1e-6f;
    private static PixelWriter pw = null;

    public static void drawTriangle(float x1, float y1, Color color1, float x2, float y2, Color color2,
                                    float x3, float y3, Color color3, GraphicsContext newGraphicContext) {
        pw = newGraphicContext.getPixelWriter();

        ColorVector colorVector1 = new ColorVector(color1.getRed(), color1.getGreen(), color1.getBlue());
        ColorVector colorVector2 = new ColorVector(color2.getRed(), color2.getGreen(), color2.getBlue());
        ColorVector colorVector3 = new ColorVector(color3.getRed(), color3.getGreen(), color3.getBlue());

        FloatPoint[] vertices = {new FloatPoint(x1, y1, colorVector1), new FloatPoint(x2, y2, colorVector2), new FloatPoint(x3, y3, colorVector3)};

        Arrays.sort(vertices, (p1, p2) -> Float.compare(p1.getY(), p2.getY()));

        var topPoint = vertices[0];
        var middlePoint = vertices[1];
        var bottomPoint = vertices[2];

        if (Math.abs(topPoint.getY() - bottomPoint.getY()) < ERROR_CONST) return;
        if (Math.abs(middlePoint.getY() - bottomPoint.getY()) < ERROR_CONST) {
            fillTrianglePart(topPoint, middlePoint, bottomPoint);
        } else if (Math.abs(topPoint.getY() - middlePoint.getY()) < ERROR_CONST) {
            fillTrianglePart(bottomPoint, topPoint, middlePoint);
        } else {
            // (y - y1)/(y2 - y1)
            float y_difference = (middlePoint.getY() - topPoint.getY()) / (bottomPoint.getY() - topPoint.getY());

            FloatPoint secondMiddlePoint = new FloatPoint(topPoint.getX() +
                    y_difference * (bottomPoint.getX() - topPoint.getX()),
                    middlePoint.getY(),
                    topPoint.getColorVector()
                            .add(bottomPoint.getColorVector().subtract(topPoint.getColorVector()).multiply(y_difference)));
            fillTrianglePart(topPoint, middlePoint, secondMiddlePoint);
            fillTrianglePart(bottomPoint, middlePoint, secondMiddlePoint);
        }
    }

    private static void fillTrianglePart(FloatPoint singlePoint, FloatPoint leftPairPoint, FloatPoint rightPairPoint) {
        if (leftPairPoint.getX() > rightPairPoint.getX()) {
            FloatPoint temp = leftPairPoint;
            leftPairPoint = rightPairPoint;
            rightPairPoint = temp;
        }
        // square of parallelogram (2 triangles)
        float denominator = (leftPairPoint.getX() - singlePoint.getX()) * (rightPairPoint.getY() - singlePoint.getY())
                - (rightPairPoint.getX() - singlePoint.getX()) * (leftPairPoint.getY() - singlePoint.getY());

        if (Math.abs(denominator) < ERROR_CONST) return;

        float dy = leftPairPoint.getY() - singlePoint.getY();
        float dxLeft = leftPairPoint.getX() - singlePoint.getX();
        float stepLeft = dxLeft / dy;

        // cool article https://github.com/ssloy/tinyrenderer/wiki/Lesson-2:-Triangle-rasterization-and-back-face-culling
        // and another one https://www.scratchapixel.com/lessons/3d-basic-rendering/rasterization-practical-implementation/rasterization-stage.html
        float dvDx = (rightPairPoint.getY() - singlePoint.getY()) / denominator;
        float dvDy = (singlePoint.getX() - rightPairPoint.getX()) / denominator;
        float dwDx = (singlePoint.getY() - leftPairPoint.getY()) / denominator;
        float dwDy = (leftPairPoint.getX() - singlePoint.getX()) / denominator;

        //shows which way color changes from leftPairPoint to singlePoint
        ColorVector dcDv = leftPairPoint.getColorVector().subtract(singlePoint.getColorVector());
        ColorVector dcDw = rightPairPoint.getColorVector().subtract(singlePoint.getColorVector());

        ColorVector dColorDx = dcDv.multiply(dvDx).add(dcDw.multiply(dwDx));
        ColorVector dColorDy = dcDv.multiply(dvDy).add(dcDw.multiply(dwDy));
        ColorVector dColorEdgeLeft = dColorDx.multiply(stepLeft).add(dColorDy);

        int startY, endY;
        if (singlePoint.getY() > leftPairPoint.getY()) {
            startY = (int) Math.ceil(leftPairPoint.getY() - 0.5);
            endY   = (int) Math.ceil(singlePoint.getY() - 0.5) - 1;
        } else {
            startY = (int) Math.ceil(singlePoint.getY() - 0.5);
            endY   = (int) Math.ceil(leftPairPoint.getY() - 0.5) - 1;
        }

        float yCenter = startY + 0.5f;
        float yPrestep = yCenter - singlePoint.getY();
        float currentXLeft = singlePoint.getX() + stepLeft * yPrestep;
        float currentXRight;
        ColorVector colorLeft = singlePoint.getColorVector().add(dColorEdgeLeft.multiply(yPrestep));

        for (int y = startY; y <= endY; y++) {
            float yc = y + 0.5f;

            currentXRight = getXOnLine(singlePoint, rightPairPoint, yc);

            int xL = (int) Math.ceil(currentXLeft - 0.5f);
            int xR = (int) Math.ceil(currentXRight - 0.5f);

            if (xL < xR) {
                ColorVector startColor = colorLeft.add(dColorDx.multiply((xL + 0.5) - currentXLeft));
                drawHorizontalLine(xL, xR, y, startColor, dColorDx);
            }

            currentXLeft += stepLeft;
            colorLeft = colorLeft.add(dColorEdgeLeft);
        }
    }

    private static void drawHorizontalLine(int leftX, int rightX, int y, ColorVector startColor, ColorVector dColor_dx) {
        for (int x = leftX; x < rightX; x++) {
            pw.setColor(x, y, startColor.toFxColor());
            startColor = startColor.add(dColor_dx);
        }
    }
    private static float getXOnLine(FloatPoint point1, FloatPoint point2, float y) {
        float dx = point2.getX() - point1.getX();
        float dy = point2.getY() - point1.getY();

        if (Math.abs(dy) < ERROR_CONST) return point1.getX();
        return point1.getX() + (y - point1.getY()) * dx / dy;
    }
}
