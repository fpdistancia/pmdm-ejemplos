package fp.dam.pmdm.handlerbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    TextView text;

    private static class LooperThread extends Thread {

        private Handler handler;

        public void run() {
            Looper.prepare();
            synchronized (this) {
                handler = new Handler();
                notifyAll();
            }
            Looper.loop();
        }

        public synchronized Handler getHandler() {
            while (handler == null) {
                try {
                    wait();
                } catch (InterruptedException e) {}
            }
            return handler;
        }

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.textView);
        LooperThread thread = new LooperThread();
        thread.start();
        while ((handler = thread.getHandler()) == null);
    }

    public void onClick(View v) {
        if (handler != null)
            handler.post(this::doLongRunningOperation);
    }

    private void doLongRunningOperation() {
        for (int i=1; i<=100; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    text.setText(String.valueOf(Integer.parseInt(text.getText().toString()) + 1));
                }
            });
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        handler.getLooper().quit();
    }

}