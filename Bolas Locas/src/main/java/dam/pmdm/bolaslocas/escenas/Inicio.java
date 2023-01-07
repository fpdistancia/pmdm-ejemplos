package dam.pmdm.bolaslocas.escenas;

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

import dam.pmdm.bolaslocas.util.Boton;
import dam.pmdm.bolaslocas.Escenario;
import dam.pmdm.bolaslocas.R;
import dam.pmdm.bolaslocas.util.Texto;

public class Inicio extends Escena {

    private int dificultad = 50;

    private Bitmap titulo;
    private float tx;
    private float ty;

    private Boton botonFacil;
    private Boton botonMedio;
    private Boton botonDificil;
    private Boton botonInstrucciones;

    private SVG bola;
    private float dx = 0;
    private float dy = 0;
    private float vx;
    private float vy;
    private float ang = 0;
    private float px;
    private float py;
    private float rot = -1;
    private final float VANG = 100f;

    private final Paint PAINT = new Paint();

    public Inicio(Escenario escenario, String nombre, boolean inicial) {
        super(escenario, nombre, inicial);
        try {
            final Typeface TYPEFACE1 = Typeface.createFromAsset(escenario.getContext().getAssets(), "fuentes/ThunderLord.ttf");
            final Typeface TYPEFACE2 = Typeface.createFromAsset(escenario.getContext().getAssets(), "fuentes/ComicNeueSansID.ttf");

            titulo = Texto.crearAncho("Bolas Locas", TYPEFACE1, escenario.getWidth() * .7f);
            tx = (escenario.getWidth() - titulo.getWidth()) / 2f;
            ty = escenario.getHeight() * .07f;

            float resto = escenario.getHeight() - (ty + titulo.getHeight());
            float sepBotones = resto * .1f;
            float altoBotones = (resto - (sepBotones * 5f)) / 4f;
            float by = ty + titulo.getHeight() + sepBotones;

            Bitmap bmp = Texto.crearBotonDeAlto("FACIL", TYPEFACE2, altoBotones, Color.RED);
            botonFacil = new Boton(bmp, (escenario.getWidth() - bmp.getWidth()) / 2f, by);
            bmp = Texto.crearBotonDeAlto("MEDIO", TYPEFACE2, altoBotones, Color.RED);
            botonMedio = new Boton(bmp, (escenario.getWidth() - bmp.getWidth()) / 2f, by + sepBotones + altoBotones);
            bmp = Texto.crearBotonDeAlto("DIFICIL", TYPEFACE2, altoBotones, Color.RED);
            botonDificil = new Boton(bmp, (escenario.getWidth() - bmp.getWidth()) / 2f, by + (2 * sepBotones) + (2 * altoBotones));
            bmp = Texto.crearBotonDeAlto("INSTRUCCIONES", TYPEFACE2, altoBotones, Color.RED);
            botonInstrucciones = new Boton(bmp, (escenario.getWidth() - bmp.getWidth()) / 2f, by + (3 * sepBotones) + (3 * altoBotones));

            PAINT.setAntiAlias(true);
            PAINT.setColor(Color.WHITE);
            PAINT.setStyle(Paint.Style.FILL);

            bola = SVG.getFromResource(escenario.getContext().getResources(), R.raw.bolaloca);
            bola.setDocumentWidth(escenario.getHeight() * .5f);
            bola.setDocumentHeight(escenario.getHeight() * .5f);

            px = bola.getDocumentWidth() / 2f;
            py = bola.getDocumentHeight() / 2f;

            final float VLIN = px * (VANG * (float) Math.PI / 180f);
            final float DIR = (float) Math.PI / 4f;
            vx = (float) Math.cos(DIR) * VLIN;
            vy = (float) Math.sin(DIR) * VLIN;
        } catch (SVGParseException e) {
            Log.e("MENU", e.getMessage());
        }
    }

    @Override
    public void iniciar() {
    }

    public int getDificultad() {
        return dificultad;
    }

    @Override
    public boolean siguiente(long lapso) {
        ang += (lapso * VANG / 1000000000f) * rot;
        dx += lapso * vx / 1000000000f;
        dy += lapso * vy / 1000000000f;
        float dcha = dx + bola.getDocumentWidth();
        float abajo = dy + bola.getDocumentHeight();
        if (dx < 0) {
            dx = -dx;
            vx = -vx;

            if ((vy < 0 && rot > 0) || (vy > 0 && rot < 0))
                rot *= -1;

        }
        else if (dcha > escenario.getWidth()) {
            dx -= 2 * (dcha - escenario.getWidth());
            vx = -vx;

            if ((vy > 0 && rot > 0) || (vy < 0 && rot < 0))
                rot *= -1;

        }
        if (dy < 0) {
            dy = -dy;
            vy = -vy;

            if ((vx > 0 && rot > 0) || (vx < 0 && rot < 0))
                rot *= -1;

        }
        else if (abajo > escenario.getHeight()) {
            dy -= 2 * (abajo - escenario.getHeight());
            vy = -vy;

            if ((vx < 0 && rot > 0) || (vx > 0 && rot < 0))
                rot *= -1;

        }
        return false;
    }

    @Override
    public void dibujar(Canvas canvas) {
        canvas.drawRect(0, 0, escenario.getWidth(), escenario.getHeight(), PAINT);
//        canvas.drawColor(Color.WHITE);
//        final DrawFilter filter = new PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG| Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG, 0);
//        canvas.setDrawFilter(filter);
        Matrix matrix = canvas.getMatrix();
        canvas.translate(dx, dy);
        canvas.rotate(ang, px, py);
        bola.renderToCanvas(canvas);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(titulo, tx, ty, PAINT);
        botonFacil.draw(canvas);
        botonMedio.draw(canvas);
        botonDificil.draw(canvas);
        botonInstrucciones.draw(canvas);
    }

    private void iniciarPartida(int dificultad) {
        final String [] LINEAS = new String[] {
            String.format("ELIMINA %d BOLAS", dificultad),
            "TOCA LA PANTALLA PARA COMENZAR"
        };
        Info info = (Info) escenario.getEscena("info");
        info.reset(LINEAS, 0, "juego");
        escenario.setEscena("info", "giro");
    }



    @Override
    public void onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (botonFacil.touchIn(event.getX(), event.getY()))
                iniciarPartida(dificultad = 25);
            else if (botonMedio.touchIn(event.getX(), event.getY()))
                iniciarPartida(dificultad = 50);
            else if (botonDificil.touchIn(event.getX(), event.getY()))
                iniciarPartida(dificultad = 100);
            else if (botonInstrucciones.touchIn(event.getX(), event.getY())) {
                Info info = (Info) escenario.getEscena("info");
                info.reset(escenario.getContext().getResources().getStringArray(R.array.instrucciones), 2, "inicio", "desplazamiento");
                escenario.setEscena("info", "desplazamiento");
            }
        }
    }

    @Override
    public void actualizar(int format, int ancho, int alto) {

    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

}
