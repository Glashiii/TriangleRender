package fxapp;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import triangle.Triangle;

public class RasterizationController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;



    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
//        Triangle.test(canvas.getGraphicsContext2D());
//        Triangle.drawLineDDA(canvas.getGraphicsContext2D(), 5, 5, 108, 90);
        Triangle.drawTriangle(10,10,Color.RED,
                    10, 150, Color.BLUE,
                150, 150, new Color(0,1,0,0),
                canvas.getGraphicsContext2D());
//        Rasterization.drawRectangle(canvas.getGraphicsContext2D(), 200, 300, 200, 100, Color.CHOCOLATE);
//        Rasterization.drawRectangle(canvas.getGraphicsContext2D(), 250, 250, 50, 200, Color.AQUA);
    }

}