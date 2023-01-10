package dam.pmdm.bolaslocas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import dam.pmdm.bolaslocas.scenes.info.Info;
import dam.pmdm.bolaslocas.scenes.game.Game;
import dam.pmdm.bolaslocas.scenes.Start;
import dam.pmdm.bolaslocas.scenes.Scores;
import dam.pmdm.bolaslocas.transitions.Displacement;
import dam.pmdm.bolaslocas.transitions.Rotation;

public class MainActivity extends AppCompatActivity {

    private Stage stage;

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
        setContentView(stage = new Stage(this));
        new Start(stage, "inicio", true);
        new Game(stage, "juego", false);
        new Info(stage, "info", false);
        new Scores(stage, "tiempos", false);
        new Displacement(stage, "desplazamiento");
        new Rotation(stage, "giro");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ACTIVITY", "PAUSED");
        stage.detenerGameLoop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
       if (stage.onBackPressed())
           super.onBackPressed();
    }
}