package dam.pmdm.bolaslocas.escenas;

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

import dam.pmdm.bolaslocas.Escenario;

public class Info extends Escena {

    private static class Linea {
        String texto;
        float width;
        float height;

        public Linea(String texto, float width, float height) {
            this.texto = texto;
            this.width = width;
            this.height = height;
        }
    }

    private final Paint PAINT = new Paint();
    private float escala;
    private float y;
    private float inter;
    private float width;
    private float height;
    private float lineaH;
    private float sepUltima;
    private final Rect bounds = new Rect();

    String escena;
    String transicion;

    private final List<Linea> lineas = new LinkedList<>();

    public Info(Escenario escenario, String nombre, boolean inicial) {
        super(escenario, nombre, inicial);
        PAINT.setAntiAlias(true);
        PAINT.setColor(Color.BLACK);
        PAINT.setStyle(Paint.Style.FILL);
        PAINT.setTypeface(Typeface.createFromAsset(escenario.getContext().getAssets(), "fuentes/ComicNeueSansID.ttf"));
    }

    public void reset(String [] strings, int sepUltima, String escena, String transicion) {
        reset(strings, sepUltima, escena);
        this.transicion = transicion;
    }

    public void reset(String [] strings, int sepUltima, String escena) {
        transicion = null;
        this.escena = escena;
        float anchoMax = 0;
        lineaH = 0;
        lineas.clear();
        for (String linea: strings) {
            PAINT.getTextBounds(linea, 0, linea.length(), bounds);
            lineas.add(new Linea(linea, bounds.width(), bounds.height()));
            if (anchoMax < bounds.width())
                anchoMax = bounds.width();
            if (lineaH < bounds.height())
                lineaH = bounds.height();
        }
        escala = escenario.getWidth() * .9f / anchoMax;
        inter = lineaH * .7f;
        width = escenario.getWidth() / escala;
        height = escenario.getHeight() / escala;
        this.sepUltima = sepUltima * (lineaH + inter);
        y = (height - ((strings.length * lineaH) + ((strings.length - 1) * inter) + this.sepUltima)) / 2f - PAINT.getFontMetrics().ascent;;
    }

    @Override
    public void iniciar() {
    }

    @Override
    public boolean siguiente(long lapso) {
        return true;
    }

    @Override
    public void dibujar(Canvas canvas) {
        PAINT.setColor(Color.BLACK);
        canvas.drawRect(0, 0, width * escala, height * escala, PAINT);
        PAINT.setColor(Color.WHITE);
        Matrix matrix = canvas.getMatrix();
        canvas.scale(escala, escala);
        float x, y = this.y;
        Iterator<Linea> i = lineas.listIterator();
        while (i.hasNext()) {
            Linea l = i.next();
            x = (width - l.width) / 2;
            if (!i.hasNext())
                y += sepUltima;
            canvas.drawText(l.texto, x, y, PAINT);
            y += l.height + inter;
        }
        canvas.setMatrix(matrix);
    }

    @Override
    public void onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (transicion != null)
                escenario.setEscena(escena, transicion);
            else
                escenario.setEscena(escena);
        }
    }

    @Override
    public void actualizar(int format, int ancho, int alto) {

    }

    @Override
    public boolean onBackPressed() {
        escenario.setEscena("inicio", "desplazamiento");
        return false;
    }

}
