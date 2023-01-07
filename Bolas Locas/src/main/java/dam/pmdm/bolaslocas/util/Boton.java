package dam.pmdm.bolaslocas.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Boton {

    private Bitmap bmp;
    private float x;
    private float y;

    public Boton(Bitmap bmp, float x, float y) {
        this.bmp = bmp;
        this.x = x;
        this.y = y;
    }

    public boolean touchIn(float x, float y) {
        return x > this.x && x < (this.x + bmp.getWidth()) && y > this.y && y < (this.y + bmp.getHeight());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bmp, x, y, null);
    }

}
