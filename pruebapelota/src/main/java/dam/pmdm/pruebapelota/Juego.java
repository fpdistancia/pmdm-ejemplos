package dam.pmdm.pruebapelota;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class Juego implements Runnable {

    private float w;
    private float h;
    private SurfaceHolder holder;
    private Thread thread;
    private Pelota pelota;
    private volatile boolean fin;
    private boolean suspendido;
    private Paint paint;

    public Juego() {
        pelota = new Pelota(this, 50, 50, 50, 50f, (float) Math.PI /4f, Color.RED);
        paint = new Paint(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
    }

    public float getW() {
        return w;
    }

    public float getH() {
        return h;
    }

    public void jugar(SurfaceHolder holder, float w, float h) {
        this.holder = holder;
        this.w = w;
        this.h = h;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void detener() {
        fin = true;
        if (suspendido)
            reanudar();
    }

    public synchronized void suspender() {
        suspendido = true;
    }

    public synchronized void reanudar() {
        suspendido = false;
        notify();
    }

    private void dibujar(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        pelota.dibujar(canvas);
    }

    public void render() {
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas();
            synchronized (holder) {
                dibujar(canvas);
            }
        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), e.getMessage(), e);
        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public void run() {
        fin = false;
        long t0 = System.nanoTime(), t1, lapso;
        while (!fin) {
            synchronized (this) {
                if (suspendido)
                    try { wait(); } catch (InterruptedException e) {}
            }
            t1 = System.nanoTime();
            lapso = t1 - t0;
            t0 = t1;
            pelota.mover(lapso);
            render();
        }
    }

}
