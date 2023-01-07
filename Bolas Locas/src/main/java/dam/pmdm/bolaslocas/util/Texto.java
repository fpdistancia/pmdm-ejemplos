package dam.pmdm.bolaslocas.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class Texto {

    public static Bitmap crearAncho(String texto, Typeface typeface, float width) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setTypeface(typeface);
        Rect bounds = new Rect();
        paint.getTextBounds(texto, 0, texto.length(), bounds);
        float scale = width / bounds.width();
        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) (bounds.height() * scale), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(scale, scale);
        for (int i=texto.length(); i>0; i--) {
            paint.setColor(Colores.get());
            canvas.drawText(texto.substring(0, i), 0, -paint.getFontMetrics().ascent, paint);
        }
        return bitmap;
    }

    public static Bitmap crearAncho(String texto, Typeface typeface, float width, int color) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setTypeface(typeface);
        Rect bounds = new Rect();
        paint.getTextBounds(texto, 0, texto.length(), bounds);
        float scale = width / bounds.width();
        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) (bounds.height() * scale), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(scale, scale);
        paint.setColor(color);
        canvas.drawText(texto, 0, -paint.getFontMetrics().ascent, paint);
        return bitmap;
    }

    public static Bitmap crearAlto(String texto, Typeface typeface, float width, float height) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setTypeface(typeface);
        Rect bounds = new Rect();
        paint.getTextBounds(texto, 0, texto.length(), bounds);
        float scale = height / bounds.width();
        Bitmap bitmap = Bitmap.createBitmap((int) (bounds.width() * scale), (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(scale, scale);
        for (int i=texto.length(); i>0; i--) {
            paint.setColor(Colores.get());
            canvas.drawText(texto.substring(0, i), 0, -paint.getFontMetrics().ascent, paint);
        }
        return bitmap;
    }

    public static Bitmap crearAlto(String texto, Typeface typeface, float height, int color) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setTypeface(typeface);
        Rect bounds = new Rect();
        paint.getTextBounds(texto, 0, texto.length(), bounds);
        float scale = height / bounds.width();
        Bitmap bitmap = Bitmap.createBitmap((int) (bounds.width() * scale), (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(scale, scale);
        paint.setColor(color);
        canvas.drawText(texto, 0, -paint.getFontMetrics().ascent, paint);
        return bitmap;
    }


    public static Bitmap crearBotonDeAlto(String texto, Typeface typeface, float alto, int colorBorde) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setTypeface(typeface);
        Rect bounds = new Rect();
        paint.getTextBounds(texto, 0, texto.length(), bounds);
        float scale = alto * .8f / bounds.height();
        float ancho = (bounds.width() * scale) / .8f;
        Bitmap bitmap = Bitmap.createBitmap((int) ancho, (int) alto, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        int tx = (int) (((ancho / scale) - bounds.width()) / 2);
        int ty = (int) (((alto / scale) - bounds.height()) / 2);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(1, 1, bitmap.getWidth() - 2, bitmap.getHeight() - 2, 50, 50, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(colorBorde);
        canvas.drawRoundRect(1, 1, bitmap.getWidth() - 2, bitmap.getHeight() - 2, 50, 50, paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.scale(scale, scale);
        paint.setColor(Color.BLACK);
        canvas.drawText(texto, tx, -paint.getFontMetrics().ascent, paint);
        return bitmap;
    }

}
