package dam.pmdm.bolaslocas;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import dam.pmdm.bolaslocas.escenas.Escena;
import dam.pmdm.bolaslocas.transiciones.Transicion;

public class Escenario extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, Runnable {

    private EscenarioCallback escenarioCallback;

    private final Map<String, Transicion> transiciones = new HashMap<>();
    private Transicion transicion;

    private final Map<String, Escena> escenas = new HashMap<>();
    private Escena escena;

    private Thread gameLoop;
    private volatile boolean fin;
    private boolean pausado;
    private boolean creado = false;

    private Escena inicial;

    public Escenario(Context context, EscenarioCallback callback) {
        super(context);
        this.escenarioCallback = callback;
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    public void addEscena(Escena escena, boolean inicial) {
        if (escenas.containsKey(escena.getNombre()))
            throw new InvalidParameterException(String.format("ya existe una escena llamada \"%s\"", escena.getNombre()));
        escenas.put(escena.getNombre(), escena);
        if (inicial)
            this.inicial = escena;
    }

    public Escena getEscena(String nombre) {
        if (!escenas.containsKey(nombre))
            throw new InvalidParameterException(String.format("no existe una escena llamada \"%s\"", nombre));
        return escenas.get(nombre);
    }

    public void setEscena(String nombre) {
        detenerGameLoop();
        if (!escenas.containsKey(nombre))
            throw new InvalidParameterException(String.format("no existe una escena llamada \"%s\"", nombre));
        escena = escenas.get(nombre);
        escena.iniciar();
        iniciarGameLoop();
    }

    public void setEscena(String nombreEscena, String nombreTransicion) {
        detenerGameLoop();
        Transicion transicion = getTransicion(nombreTransicion);
        transicion.preparar(getEscena(nombreEscena), escena);
        this.transicion = transicion;
        escena = transicion.getEntrante();
        escena.iniciar();
        iniciarGameLoop();
    }

    public void addTransicion(String nombre, Transicion transicion) {
        if (transiciones.containsKey(nombre))
            throw new InvalidParameterException("el nombre de la transición ya se está usando");
        transiciones.put(nombre, transicion);
    }

    public Transicion getTransicion(String nombre) {
        if (!transiciones.containsKey(nombre))
            throw new InvalidParameterException("no existe una transicion con ese nombre");
        return transiciones.get(nombre);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        dibujar();
//    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Log.i("SURFACE", "CREATED");
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.i("SURFACE", "CHANGED");

        if (!creado) {
            creado = true;
            escenarioCallback.callback(this);
            escena = inicial;
        }
        else {
            escenas.values().forEach(escena -> escena.actualizar(format, width, height));
        }
        iniciarGameLoop();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        Log.i("SURFACE", "DESTROYED");
        detenerGameLoop();
    }

    public void iniciarGameLoop() {
        if (gameLoop == null)
            (gameLoop = new Thread(this)).start();
    }

    public synchronized void pausar() {
        pausado = true;
    }

    public synchronized void reanudar() {
        pausado = false;
        notify();
    }

    public void detenerGameLoop() {
        if (gameLoop != null) {
            fin = true;
            if (pausado)
                reanudar();
            try {
                gameLoop.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gameLoop = null;
    }

//    private boolean dibujar(Consumer<Canvas> consumer) {
//        Canvas canvas = null;
//        SurfaceHolder holder = getHolder();
//        try {
//            if ((canvas = holder.lockCanvas()) != null)
//                consumer.accept(canvas);
//        } catch (Exception e) {
//            Log.e(getClass().getCanonicalName(), e.getMessage(), e);
//        } finally {
//            if (canvas != null)
//                holder.unlockCanvasAndPost(canvas);
//        }
//        return true;
//    }

    private void dibujar(Consumer<Canvas> consumer) {
        Canvas canvas = null;
        SurfaceHolder holder = getHolder();
        try {
            canvas = holder.lockCanvas();
            consumer.accept(canvas);
        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), e.getMessage(), e);
        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }
    }

//    @Override
//    public void run() {
//        fin = false;
//        long t0 = System.nanoTime(), t1, lapso = 0;
//        while (!fin) {
//            synchronized (this) {
//                while (pausado) {
//                    try { wait(); } catch (InterruptedException ignored) { }
//                    t0 = System.nanoTime();
//                }
//            }
//            if (transicion != null) {
//                boolean fin = transicion.siguiente(lapso);
//                dibujar(transicion::dibujar);
//                if (fin) {
//                    transicion = null;
//                }
//            }
//            else {
//                fin = escena.siguiente(lapso);
//                dibujar(escena::dibujar);
//            }
//            t1 = System.nanoTime();
//            lapso = t1 - t0;
//            t0 = t1;
//
//        }
//    }

    @Override
    public void run() {
        Log.i("GAME LOOP", "INICIADO");
        fin = false;
        long t0 = System.nanoTime(), t1, lapso;
        while (!fin) {
            synchronized (this) {
                while (pausado) {
                    try { wait(); } catch (InterruptedException ignored) { }
                    t0 = System.nanoTime();
                }
            }
            t1 = System.nanoTime();
            lapso = t1 - t0;
            t0 = t1;
            if (transicion != null) {
                boolean fin = transicion.siguiente(lapso);
                dibujar(transicion::dibujar);
                if (fin) {
                    transicion = null;
                }
            }
            else {
                fin = escena.siguiente(lapso);
                dibujar(escena::dibujar);
            }
        }
        Log.i("GAME LOOP", "DETENIDO");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("TOUCH", ACCIONES.get(event.getAction()) + " (" + event.getX() + ", " + event.getY() + ")");
        escena.onTouch(event);
        return true;
    }

    public boolean onBackPressed() {
        return escena.onBackPressed();
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
