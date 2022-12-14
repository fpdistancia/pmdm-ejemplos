package dam.ejemplospmdm.pelotarebotando;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Juego implements Runnable {

    private Thread thread;
    private Lienzo lienzo;
    private Pelota pelota;
    private volatile boolean fin;
    private boolean suspendido;
    private Paint paint;



    public Juego(Lienzo lienzo) {
        this.lienzo = lienzo;
        pelota = new Pelota(50, lienzo.getWidth() / 2f, lienzo.getHeight() / 2f, 50f, (float) Math.PI /4f, Color.RED);
        paint = new Paint(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
    }

    public void comenzar() {
        thread = new Thread(this);
        thread.start();
    }

    public void detener() {
        fin = true;
        try { thread.join(); } catch (InterruptedException e) {}
    }

    public synchronized void suspender() {
        suspendido = true;
    }
    public synchronized void reanudar() {
        suspendido = false;
        notify();
    }

    private void dibujar() {
        Canvas canvas = lienzo.getCanvas();
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        pelota.dibujar(lienzo.getCanvas());
        lienzo.render();
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
            dibujar();
        }
    }

}
