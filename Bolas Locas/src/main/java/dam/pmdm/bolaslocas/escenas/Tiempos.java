package dam.pmdm.bolaslocas.escenas;

import android.graphics.Canvas;
import android.view.MotionEvent;

import dam.pmdm.bolaslocas.Escenario;

public class Tiempos extends Escena {

    public Tiempos(Escenario escenario, String nombre, boolean inicial) {
        super(escenario, nombre, inicial);
    }

    @Override
    public void iniciar() {
    }

    @Override
    public boolean siguiente(long lapso) {
        return false;
    }

    @Override
    public void dibujar(Canvas canvas) {

    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void actualizar(int format, int ancho, int alto) {

    }

    @Override
    public boolean onBackPressed() {
        escenario.setEscena("inicio", "giro");
        return false;
    }
}
