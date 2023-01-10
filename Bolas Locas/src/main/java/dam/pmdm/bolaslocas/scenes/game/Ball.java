package dam.pmdm.bolaslocas.scenes.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import dam.pmdm.bolaslocas.util.Colores;

public class Ball {

    private static final float TIEMPOOUT = 1500000000f;

    private final Game game;
    private float radius;
    private final PointF center;
    private float vx;
    private float vy;
    private final Paint paint;

    private float alpha = 255;

    public Ball(Game game, float radius, float x, float y, float speed, float direction) {
        this(game, radius, x, y, speed, direction, Colores.getPredefinido());
    }

    public Ball(Game game, float radius, float x, float y, float speed, float direction, int color) {
        this.game = game;
        this.radius = radius;
        center = new PointF(x, y);
        vx = speed * (float) Math.cos(direction);
        vy = speed * (float) Math.sin(direction);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setAlpha((int) alpha);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void move(long lapse) {
        center.offset(lapse * vx / 1000000000f, lapse * vy / 1000000000f);
        float left = center.x - radius;
        float right = center.x + radius;
        float top = center.y - radius;
        float bottom = center.y + radius;
        if (left < 0) {
            center.x -= 2 * left;
            vx = -vx;
        }
        else if (right > game.getWidth()) {
            center.x -= 2 * (right - game.getWidth());
            vx = -vx;
        }
        else if (top < 0) {
            center.y -= 2 * top;
            vy = -vy;
        }
        else if (bottom > game.getHeight()) {
            center.y -= 2 * (bottom - game.getHeight());
            vy = -vy;
        }
    }

    public boolean destroy(long lapse) {
        alpha -= (lapse * 255) / TIEMPOOUT;
        radius += 15;
        paint.setAlpha((int) alpha);
        return alpha <= 0;
    }

    public boolean in(float x, float y) {
        return (float) Math.sqrt(Math.pow(center.x - x, 2) + Math.pow(center.y - y, 2)) <= radius;
    }

    public void paint(Canvas canvas, int alpha) {
        paint.setAlpha(alpha);
        canvas.drawCircle(center.x, center.y, radius, paint);
    }

    public void paintDestroy(Canvas canvas) {
        paint.setAlpha((int) alpha);
        canvas.drawCircle(center.x, center.y, radius, paint);
    }

}
