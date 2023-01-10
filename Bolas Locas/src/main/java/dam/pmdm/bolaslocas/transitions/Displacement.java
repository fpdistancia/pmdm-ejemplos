package dam.pmdm.bolaslocas.transitions;

import android.graphics.Canvas;
import android.graphics.Matrix;

import dam.pmdm.bolaslocas.Stage;
import dam.pmdm.bolaslocas.scenes.Scene;

public class Displacement extends Transition {

    private float distance;

    public Displacement(Stage stage, String name) {
        super(stage, name);
    }

    @Override
    public void setUp(Scene sceneIn, Scene scenOut, float time) {
        super.setUp(sceneIn, scenOut, time);
        distance = stage.getWidth();
    }

    @Override
    public boolean next(long lapso) {
        super.next(lapso);
        if (distance > 0) {
            distance -= stage.getWidth() * (lapso / time);
            if (distance < 0)
                distance = 0;
        }
        return distance == 0;
    }

    @Override
    public void paint(Canvas canvas) {
        Matrix matrix = canvas.getMatrix();
        canvas.translate(distance, 0);
        sceneIn.paint(canvas);
        canvas.setMatrix(matrix);
        canvas.translate(distance - stage.getWidth(), 0);
        sceneOut.paint(canvas);
    }

}
