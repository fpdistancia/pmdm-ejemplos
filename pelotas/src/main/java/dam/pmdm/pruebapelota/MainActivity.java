package dam.pmdm.pruebapelota;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MainActivity extends Activity {

    private Lienzo juego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // This work only for android 4.4+
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            final View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(flags);
            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won' hide
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                    decorView.setSystemUiVisibility(flags);
            });
        }

        setContentView(juego = new Lienzo(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        juego.reanudar();
        Toast.makeText(this, "RESUME", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        juego.pausar();
        Toast.makeText(this, "PAUSE", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(this, "SAVE INSTANCE", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Toast.makeText(this, "RESTORE INSTANCE", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(this, "CONFIGURATION CHANGED", Toast.LENGTH_SHORT).show();
    }

}