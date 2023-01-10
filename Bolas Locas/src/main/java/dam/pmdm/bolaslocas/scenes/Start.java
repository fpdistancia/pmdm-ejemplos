package dam.pmdm.bolaslocas.scenes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import dam.pmdm.bolaslocas.scenes.info.Info;
import dam.pmdm.bolaslocas.scenes.info.Line;
import dam.pmdm.bolaslocas.util.Boton;
import dam.pmdm.bolaslocas.Stage;
import dam.pmdm.bolaslocas.R;
import dam.pmdm.bolaslocas.util.Texto;

public class Start extends Scene {

    private int level = 50;

    private final Typeface TYPEFACE1;
    private final Typeface TYPEFACE2;

    private Bitmap title;
    private float titleX;
    private float titleY;

    private Boton softButton;
    private Boton mediumButton;
    private Boton hardButton;
    private Boton helpButton;

    private SVG ballSVG;
    private float dx = 0;
    private float dy = 0;
    private float vx;
    private float vy;
    private float dir = 0;
    private float px;
    private float py;
    private float rotationAngle = -1;
    private final float angularVelocity = 100f;

    private final Paint PAINT = new Paint();

    public Start(Stage stage, String name, boolean isInitialScene) {
        super(stage, name, isInitialScene);
        TYPEFACE1 = Typeface.createFromAsset(stage.getContext().getAssets(), "fuentes/ThunderLord.ttf");
        TYPEFACE2 = Typeface.createFromAsset(stage.getContext().getAssets(), "fuentes/ComicNeueSansID.ttf");
    }

    @Override
    public void sceneSet() {
    }

    public int getLevel() {
        return level;
    }

    @Override
    public boolean next(long lapse, boolean isTransitionActive) {
        dir += (lapse * angularVelocity / 1000000000f) * rotationAngle;
        dx += lapse * vx / 1000000000f;
        dy += lapse * vy / 1000000000f;
        float dcha = dx + ballSVG.getDocumentWidth();
        float abajo = dy + ballSVG.getDocumentHeight();
        if (dx < 0) {
            dx = -dx;
            vx = -vx;

            if ((vy < 0 && rotationAngle > 0) || (vy > 0 && rotationAngle < 0))
                rotationAngle *= -1;

        }
        else if (dcha > width) {
            dx -= 2 * (dcha - width);
            vx = -vx;

            if ((vy > 0 && rotationAngle > 0) || (vy < 0 && rotationAngle < 0))
                rotationAngle *= -1;

        }
        if (dy < 0) {
            dy = -dy;
            vy = -vy;

            if ((vx > 0 && rotationAngle > 0) || (vx < 0 && rotationAngle < 0))
                rotationAngle *= -1;

        }
        else if (abajo > height) {
            dy -= 2 * (abajo - height);
            vy = -vy;

            if ((vx < 0 && rotationAngle > 0) || (vx > 0 && rotationAngle < 0))
                rotationAngle *= -1;

        }
        return false;
    }

    @Override
    public void paint(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, PAINT);
//        canvas.drawColor(Color.WHITE);
//        final DrawFilter filter = new PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG| Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG, 0);
//        canvas.setDrawFilter(filter);
        Matrix matrix = canvas.getMatrix();
        canvas.translate(dx, dy);
        canvas.rotate(dir, px, py);
        ballSVG.renderToCanvas(canvas);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(title, titleX, titleY, PAINT);
        softButton.draw(canvas);
        mediumButton.draw(canvas);
        hardButton.draw(canvas);
        helpButton.draw(canvas);
    }

    private void startGame(int level) {
//        final String [] LINES = new String[] {
//            String.format("ELIMINA %d BOLAS", level),
//            "TOCA LA PANTALLA PARA COMENZAR"
//        };
//        Info info = (Info) stage.getScene("info");
//        info.reset(LINES, 0, "juego");
//        stage.setScene("info", "giro", 1000000000f);
        stage.setScene("juego", "giro", 1000000000f);
    }

    @Override
    public void onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (softButton.touchIn(event.getX(), event.getY()))
                startGame(level = 25);
            else if (mediumButton.touchIn(event.getX(), event.getY()))
                startGame(level = 50);
            else if (hardButton.touchIn(event.getX(), event.getY()))
                startGame(level = 100);
            else if (helpButton.touchIn(event.getX(), event.getY())) {
                Info info = (Info) stage.getScene("info");
                String [] strings = stage.getContext().getResources().getStringArray(R.array.instrucciones);
                Line [] lines = new Line[strings.length];
                for (int i=0; i<strings.length; i++)
                    lines[i] = new Line(strings[i], Color.WHITE);
                info.reset(lines, 2, "inicio", "desplazamiento", 150000000f);
                stage.setScene("info", "desplazamiento", 150000000f);
            }
        }
    }

    @Override
    public void update() {
        try {
            title = Texto.crearAncho("Bolas Locas", TYPEFACE1, width * .7f);
            titleX = (width - title.getWidth()) / 2f;
            titleY = height * .07f;

            float rest = height - (titleY + title.getHeight());
            float buttonMargin = rest * .1f;
            float buttonHeight = (rest - (buttonMargin * 5f)) / 4f;
            float buttonY = titleY + title.getHeight() + buttonMargin;

            Bitmap bmp = Texto.crearBotonDeAlto("FACIL", TYPEFACE2, buttonHeight, Color.RED);
            softButton = new Boton(bmp, (width - bmp.getWidth()) / 2f, buttonY);
            bmp = Texto.crearBotonDeAlto("MEDIO", TYPEFACE2, buttonHeight, Color.RED);
            mediumButton = new Boton(bmp, (width - bmp.getWidth()) / 2f, buttonY + buttonMargin + buttonHeight);
            bmp = Texto.crearBotonDeAlto("DIFICIL", TYPEFACE2, buttonHeight, Color.RED);
            hardButton = new Boton(bmp, (width - bmp.getWidth()) / 2f, buttonY + (2 * buttonMargin) + (2 * buttonHeight));
            bmp = Texto.crearBotonDeAlto("INSTRUCCIONES", TYPEFACE2, buttonHeight, Color.RED);
            helpButton = new Boton(bmp, (width - bmp.getWidth()) / 2f, buttonY + (3 * buttonMargin) + (3 * buttonHeight));

            PAINT.setAntiAlias(true);
            PAINT.setColor(Color.WHITE);
            PAINT.setStyle(Paint.Style.FILL);

            ballSVG = SVG.getFromResource(stage.getContext().getResources(), R.raw.bolaloca);
            ballSVG.setDocumentWidth(height * .5f);
            ballSVG.setDocumentHeight(height * .5f);

            px = ballSVG.getDocumentWidth() / 2f;
            py = ballSVG.getDocumentHeight() / 2f;

            final float VLIN = px * (angularVelocity * (float) Math.PI / 180f);
            final float DIR = (float) Math.PI / 4f;
            vx = (float) Math.cos(DIR) * VLIN;
            vy = (float) Math.sin(DIR) * VLIN;
        } catch (SVGParseException e) {
            Log.e("START", e.getMessage());
        }
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

}
