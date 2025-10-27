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

    public static void drawTriangle(double x1, double y1, Color color1, double x2, double y2, Color color2,
                                    double x3, double y3, Color color3, GraphicsContext newGraphicContext) {

        gc = newGraphicContext;
        pw = gc.getPixelWriter();

        ColorVector colorVector1 = new ColorVector(color1.getRed(), color1.getGreen(), color1.getBlue());
        ColorVector colorVector2 = new ColorVector(color2.getRed(), color2.getGreen(), color2.getBlue());
        ColorVector colorVector3 = new ColorVector(color3.getRed(), color3.getGreen(), color3.getBlue());

        DoublePoint[] vertices = {new DoublePoint(x1, y1, colorVector1), new DoublePoint(x2, y2, colorVector2), new DoublePoint(x3, y3, colorVector3)};

        Arrays.sort(vertices, (p1, p2) -> Double.compare(p1.getY(), p2.getY()));

        var topPoint = vertices[0];
        var middlePoint = vertices[1];
        var bottomPoint = vertices[2];


        if (Math.abs(topPoint.getY() - bottomPoint.getY()) < ERROR_CONST) return;
        if (Math.abs(middlePoint.getY() - bottomPoint.getY()) < ERROR_CONST) {
            fillTrianglePart(topPoint, middlePoint, bottomPoint, false);
        } else if (Math.abs(topPoint.getY() - middlePoint.getY()) < ERROR_CONST) {
            fillTrianglePart(bottomPoint, topPoint, middlePoint, false);
        } else {
            // (y - y1)/(y2 - y1)
            double y_difference = (middlePoint.getY() - topPoint.getY()) / (bottomPoint.getY() - topPoint.getY());

            DoublePoint secondMiddlePoint = new DoublePoint(topPoint.getX() +
                    y_difference * (bottomPoint.getX() - topPoint.getX()),
                    middlePoint.getY(),
                    topPoint.getColorVector()
                            .add(bottomPoint.getColorVector().subtract(topPoint.getColorVector()).multiply(y_difference)));
            fillTrianglePart(bottomPoint, middlePoint, secondMiddlePoint, false);
            fillTrianglePart(topPoint, middlePoint, secondMiddlePoint, true);

        }
    }

    private static void fillTrianglePart(DoublePoint singlePoint, DoublePoint leftPairPoint, DoublePoint rightPairPoint, boolean isSecondPart) {
        if (leftPairPoint.getX() > rightPairPoint.getX()) {
            DoublePoint temp = leftPairPoint;
            leftPairPoint = rightPairPoint;
            rightPairPoint = temp;
        }
        // square of parallelogram (2 triangles)
        double denominator = (leftPairPoint.getX() - singlePoint.getX()) * (rightPairPoint.getY() - singlePoint.getY())
                - (rightPairPoint.getX() - singlePoint.getX()) * (leftPairPoint.getY() - singlePoint.getY());
        if (Math.abs(denominator) < ERROR_CONST) return;



        double dy = leftPairPoint.getY() - singlePoint.getY();

        double dx_left = leftPairPoint.getX() - singlePoint.getX();
//        double dx_right = rightPairPoint.getX() - singlePoint.getX();



        double step_left = dx_left / dy;
//        double step_right = dx_right / dy;

        // cool article https://github.com/ssloy/tinyrenderer/wiki/Lesson-2:-Triangle-rasterization-and-back-face-culling
        // and another one https://www.scratchapixel.com/lessons/3d-basic-rendering/rasterization-practical-implementation/rasterization-stage.html
        double dv_dx = (rightPairPoint.getY() - singlePoint.getY()) / denominator;
        double dv_dy = (singlePoint.getX() - rightPairPoint.getX()) / denominator;
        double dw_dx = (singlePoint.getY() - leftPairPoint.getY()) / denominator;
        double dw_dy = (leftPairPoint.getX() - singlePoint.getX()) / denominator;

        //shows which way color changes from leftPairPoint to singlePoint
        ColorVector dc_dv = leftPairPoint.getColorVector().subtract(singlePoint.getColorVector());
        ColorVector dc_dw = rightPairPoint.getColorVector().subtract(singlePoint.getColorVector());

        ColorVector dColor_dx = dc_dv.multiply(dv_dx).add(dc_dw.multiply(dw_dx));
        ColorVector dColor_dy = dc_dv.multiply(dv_dy).add(dc_dw.multiply(dw_dy));

        ColorVector dColor_edge_left = dColor_dx.multiply(step_left).add(dColor_dy);
//        int startY = (int) Math.round(singlePoint.getY());
//        int endY = (int) Math.round(leftPairPoint.getY());
        int startY = (int) Math.ceil(singlePoint.getY());
        int endY = (int) Math.ceil(leftPairPoint.getY());
//        double yPrestep = (startY + 0.5) - singlePoint.getY();


        double currentXLeft = singlePoint.getX();
        ColorVector color_left = singlePoint.getColorVector();

        double currentXRight = singlePoint.getX();
        // that should fix artefacts with 2 parts triangle when upper part dy is very small
        if (isSecondPart) endY -= 1;


        if (startY < endY) {
            for (int y = startY; y <= endY; y++) {
                currentXRight = getXOnLine(singlePoint, rightPairPoint, y);
                drawHorizontalLine((int) Math.round(currentXLeft), (int) Math.ceil(currentXRight-1), y, color_left, dColor_dx);
                currentXLeft += step_left;
//                currentXRight = currentXLeft + (y - singlePoint.getY());
                color_left = color_left.add(dColor_edge_left);
            }
        } else {
            for (int y = startY; y >= endY; y--) {
                currentXRight = getXOnLine(singlePoint, rightPairPoint, y);
                drawHorizontalLine((int)Math.round(currentXLeft), (int)Math.ceil(currentXRight-1), y, color_left, dColor_dx);
                currentXLeft -= step_left;
//                currentXRight -= step_right;
                color_left = color_left.subtract(dColor_edge_left);
            }
        }
    }

    private static void drawHorizontalLine(int leftX, int rightX, int y, ColorVector startColor, ColorVector dColor_dx) {
        if (leftX > rightX) {
            int temp = leftX; leftX = rightX; rightX = temp;
            startColor = startColor.add(dColor_dx.multiply(leftX - rightX));
        }
        for (int x = leftX; x < rightX; x++) {
            pw.setColor(x, y, startColor.toFxColor());
            startColor = startColor.add(dColor_dx);
        }
        System.out.println("line drawn");
    }
    private static double getXOnLine(DoublePoint point1, DoublePoint point2, double y) {
        double dx = point2.getX() - point1.getX();
        double dy = point2.getY() - point1.getY();

        if (Math.abs(dy) < ERROR_CONST) return point1.getX();
        return point1.getX() + (y - point1.getY()) * dx / dy;
    }

}
