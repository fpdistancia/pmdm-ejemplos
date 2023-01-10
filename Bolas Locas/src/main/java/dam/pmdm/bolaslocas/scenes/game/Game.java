package dam.pmdm.bolaslocas.scenes.game;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import dam.pmdm.bolaslocas.Stage;
import dam.pmdm.bolaslocas.scenes.info.Info;
import dam.pmdm.bolaslocas.scenes.Scene;
import dam.pmdm.bolaslocas.scenes.Start;
import dam.pmdm.bolaslocas.scenes.info.Line;

public class Game extends Scene {

    private static final double ANG1 = 25 * Math.PI / 180;
    private static final double ANG2 = 40 * Math.PI / 180;
    private static final double ANG3 = Math.PI / 2;

    private final Deque<Ball> balls = new LinkedList<>();
    private final List<Ball> removed = new LinkedList<>();
    private float ballsAlpha;

    private String ballCount;
    private final Rect ballCountBounds = new Rect();
    private float ballCountX;
    private float ballCountY;
    private float ballCountHeight;
    private final float ballCountTime = 2000000000f;
    private volatile float ballCountAlpha;
    private int level;
    private int failures;
    private int maxFailures;

    private final Paint PAINT = new Paint();

    private long chrono;

    public Game(Stage stage, String name, boolean isInitialScene) {
        super(stage, name, isInitialScene);
        PAINT.setAntiAlias(true);
        PAINT.setStyle(Paint.Style.FILL);
        PAINT.setTypeface(Typeface.createFromAsset(stage.getContext().getAssets(), "fuentes/digital-7.ttf"));
    }

    private void verNumPelotas() {
        ballCount = String.valueOf(balls.size());
        PAINT.getTextBounds(ballCount, 0, ballCount.length(), ballCountBounds);
        ballCountHeight = stage.getHeight() * .7f / ballCountBounds.height();
        ballCountAlpha = 255f;
        ballCountX = (stage.getWidth() / ballCountHeight - ballCountBounds.width()) / 2;
        ballCountY = ((stage.getHeight() / ballCountHeight - ballCountBounds.height()) / 2) - PAINT.getFontMetrics().ascent /*- PAINT.getFontMetrics().descent*/;
    }

    @Override
    public void sceneSet() {
        level = ((Start) stage.getScene("inicio")).getLevel();
        failures = 0;
        ballsAlpha = 0;
        maxFailures = (int) (level * 0.1f);
        synchronized (this) {
            balls.clear();
            for (int i = 0; i < level; i++)
                balls.add(makeBall());
        }
        verNumPelotas();
        chrono = System.nanoTime();
    }

    private Ball makeBall() {
        return new Ball(
                this,
                (int) (Math.random() * (stage.getHeight() * 0.15) + (stage.getHeight() * 0.05)),
                stage.getWidth() / 2f, // radio + (int) (Math.random() * (getWidth() - 2 * radio)),
                stage.getHeight() / 2f, // radio + (int) (Math.random() * (getHeight() - 2 * radio)),
                (int) (Math.random() * 600 + 30),
                (float) ((Math.random() * ANG2 + ANG1) + (ANG3 * Math.floor(Math.random() * 4)))
        );
    }

    @Override
    public synchronized boolean next(long lapse, boolean isTransitionActive) {
        if (!isTransitionActive) {
            for (Ball p : balls)
                p.move(lapse);
            removed.removeIf(ball -> ball.destroy(lapse));
            if (ballsAlpha < 255) {
                ballsAlpha += (lapse * 255) / ballCountTime;
                if (ballsAlpha > 255)
                    ballsAlpha = 255;
            }
            if (ballCountAlpha != 0) {
                ballCountAlpha -= lapse * 255f / ballCountTime;
                if (ballCountAlpha < 0)
                    ballCountAlpha = 0;
            }
        }
        return (failures > maxFailures || (ballCountAlpha == 0 && balls.isEmpty() && removed.isEmpty()));
    }

