package dam.pmdm.pruebapelota;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Pelota {

    private Juego juego;
    private float radio;
    private float x;
    private float y;
    private float vx;
    private float vy;
    private Paint paint;

    public Pelota (Juego juego, float radio, float x, float y, float velocidad, float direccion, int color) {
        this.juego = juego;
        this.radio = radio;
        this.x = x;
        this.y = y;
        vx = velocidad * (float) Math.cos(direccion);
        vy = velocidad * (float) Math.sin(direccion);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    public void mover(long lapso) {
        x += lapso * vx / 1000000000f;
        y += lapso * vy / 1000000000f;
    }

    public void dibujar(Canvas canvas) {
        canvas.drawCircle(x, y, radio, paint);
    }

}
