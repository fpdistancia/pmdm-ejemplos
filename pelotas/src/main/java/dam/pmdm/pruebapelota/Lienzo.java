package dam.pmdm.pruebapelota;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Lienzo extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, Runnable {

    private static final double ANG1 = 25 * Math.PI / 180;
    private static final double ANG2 = 40 * Math.PI / 180;
    private static final double ANG3 = Math.PI / 2;

    private Thread gameLoop;
    private final Deque<Pelota> pelotas = new LinkedList<>();
    private final List<Pelota> eliminadas = new LinkedList<>();
    private volatile boolean fin;
    private boolean pausado;
    private final Paint paint;

    public Lienzo(Context context) {
        super(context);
        getHolder().addCallback(this);
        paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.FILL);
        setOnTouchListener(this);

    }

    public void crearPelotas() {
        int cx = getWidth() / 2;
        int cy = getWidth() / 2;
        for (int i=0; i<100; i++) {
            float radio = (int) (Math.random() * (getHeight() * 0.15) + (getHeight() * 0.05));
            pelotas.add(new Pelota(
                    this,
                    radio,
                    cx, // radio + (int) (Math.random() * (getWidth() - 2 * radio)),
                    cy, // radio + (int) (Math.random() * (getHeight() - 2 * radio)),
                    (int) (Math.random() * 600 + 30),
                    (float) ((Math.random() * ANG2 + ANG1) + (ANG3 * Math.floor(Math.random() * 4)))
            ));
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        dibujar();
        if (pelotas.isEmpty())
            crearPelotas();
        if (gameLoop == null)
            (gameLoop = new Thread(this)).start();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        fin = true;
        if (pausado)
            gameLoop.interrupt();
        try { gameLoop.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        gameLoop = null;
    }

    public synchronized void pausar() {
        pausado = true;
    }

    public synchronized void reanudar() {
        pausado = false;
        notify();
    }

    private void dibujar(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
        synchronized (pelotas) {
            pelotas.forEach(p -> p.dibujar(canvas));
        }
        synchronized (eliminadas) {
            eliminadas.forEach(p -> p.dibujar(canvas));
        }
    }

    public void dibujar() {
        Canvas canvas = null;
        SurfaceHolder holder = getHolder();
        try {
            canvas = holder.lockCanvas();
            dibujar(canvas);
        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), e.getMessage(), e);
        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }
    }

    private synchronized void siguiente(long lapso) {
            for (Pelota p : pelotas)
                p.mover(lapso);
            eliminadas.removeIf(pelota -> pelota.disipar(lapso));
    }

    @Override
    public void run() {
        fin = false;
        long t0 = System.nanoTime(), t1, lapso;
        while (!fin) {
            synchronized (this) {
                if (pausado) {
                    try { wait(); } catch (InterruptedException ignored) { }
                    t0 = System.nanoTime();
                }
            }
            t1 = System.nanoTime();
            lapso = t1 - t0;
            t0 = t1;
            siguiente(lapso);
            dibujar();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("TOUCH", ACCIONES.get(event.getAction()) + " (" + event.getX() + ", " + event.getY() + ")");
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            synchronized (this) {
                if (!pelotas.isEmpty() && pelotas.getLast().dentro(event.getX(), event.getY()))
                    eliminadas.add(pelotas.removeLast());
            }
        return true;
    }

    private static final Map<Integer, String> ACCIONES;
    static {
        ACCIONES = new HashMap<>();
        ACCIONES.put(MotionEvent.ACTION_DOWN, "DOWN");
        ACCIONES.put(MotionEvent.ACTION_UP, "UP");
        ACCIONES.put(MotionEvent.ACTION_CANCEL, "CANCEL");
        ACCIONES.put(MotionEvent.ACTION_MOVE, "MOVE");
        ACCIONES.put(MotionEvent.ACTION_OUTSIDE, "OUTSIDE");
        ACCIONES.put(MotionEvent.ACTION_POINTER_DOWN, "POINTER DOWN");
        ACCIONES.put(MotionEvent.ACTION_POINTER_UP, "POINTER UP");
        ACCIONES.put(MotionEvent.ACTION_HOVER_MOVE, "HOVER MOVE");
        ACCIONES.put(MotionEvent.ACTION_POINTER_INDEX_SHIFT, "POINTER INDEX SHIFT");
        ACCIONES.put(MotionEvent.ACTION_HOVER_ENTER, "HOVER ENTER");
        ACCIONES.put(MotionEvent.ACTION_HOVER_EXIT, "HOVER EXIT");
        ACCIONES.put(MotionEvent.ACTION_BUTTON_PRESS, "BUTTON PRESS");
        ACCIONES.put(MotionEvent.ACTION_BUTTON_RELEASE, "BUTTON RELEASE");
    }
}
