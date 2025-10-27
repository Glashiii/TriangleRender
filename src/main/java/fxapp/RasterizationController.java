package fxapp;

import javafx.application.Platform;
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

    private double x, y;


    @FXML
    private void initialize() {
//        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
//        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
//        Triangle.test(canvas.getGraphicsContext2D());
        canvas.widthProperty().bind(anchorPane.widthProperty());
        canvas.heightProperty().bind(anchorPane.heightProperty());
        canvas.setOnMouseMoved(event -> {
            x = event.getX();
            y = event.getY();

            redrawScene();
        });
        Platform.runLater(this::redrawScene);

//        Triangle.drawTriangle(200,200,Color.RED,
//                    200, 350, Color.BLUE,
//                150, 150, new Color(0,1,0,0),
//                canvas.getGraphicsContext2D());
    }

    private void redrawScene() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        if (width == 0 || height == 0) return;

        PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelWriter.setColor(x, y, Color.WHITE);
            }
        }


        Triangle.drawTriangle(200, 210, Color.RED,
                210, 350, Color.BLUE,
                x, y, new Color(0, 1, 0, 0),
                canvas.getGraphicsContext2D());

        System.out.println("x: " + x + ", y: " + y);

//        Triangle.drawTriangle(200, 210, Color.RED,
//                210, 350, Color.BLUE,
//                398.4, 211.6, new Color(0, 1, 0, 0),
//                canvas.getGraphicsContext2D());
    }

}