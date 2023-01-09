package dam.pmdm.bolaslocas.escenas;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import dam.pmdm.bolaslocas.Escenario;

public abstract  class Escena {

    protected final Escenario escenario;
    protected final String nombre;
    private Integer format;
    protected int width;
    protected int height;

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

    public abstract void actualizar();

    public void surfaceChanged(int format, int width, int height) {
        if (this.format == null || this.format != format || this.width != width || this.height != height) {
            Log.i(String.format("Escena \"%s\"", nombre), "ACTUALIZADA");
            this.format = format;
            this.width = width;
            this.height = height;
            actualizar();
        }
    }

    public abstract boolean onBackPressed();

}
