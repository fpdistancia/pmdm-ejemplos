package dam.pmdm.bolaslocas.transiciones;

import android.graphics.Canvas;
import android.graphics.Matrix;

import dam.pmdm.bolaslocas.Escenario;
import dam.pmdm.bolaslocas.escenas.Escena;

public class Desplazamiento extends Transicion {

    private float distancia;

    public Desplazamiento(Escenario escenario, float tiempo) {
        super(escenario, tiempo);
    }

    @Override
    public void preparar(Escena entrante, Escena saliente) {
        super.preparar(entrante, saliente);
        distancia = escenario.getWidth();
    }

    @Override
    public boolean siguiente(long lapso) {
        saliente.siguiente(lapso);
        entrante.siguiente(lapso);
        if (distancia > 0) {
            distancia -= escenario.getWidth() * (lapso / tiempo);
            if (distancia < 0)
                distancia = 0;
        }
        return distancia == 0;
    }

    @Override
    public void dibujar(Canvas canvas) {
        Matrix matrix = canvas.getMatrix();
        canvas.translate(distancia, 0);
        entrante.dibujar(canvas);
        canvas.setMatrix(matrix);
//        canvas.translate(-(width - distancia), 0);
        canvas.translate(distancia - escenario.getWidth(), 0);
        saliente.dibujar(canvas);
    }
}
