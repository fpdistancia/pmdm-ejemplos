package dam.ejemplospmdm.pelotarebotando;

public class Pelota {

    double radio;
    double x;
    double y;
    double vx;
    double vy;

    Lienzo lienzo;

    public Pelota (double radio, double x, double y, double velocidad, double direccion, Lienzo lienzo) {
        this.x = x - radio;
        this.y = y - radio;
        vx = velocidad * Math.cos(direccion);
        vy = velocidad * Math.sin(direccion);
        this.lienzo = lienzo;
    }

    public void mover(long lapso) {
        x += lapso * vx / 1000000000d;
        y += lapso * vy / 1000000000d;
    }

}
