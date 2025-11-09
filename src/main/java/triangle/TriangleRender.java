package triangle;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class TriangleRender {
    static final Float ERROR_CONST = 1e-6f;

    public static void drawTriangle(float x1, float y1, Color color1, float x2, float y2, Color color2,
                                    float x3, float y3, Color color3, GraphicsContext gc) {
        new Triangle(x1, y1, color1, x2, y2, color2, x3, y3, color3, gc).draw();
    }
}