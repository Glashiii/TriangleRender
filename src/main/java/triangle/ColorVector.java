package triangle;

import javafx.scene.paint.Color;

public class ColorVector {
    private Double red, green, blue;


    public ColorVector(Double red, Double green, Double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public ColorVector add(ColorVector toAdd){
        return new ColorVector(red + toAdd.getRed(), green + toAdd.getGreen(), blue + toAdd.getBlue());
    }
    public ColorVector subtract(ColorVector toSubtract) {
        return new ColorVector(red - toSubtract.red,
            green - toSubtract.green, blue - toSubtract.blue);
    }
    public ColorVector multiply(double scalar) {
        return new ColorVector(red * scalar, green * scalar, blue * scalar);}
    
    public Color toFxColor() {
        return Color.color(
                Math.max(0, Math.min(1, red)),
                Math.max(0, Math.min(1, green)),
                Math.max(0, Math.min(1, blue))
        );
    }

    public Double getRed() {
        return red;
    }

    public void setRed(Double red) {
        this.red = red;
    }

    public Double getGreen() {
        return green;
    }

    public void setGreen(Double green) {
        this.green = green;
    }

    public Double getBlue() {
        return blue;
    }

    public void setBlue(Double blue) {
        this.blue = blue;
    }
}
