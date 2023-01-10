package dam.pmdm.bolaslocas.scenes;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import dam.pmdm.bolaslocas.Stage;

public abstract  class Scene {

    protected final Stage stage;
    protected final String name;
    private Integer format;
    protected int width;
    protected int height;

    public Scene(Stage stage, String name, boolean isInitialScene) {
        this.stage = stage;
        this.name = name;
        stage.addScene(this, isInitialScene);
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return stage.getWidth();
    }

    public int getHeight() {
        return stage.getHeight();
    }

    public abstract boolean next(long lapse, boolean isTransitionActive);

    public abstract void paint(Canvas canvas);

    public abstract void sceneSet();

    public abstract void onTouch(MotionEvent event);

    public abstract void update();

    public void surfaceChanged(int format, int width, int height) {
        if (this.format == null || this.format != format || this.width != width || this.height != height) {
            Log.i(String.format("Escena \"%s\"", name), "ACTUALIZADA");
            this.format = format;
            this.width = width;
            this.height = height;
            update();
        }
    }

    public abstract boolean onBackPressed();

}
