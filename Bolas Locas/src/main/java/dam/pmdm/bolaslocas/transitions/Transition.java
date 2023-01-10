package dam.pmdm.bolaslocas.transitions;

import android.graphics.Canvas;

import dam.pmdm.bolaslocas.Stage;
import dam.pmdm.bolaslocas.scenes.Scene;

public abstract class Transition {

    protected Stage stage;
    protected Scene sceneIn;
    protected Scene sceneOut;
    protected float time;

    public Transition(Stage stage, String name) {
        this.stage = stage;
        stage.addTransicion(name, this);
    }

    public void setUp(Scene sceneIn, Scene scenOut, float time) {
        this.sceneIn = sceneIn;
        this.sceneOut = scenOut;
        this.time = time;
    }

    public Scene getSceneIn() {
        return sceneIn;
    }

    public boolean next(long lapso) {
        sceneOut.next(lapso, true);
        sceneIn.next(lapso, true);
        return false;
    }

    public abstract void paint(Canvas canvas);

}