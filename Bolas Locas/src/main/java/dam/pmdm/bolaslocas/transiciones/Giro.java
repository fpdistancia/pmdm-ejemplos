package dam.pmdm.bolaslocas.transiciones;

import android.graphics.Canvas;

import dam.pmdm.bolaslocas.Escenario;
import dam.pmdm.bolaslocas.escenas.Escena;

public class Giro extends Transicion {

    private final float ANGULO = 360f;
    private float angulo;
    private float escala;

    public Giro(Escenario escenario, float tiempo) {
        super(escenario, tiempo);
    }

    @Override
    public void preparar(Escena entrante, Escena saliente) {
        super.preparar(entrante, saliente);
        angulo = 0;
        escala = 1;
    }

    @Override
    public boolean siguiente(long lapso) {
        angulo += lapso * ANGULO / tiempo;
        escala -= lapso / tiempo;
        if (angulo > ANGULO)
            angulo = ANGULO;
        entrante.siguiente(lapso);
        saliente.siguiente(lapso);
        return angulo == ANGULO;
    }

    @Override
    public void dibujar(Canvas canvas) {
        entrante.dibujar(canvas);
        float dx = (escenario.getWidth() - (escenario.getWidth() * escala)) / 2;
        float dy = (escenario.getHeight() - (escenario.getHeight() * escala)) / 2;
        canvas.translate(dx, dy);
        canvas.scale(escala, escala);
        canvas.rotate(angulo, escenario.getWidth() / 2f, escenario.getHeight() / 2f);

        saliente.dibujar(canvas);
    }
}
