package dam.pmdm.pruebapelota;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Pelota {

    private static final float TIEMPOIN = 3000000000f;
    private static final float TIEMPOOUT = 1500000000f;

    private final Lienzo juego;
    private float radio;
    private final PointF centro;
    private float vx;
    private float vy;
    private final Paint paint;
    private float alpha = 0;

    public Pelota (Lienzo juego, float radio, float x, float y, float velocidad, float direccion) {
        this(juego, radio, x, y, velocidad, direccion, Colores.get());
    }

    public Pelota (Lienzo juego, float radio, float x, float y, float velocidad, float direccion, int color) {
        this.juego = juego;
        this.radio = radio;
        centro = new PointF(x, y);
        vx = velocidad * (float) Math.cos(direccion);
        vy = velocidad * (float) Math.sin(direccion);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setAlpha((int) alpha);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void mover(long lapso) {
        if (alpha < 255) {
            alpha += (lapso * 255) / TIEMPOIN;
            if (alpha > 255)
                alpha = 255;
            paint.setAlpha((int) alpha);
        }
        centro.offset(lapso * vx / 1000000000f, lapso * vy / 1000000000f);
        float xl = centro.x - radio;
        float xr = centro.x + radio;
        float ys = centro.y - radio;
        float yi = centro.y + radio;
        if (xl < 0) {
            centro.x -= 2 * xl;
            vx = -vx;
        }
        else if (xr > juego.getWidth()) {
            centro.x -= 2 * (xr - juego.getWidth());
            vx = -vx;
        }
        else if (ys < 0) {
            centro.y -= 2 * ys;
            vy = -vy;
        }
        else if (yi > juego.getHeight()) {
            centro.y -= 2 * (yi - juego.getHeight());
            vy = -vy;
        }
    }

    public boolean disipar(long lapso) {
        alpha -= (lapso * 255) / TIEMPOOUT;
        radio += 15;
        paint.setAlpha((int) alpha);
        return alpha <= 0;
    }

    public boolean dentro(float x, float y) {
        float d = (float) Math.sqrt(Math.pow(centro.x - x, 2) + Math.pow(centro.y - y, 2));
        return d <= radio;
    }

    public void dibujar(Canvas canvas) {
        canvas.drawCircle(centro.x, centro.y, radio, paint);
    }

}
