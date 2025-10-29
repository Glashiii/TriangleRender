package triangle;

public class FloatPoint implements Point<Float> {
    private Float x;
    private Float y;
    private ColorVector colorVector;

    public FloatPoint(Float x, Float y, ColorVector colorVector) {
        this.x = x;
        this.y = y;
        this.colorVector = colorVector;
    }

    @Override
    public Float getX() {
        return x;
    }

    @Override
    public Float getY() {
        return y;
    }

    public ColorVector getColorVector() {
        return colorVector;
    }

    public void setColorVector(ColorVector colorVector) {
        this.colorVector = colorVector;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public void setY(Float y) {
        this.y = y;
    }
}
