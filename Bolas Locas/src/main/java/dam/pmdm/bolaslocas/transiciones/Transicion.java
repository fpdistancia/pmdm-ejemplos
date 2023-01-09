package dam.pmdm.bolaslocas.transiciones;

import android.graphics.Canvas;

import dam.pmdm.bolaslocas.Escenario;
import dam.pmdm.bolaslocas.escenas.Escena;

public abstract class Transicion {

    protected Escenario escenario;
    protected Escena entrante;
    protected Escena saliente;
    protected float tiempo;

    public Transicion(Escenario escenario, float tiempo) {
        this.escenario = escenario;
        this.tiempo = tiempo;
    }

    public void preparar(Escena entrante, Escena saliente) {
        this.entrante = entrante;
        this.saliente = saliente;
    }

    public Escena getEntrante() {
        return entrante;
    }

    public abstract boolean siguiente(long lapso);

    public abstract void dibujar(Canvas canvas);

}