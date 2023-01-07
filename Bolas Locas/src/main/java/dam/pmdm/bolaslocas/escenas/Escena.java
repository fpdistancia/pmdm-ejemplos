package dam.pmdm.bolaslocas.escenas;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import dam.pmdm.bolaslocas.Escenario;

public abstract  class Escena {

    protected final Escenario escenario;
    protected final String nombre;

    public Escena(Escenario escenario, String nombre, boolean inicial) {
        this.escenario = escenario;
        this.nombre = nombre;
        escenario.addEscena(this, inicial);
    }

    public String getNombre() {
        return nombre;
    }

    public int getWidth() {
        return escenario.getWidth();
    }

    public int getHeight() {
        return escenario.getHeight();
    }

    public abstract void iniciar();

    public abstract boolean siguiente(long lapso);

    public abstract void dibujar(Canvas canvas);

    public abstract void onTouch(MotionEvent event);

    public abstract void actualizar(int format, int ancho, int alto);

    public abstract boolean onBackPressed();

}
