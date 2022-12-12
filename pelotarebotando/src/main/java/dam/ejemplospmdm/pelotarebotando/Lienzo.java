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

    SurfaceHolder holder;

    Bitmap bmp;
    Bitmap bmpLandscape;
    Bitmap bmpPortrait;
    Canvas canvas;
    Canvas canvasLandscape;
    Canvas canvasPortrait;

    public Lienzo(Context context) {
        super(context);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        holder = getHolder();
//        iniciar juego
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
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void render() {
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
