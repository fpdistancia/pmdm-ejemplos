package dam.pmdm.bolaslocas;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import dam.pmdm.bolaslocas.scenes.Scene;
import dam.pmdm.bolaslocas.transitions.Transition;

public class Stage extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, Runnable {

    private final Map<String, Transition> transitions = new HashMap<>();
    private Transition currentTransition;
    private String nextTransition;
    private long nextTransitionTime;

    private final Map<String, Scene> scenes = new HashMap<>();
    private Scene currentScene;
    private Scene initialScene;
    private String nextScene;

    private Thread gameLoop;
    private volatile boolean stopped;
    private boolean paused;

    public Stage(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    public void addScene(Scene scene, boolean isInitialScene) {
        if (scenes.containsKey(scene.getName()))
            throw new InvalidParameterException(String.format("ya existe una escena llamada \"%s\"", scene.getName()));
        if (isInitialScene) {
            if (this.initialScene != null)
                throw new InvalidParameterException(String.format("error añadiendo escena \"%s\" como escena inicial, ya existe una escena inicial llamada \"%s\"", scene.getName(), this.initialScene.getName()));
            this.initialScene = scene;
        }
        scenes.put(scene.getName(), scene);
    }

    public Scene getScene(String name) {
        if (!scenes.containsKey(name))
            throw new InvalidParameterException(String.format("no existe una escena llamada \"%s\"", name));
        return scenes.get(name);
    }

    public void setScene(String name) {
        if (!scenes.containsKey(name))
            throw new InvalidParameterException(String.format("no existe una escena llamada \"%s\"", name));
        detenerGameLoop();
        currentScene = scenes.get(name);
        currentScene.sceneSet();
        startGameLoop();
    }

    public void setScene(String sceneName, String transitionName, float time) {
        if (!scenes.containsKey(sceneName))
            throw new InvalidParameterException(String.format("no existe una escena llamada \"%s\"", sceneName));
        if (!transitions.containsKey(transitionName))
            throw new InvalidParameterException(String.format("no existe una escena llamada \"%s\"", transitionName));
        detenerGameLoop();
        Transition transition = getTransicion(transitionName);
        transition.setUp(getScene(sceneName), currentScene, time);
        this.currentTransition = transition;
        currentScene = transition.getSceneIn();
        currentScene.sceneSet();
        startGameLoop();
    }

    public void setNextScene(String name) {
        if (!scenes.containsKey(name))
            throw new InvalidParameterException(String.format("no existe una escena llamada \"%s\"", name));
        nextScene = name;
    }

    public void setNextScene(String sceneName, String transitionName, long time) {
        if (!scenes.containsKey(sceneName))
            throw new InvalidParameterException(String.format("no existe una escena llamada \"%s\"", sceneName));
        if (!transitions.containsKey(transitionName))
            throw new InvalidParameterException(String.format("no existe una escena llamada \"%s\"", transitionName));
        nextScene = sceneName;
        nextTransition = transitionName;
        nextTransitionTime = time;
    }

    public void addTransicion(String nombre, Transition transition) {
        if (transitions.containsKey(nombre))
            throw new InvalidParameterException("el nombre de la transición ya se está usando");
        transitions.put(nombre, transition);
    }

    public Transition getTransicion(String nombre) {
        if (!transitions.containsKey(nombre))
            throw new InvalidParameterException("no existe una transicion con ese nombre");
        return transitions.get(nombre);
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        paint();
//    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Log.i("SURFACE", "CREATED");
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.i("SURFACE", "CHANGED");
        scenes.values().forEach(escena -> escena.surfaceChanged(format, width, height));
        if (currentScene == null)
            currentScene = initialScene;
        startGameLoop();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        Log.i("SURFACE", "DESTROYED");
        detenerGameLoop();
    }

    public void startGameLoop() {
        if (gameLoop == null)
            (gameLoop = new Thread(this, "gameloop")).start();
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notify();
    }

    public void detenerGameLoop() {
        if (gameLoop != null && !stopped) {
            stopped = true;
            if (paused)
                resume();
            try {
                gameLoop.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void paint(Consumer<Canvas> consumer) {
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
        stopped = false;
        boolean finalized = false;
        long t0 = System.nanoTime(), t1, lapse;
        while (!stopped && !finalized) {
            synchronized (this) {
                while (paused) {
                    try { wait(); } catch (InterruptedException ignored) { }
                    t0 = System.nanoTime();
                }
            }
            t1 = System.nanoTime();
            lapse = t1 - t0;
            t0 = t1;
            if (currentTransition != null) {
                boolean finTransicion = currentTransition.next(lapse);
                paint(currentTransition::paint);
                if (finTransicion)
                    currentTransition = null;
            }
            else {
                finalized = currentScene.next(lapse, false);
                paint(currentScene::paint);
            }
        }
        gameLoop = null;
        if (finalized)
            if (nextScene != null) {
                if (nextTransition != null) {
                    Transition transition = getTransicion(nextTransition);
                    transition.setUp(scenes.get(nextScene), currentScene, nextTransitionTime);
                    currentTransition = transition;
                    currentScene = transition.getSceneIn();
                    nextTransition = null;
                }
                else
                    currentScene = scenes.get(nextScene);
                nextScene = null;
                currentScene.sceneSet();
                startGameLoop();
            }
        Log.i("GAME LOOP", "DETENIDO");
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        Log.i("TOUCH", ACTIONS.get(event.getAction()) + " (" + event.getX() + ", " + event.getY() + ")");
        if (currentTransition == null && currentScene != null)
            currentScene.onTouch(event);
        return true;
    }

    public boolean onBackPressed() {
        return currentScene.onBackPressed();
    }

    private static final Map<Integer, String> ACTIONS;
    static {
        ACTIONS = new HashMap<>();
        ACTIONS.put(MotionEvent.ACTION_DOWN, "DOWN");
        ACTIONS.put(MotionEvent.ACTION_UP, "UP");
        ACTIONS.put(MotionEvent.ACTION_CANCEL, "CANCEL");
        ACTIONS.put(MotionEvent.ACTION_MOVE, "MOVE");
        ACTIONS.put(MotionEvent.ACTION_OUTSIDE, "OUTSIDE");
        ACTIONS.put(MotionEvent.ACTION_POINTER_DOWN, "POINTER DOWN");
        ACTIONS.put(MotionEvent.ACTION_POINTER_UP, "POINTER UP");
        ACTIONS.put(MotionEvent.ACTION_HOVER_MOVE, "HOVER MOVE");
        ACTIONS.put(MotionEvent.ACTION_POINTER_INDEX_SHIFT, "POINTER INDEX SHIFT");
        ACTIONS.put(MotionEvent.ACTION_HOVER_ENTER, "HOVER ENTER");
        ACTIONS.put(MotionEvent.ACTION_HOVER_EXIT, "HOVER EXIT");
        ACTIONS.put(MotionEvent.ACTION_BUTTON_PRESS, "BUTTON PRESS");
        ACTIONS.put(MotionEvent.ACTION_BUTTON_RELEASE, "BUTTON RELEASE");
    }

}
