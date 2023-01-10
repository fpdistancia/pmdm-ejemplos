package dam.pmdm.bolaslocas.transitions;

import android.graphics.Canvas;

import dam.pmdm.bolaslocas.Stage;
import dam.pmdm.bolaslocas.scenes.Scene;

public class Rotation extends Transition {

    private final float ANGLE = 360f;
    private float rotationAngle;
    private float scale;
    private float px;
    private float py;
    private float dx;
    private float dy;

    public Rotation(Stage stage, String name) {
        super(stage, name);
    }

    @Override
    public void setUp(Scene sceneIn, Scene scenOut, float time) {
        super.setUp(sceneIn, scenOut, time);
        rotationAngle = 0;
        scale = 1;
        px = stage.getWidth() / 2f;
        py = stage.getHeight() / 2f;
    }

    @Override
    public boolean next(long lapso) {
        super.next(lapso);
        scale -= lapso / time;
        float factor = (1f - scale) / (2f * scale);
        dx = stage.getWidth() * factor;
        dy = stage.getHeight() * factor;
        rotationAngle += lapso * ANGLE / time;
        if (rotationAngle > ANGLE)
            rotationAngle = ANGLE;
        return rotationAngle == ANGLE;
    }

    @Override
    public void paint(Canvas canvas) {
        sceneIn.paint(canvas);
        canvas.scale(scale, scale);
        canvas.translate(dx, dy);
        canvas.rotate(rotationAngle, px, py);
        sceneOut.paint(canvas);
    }
}
