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
        double xl = x - radio;
        double xr = x + radio;
        double ys = y - radio;
        double yi = y + radio;
        if (xl < 0) {
            x -= 2 * xl;
            vx = -vx;
        }
        else if (xr > juego.getW()) {
            x -= 2 * (xr - juego.getW());
            vx = -vx;
        }
        else if (ys < 0) {
            y -= 2 * ys;
            vy = -vy;
        }
        else if (yi > juego.getH()) {
            y -= 2 * (yi - juego.getH());
            vy = -vy;
        }
    }

    public void dibujar(Canvas canvas) {
        canvas.drawCircle(x, y, radio, paint);
    }

}
