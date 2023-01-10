package dam.pmdm.bolaslocas.scenes;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

import dam.pmdm.bolaslocas.Stage;

public class Scores extends Scene {

    private String currentTime;
    private String bestSoftTime;
    private String bestMediumTime;
    private String bestHardTime;

    private Paint PAINT = new Paint();

    public Scores(Stage stage, String name, boolean isInitialScene) {
        super(stage, name, isInitialScene);
        PAINT.setAntiAlias(true);
        PAINT.setStyle(Paint.Style.FILL);
    }

    public void set(int level, long nanos) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(stage.getContext());
        currentTime = nanosToString(nanos);
        long bestSoftTime = preferences.getLong("FACIL", -1);
        if (level == 25) {
            if (bestSoftTime == -1) {
                save("FACIL", bestSoftTime = nanos);
            }
            else if (bestSoftTime > nanos) {
                save("FACIL", bestSoftTime = nanos);
            }
        }
        this.bestSoftTime = nanosToString(bestSoftTime);
        long bestMediumTime = preferences.getLong("MEDIO", -1);
        if (level == 50) {
            if (bestMediumTime == -1) {
                save("MEDIO", bestMediumTime = nanos);
            }
            else if (bestMediumTime > nanos) {
                save("MEDIO", bestMediumTime = nanos);
            }
        }
        this.bestMediumTime = nanosToString(bestMediumTime);
        long bestHardTime = preferences.getLong("DIFICIL", -1);
        if (level == 100) {
            if (bestHardTime == -1) {
                save("DIFICIL", bestHardTime = nanos);
            }
            else if (bestHardTime > nanos) {
                save("DIFICIL", bestHardTime = nanos);
            }
        }
        this.bestHardTime = nanosToString(bestHardTime);
    }

    private String nanosToString(long nanos) {
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


    @Override
    public void sceneSet() {
    }

    @Override
    public boolean next(long lapse, boolean isTransitionActive) {
        return true;
    }

    @Override
    public void paint(Canvas canvas) {
        PAINT.setColor(Color.BLACK);
        canvas.drawRect(0, 0, stage.getWidth(), stage.getHeight(), PAINT);
        canvas.scale(3, 3);
        PAINT.setColor(Color.WHITE);
        canvas.drawText(currentTime, 100, 10, PAINT);
        canvas.drawText(bestSoftTime, 100, 30, PAINT);
        canvas.drawText(bestMediumTime, 100, 50, PAINT);
        canvas.drawText(bestHardTime, 100, 70, PAINT);;
    }

    @Override
    public void onTouch(MotionEvent event) {
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
