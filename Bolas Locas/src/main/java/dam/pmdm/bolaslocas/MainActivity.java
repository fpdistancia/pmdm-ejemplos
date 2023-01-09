package dam.pmdm.bolaslocas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import dam.pmdm.bolaslocas.escenas.Info;
import dam.pmdm.bolaslocas.escenas.Juego;
import dam.pmdm.bolaslocas.escenas.Inicio;
import dam.pmdm.bolaslocas.escenas.Tiempos;
import dam.pmdm.bolaslocas.transiciones.Desplazamiento;
import dam.pmdm.bolaslocas.transiciones.Giro;

public class MainActivity extends AppCompatActivity {

    private Escenario escenario;

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
        setContentView(escenario = new Escenario(this));
        new Inicio(escenario, "inicio", true);
        new Juego(escenario, "juego", false);
        new Info(escenario, "info", false);
        new Tiempos(escenario, "tiempos", false);
        escenario.addTransicion("desplazamiento", new Desplazamiento(escenario, 500000000f));
        escenario.addTransicion("giro", new Giro(escenario, 1500000000f));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ACTIVITY", "PAUSED");
        escenario.detenerGameLoop();
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
    }

    @Override
    public void onBackPressed() {
       if (escenario.onBackPressed())
           super.onBackPressed();
    }
}