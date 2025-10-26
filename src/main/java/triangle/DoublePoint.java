package triangle;

public class DoublePoint implements Point<Double> {
    private Double x;
    private Double y;
    private ColorVector colorVector;

    public DoublePoint(Double x, Double y, ColorVector colorVector) {
        this.x = x;
        this.y = y;
        this.colorVector = colorVector;
    }

    @Override
    public Double getX() {
        return x;
    }

    @Override
    public Double getY() {
        return y;
    }

    public ColorVector getColorVector() {
        return colorVector;
    }

    public void setColorVector(ColorVector colorVector) {
        this.colorVector = colorVector;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
