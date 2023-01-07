package dam.ejemplospmdm.handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ProgressBar pb;
    private HiloHandler hiloHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = findViewById(R.id.progressBar);
        hiloHandler = new HiloHandler();
        hiloHandler.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "RESUME", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "RESTART", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "STOP", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "DESTROY", Toast.LENGTH_SHORT).show();
        hiloHandler.getHandler().getLooper().quit();
        try {
            hiloHandler.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSup:
                pb.setProgress(0);
                hiloHandler.getHandler().post(this::tarea);
                break;
            case R.id.buttonInf:
                Toast.makeText(this, "Hola Mundo!", Toast.LENGTH_SHORT).show();
        }
    }

    public void tarea() {
        for (int i=0; i<100; i++) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pb.post(new Runnable() {
                @Override
                public void run() {
                    pb.incrementProgressBy(1);
                }
            });
        }
    }

    private static class HiloHandler extends Thread {

        Handler handler;

        @Override
        public void run() {
            Looper.prepare();
            synchronized (this) {
                handler = new Handler(Looper.myLooper());
                notifyAll();
            }
            Looper.loop();
        }

        public synchronized Handler getHandler() {
            while (handler == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return handler;
        }

    }

}