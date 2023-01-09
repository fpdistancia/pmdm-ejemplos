package dam.pmdm.bolaslocas.escenas;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import dam.pmdm.bolaslocas.Escenario;
import dam.pmdm.bolaslocas.Pelota;

public class Juego extends Escena {

    private static final double ANG1 = 25 * Math.PI / 180;
    private static final double ANG2 = 40 * Math.PI / 180;
    private static final double ANG3 = Math.PI / 2;

    private final Deque<Pelota> pelotas = new LinkedList<>();
    private final List<Pelota> eliminadas = new LinkedList<>();

    private String numPelotas;
    private final Rect numPelotasB = new Rect();
    private float numPelotasX;
    private float numPelotasY;
    private float numPelotasS;
    private final float numPelotasT = 2000000000f;
    private volatile float numPelotasA;

    private final Paint PAINT = new Paint();

    public Juego(Escenario escenario, String nombre, boolean inicial) {
        super(escenario, nombre, inicial);
        PAINT.setAntiAlias(true);
        PAINT.setStyle(Paint.Style.FILL);
        PAINT.setTypeface(Typeface.createFromAsset(escenario.getContext().getAssets(), "fuentes/digital-7.ttf"));
    }

    private void verNumPelotas() {
        numPelotas = String.valueOf(pelotas.size());
        PAINT.getTextBounds(numPelotas, 0, numPelotas.length(), numPelotasB);
        numPelotasS = escenario.getHeight() * .7f / numPelotasB.height();
        numPelotasA = 255f;
        numPelotasX = (escenario.getWidth() / numPelotasS - numPelotasB.width()) / 2;
        numPelotasY = ((escenario.getHeight() / numPelotasS - numPelotasB.height()) / 2) - PAINT.getFontMetrics().ascent /*- PAINT.getFontMetrics().descent*/;
    }

    @Override
    public void iniciar() {
        int numBolas = ((Inicio) escenario.getEscena("inicio")).getDificultad();
        synchronized (this) {
            pelotas.clear();
            for (int i = 0; i < numBolas; i++)
                pelotas.add(crearPelota());
        }
    }

    private Pelota crearPelota() {
        return new Pelota(
                this,
                (int) (Math.random() * (escenario.getHeight() * 0.15) + (escenario.getHeight() * 0.05)),
                escenario.getWidth() / 2f, // radio + (int) (Math.random() * (getWidth() - 2 * radio)),
                escenario.getHeight() / 2f, // radio + (int) (Math.random() * (getHeight() - 2 * radio)),
                (int) (Math.random() * 600 + 30),
                (float) ((Math.random() * ANG2 + ANG1) + (ANG3 * Math.floor(Math.random() * 4)))
        );
    }

    @Override
    public synchronized boolean siguiente(long lapso) {
        for (Pelota p : pelotas)
            p.mover(lapso);
        eliminadas.removeIf(pelota -> pelota.disipar(lapso));
        if (numPelotasA != 0) {
            numPelotasA -= lapso * 255f / numPelotasT;
            if (numPelotasA < 0)
                numPelotasA = 0;
        }
        return numPelotasA == 0 && pelotas.isEmpty() && eliminadas.isEmpty();
    }

    @Override
    public void dibujar(Canvas canvas) {
        PAINT.setColor(Color.BLACK);
        canvas.drawRect(0, 0, escenario.getWidth(), escenario.getHeight(), PAINT);
//        canvas.drawColor(Color.DKGRAY);
        synchronized (this) {
            pelotas.forEach(p -> p.dibujar(canvas));
            eliminadas.forEach(p -> p.dibujar(canvas));
        }
        if (numPelotasA != 0) {
            PAINT.setColor(Color.RED);
            PAINT.setAlpha((int) numPelotasA);
            Matrix matrix = canvas.getMatrix();
            canvas.scale(numPelotasS, numPelotasS);
            canvas.drawText(numPelotas, numPelotasX, numPelotasY, PAINT);
            canvas.setMatrix(matrix);
        }
    }

    @Override
    public void onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (this) {
                if (!pelotas.isEmpty()) {
                    if (pelotas.getLast().dentro(event.getX(), event.getY()))
                        eliminadas.add(pelotas.removeLast());
                    else {
                        pelotas.addFirst(crearPelota());
                        verNumPelotas();
                    }
                }
            }
        }
    }

    @Override
    public void actualizar() {
    }

    @Override
    public boolean onBackPressed() {
        escenario.setEscena("inicio", "giro");
        return false;
    }
}
