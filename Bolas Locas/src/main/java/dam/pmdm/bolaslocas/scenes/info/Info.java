package dam.pmdm.bolaslocas.scenes.info;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dam.pmdm.bolaslocas.Stage;
import dam.pmdm.bolaslocas.scenes.Scene;

public class Info extends Scene {

    private final Paint PAINT = new Paint();
    private float scale;
    private float y;
    private float inter;
    private float width;
    private float height;
    private float maxHeight;
    private float lastLineDistance;
    private final Rect bounds = new Rect();

    private String scene;
    private String transition;
    private float time;

    private Line [] lines;

    public Info(Stage stage, String name, boolean isInitialScene) {
        super(stage, name, isInitialScene);
        PAINT.setAntiAlias(true);
        PAINT.setColor(Color.BLACK);
        PAINT.setStyle(Paint.Style.FILL);
        PAINT.setTypeface(Typeface.createFromAsset(stage.getContext().getAssets(), "fuentes/ComicNeueSansID.ttf"));
    }

    public void reset(Line [] lines, int sepUltima, String escena, String transicion, float tiempo) {
        reset(lines, sepUltima, escena);
        this.transition = transicion;
        this.time = tiempo;
    }

    public void reset(Line [] lines, int sepUltima, String escena) {
        transition = null;
        this.scene = escena;
        float maxWidth = 0;
        maxHeight = 0;
        this.lines = lines;
        for (Line line: lines) {
            PAINT.getTextBounds(line.getText(), 0, line.getText().length(), bounds);
            line.setBounds(bounds.width(), bounds.height());
            if (maxWidth < bounds.width())
                maxWidth = bounds.width();
            if (maxHeight < bounds.height())
                maxHeight = bounds.height();
        }
        scale = stage.getWidth() * .9f / maxWidth;
        inter = maxHeight * .7f;
        width = stage.getWidth() / scale;
        height = stage.getHeight() / scale;
        this.lastLineDistance = sepUltima * (maxHeight + inter);
        y = (height - ((lines.length * maxHeight) + ((lines.length - 1) * inter) + this.lastLineDistance)) / 2f - PAINT.getFontMetrics().ascent;;
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
        canvas.drawRect(0, 0, width * scale, height * scale, PAINT);
        Matrix matrix = canvas.getMatrix();
        canvas.scale(scale, scale);
        float x, y = this.y;
        for (int i=0; i<lines.length; i++) {
            x = (width - lines[i].getWidth()) / 2;
            if (i == lines.length - 1)
                y += lastLineDistance;
            PAINT.setColor(lines[i].getColor());
            canvas.drawText(lines[i].getText(), x, y, PAINT);
            y += lines[i].getHeight() + inter;
        }
        canvas.setMatrix(matrix);
    }

    @Override
    public void onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (transition != null)
                stage.setScene(scene, transition, time);
            else
                stage.setScene(scene);
        }
    }

    @Override
    public void update() {
    }

    @Override
    public boolean onBackPressed() {
        stage.setScene("inicio", "desplazamiento", 150000000f);
        return false;
    }

}
