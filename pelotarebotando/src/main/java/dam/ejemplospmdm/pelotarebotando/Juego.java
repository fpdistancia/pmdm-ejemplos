package dam.ejemplospmdm.pelotarebotando;

public class Juego implements Runnable {

    Thread thread;
    Pelota pelota;

    public Juego(Lienzo lienzo) {
        pelota = new Pelota(10, lienzo.getWidth() / 2d, lienzo.getHeight() / 2d, 50d, 45d, lienzo);
    }

    public void comenzar() {

    }

    public void detener() {

    }

    @Override
    public void run() {

    }

}
