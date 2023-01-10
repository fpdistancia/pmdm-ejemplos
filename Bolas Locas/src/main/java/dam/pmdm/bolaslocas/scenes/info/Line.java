package dam.pmdm.bolaslocas.scenes.info;

public class Line {String text;

    private float width;
    private float height;
    private int color;

    public Line(String text, int color) {
        this.text = text;
        this.color = color;
    }

    public void setBounds(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getColor() {
        return color;
    }
}