    @Override
    public void paint(Canvas canvas) {
        PAINT.setColor(Color.BLACK);
        canvas.drawRect(0, 0, stage.getWidth(), stage.getHeight(), PAINT);
//        canvas.drawColor(Color.DKGRAY);
        synchronized (this) {
            balls.forEach(p -> p.paint(canvas, (int) ballsAlpha));
            removed.forEach(p -> p.paintDestroy(canvas));
        }
        if (ballCountAlpha != 0) {
            PAINT.setColor(Color.RED);
            PAINT.setAlpha((int) ballCountAlpha);
            Matrix matrix = canvas.getMatrix();
            canvas.scale(ballCountHeight, ballCountHeight);
            canvas.drawText(ballCount, ballCountX, ballCountY, PAINT);
            canvas.setMatrix(matrix);
        }
    }

    @Override
    public void onTouch(MotionEvent event) {
        synchronized (this) {
            if (failures <= maxFailures && ballsAlpha == 255 && !balls.isEmpty() && event.getAction() == MotionEvent.ACTION_DOWN) {
                if (balls.getLast().in(event.getX(), event.getY())) {
                    removed.add(balls.removeLast());
                    if (balls.isEmpty()) {
                        chrono = System.nanoTime() - chrono;
//                        Scores scores = (Scores) stage.getScene("tiempos");
//                        scores.set(level, chrono);
//                        stage.setNextScene("tiempos", "desplazamiento", 150000000L);
                        showTimes();
                    }
                } else {
                    failures++;
                    balls.addFirst(makeBall());
                    verNumPelotas();
                    if (failures > maxFailures) {
                        final Line[] LINES = new Line[]{
                                new Line("Lo siento, has superado el número de fallos permitidos en este nivel.", Color.WHITE),
                                new Line("TOCA LA PANTALLA PARA INTENTARLO DE NUEVO", Color.WHITE)
                        };
                        Info info = (Info) stage.getScene("info");
                        info.reset(LINES, 0, "juego");
                        stage.setNextScene("info", "giro", 1000000000L);
                    }
                }
            }
        }
    }

    private String nanosToHMS(long nanos) {
        long hh = nanos / 3600000000000L;
        nanos %= 3600000000000L;
        long mm = nanos / 60000000000L;
        nanos %= 60000000000L;
        long ss = nanos / 1000000000L;
        long ms = nanos / 1000000L;
        return String.format("%d:%02d:%02d.%d", hh, mm, ss, ms);
    }

    private void save(String dificultad, long nanos) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(stage.getContext()).edit();
        editor.putLong(dificultad, nanos);
        editor.apply();
    }

    private void showTimes() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(stage.getContext());

        long bestSoftTime = preferences.getLong("FACIL", -1);
        if (level == 25) {
            if (bestSoftTime == -1) {
                save("FACIL", bestSoftTime = chrono);
            }
            else if (bestSoftTime > chrono) {
                save("FACIL", bestSoftTime = chrono);
            }
        }

        long bestMediumTime = preferences.getLong("MEDIO", -1);
        if (level == 50) {
            if (bestMediumTime == -1) {
                save("MEDIO", bestMediumTime = chrono);
            }
            else if (bestMediumTime > chrono) {
                save("MEDIO", bestMediumTime = chrono);
            }
        }

        long bestHardTime = preferences.getLong("DIFICIL", -1);
        if (level == 100) {
            if (bestHardTime == -1) {
                save("DIFICIL", bestHardTime = chrono);
            }
            else if (bestHardTime > chrono) {
                save("DIFICIL", bestHardTime = chrono);
            }
        }

        final Line[] LINES = new Line[]{
                new Line(">>>>>>>>>>>>>>>>>>>> TIEMPO: " + nanosToHMS(chrono) + " <<<<<<<<<<<<<<<<<<<<", Color.WHITE),
                new Line("", Color.BLACK),
                new Line("MEJORES TIEMPOS", Color.RED),
                new Line("FACIL: " + nanosToHMS(bestSoftTime), Color.WHITE),
                new Line("MEDIO: " + nanosToHMS(bestMediumTime), Color.WHITE),
                new Line("DIFICIL: " + nanosToHMS(bestHardTime), Color.WHITE)
        };
        Info info = (Info) stage.getScene("info");
        info.reset(LINES, 0, "inicio", "desplazamiento", 150000000L);
        stage.setNextScene("info", "desplazamiento", 150000000L);
    }

    @Override
    public void update() {
    }

    @Override
    public boolean onBackPressed() {
        stage.setScene("inicio", "giro", 1000000000f);
        return false;
    }
}
