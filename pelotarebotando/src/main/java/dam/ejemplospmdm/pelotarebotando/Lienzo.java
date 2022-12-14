package dam.ejemplospmdm.pelotarebotando;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class Lienzo extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Juego juego;

    private Bitmap bmp;
    private Bitmap bmpLandscape;
    private Bitmap bmpPortrait;
    private Canvas canvas;
    private Canvas canvasLandscape;
    private Canvas canvasPortrait;

    public Lienzo(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        holder = getHolder();
        juego = new Juego(this);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
//                scale = (float) width / 532f;
                if (bmpPortrait == null) {
                    bmpPortrait = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    canvasPortrait = new Canvas(bmpPortrait);
                }
                bmp = bmpPortrait;
                canvas = canvasPortrait;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
//                scale = (float) height / 684f;
                if (bmpLandscape == null) {
                    bmpLandscape = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    canvasLandscape = new Canvas(bmpLandscape);
                }
                bmp = bmpLandscape;
                canvas = canvasLandscape;
        }
        juego.comenzar();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        juego.detener();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void render() {
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas();
            synchronized (holder) {
                canvas.drawBitmap(bmp, 0, 0, null);
            }
        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), e.getMessage(), e);
        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }
    }

}
