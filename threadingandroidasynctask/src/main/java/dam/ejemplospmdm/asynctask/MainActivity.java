package dam.ejemplospmdm.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import dam.ejemplospmdm.ejemploasynctask.R;

public class MainActivity extends AppCompatActivity {

    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = findViewById(R.id.progressBar);
    }

    static class Tarea extends AsyncTask<Void, Integer, Void> {

        private final WeakReference<MainActivity> activity;

        public Tarea(MainActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (activity.get().pb.getProgress() < 100) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {}
                publishProgress(1);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            activity.get().pb.setProgress(0);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            activity.get().pb.incrementProgressBy(1);
        }

        @Override
        protected void onPostExecute(Void unused) {
        }
    }

    public void iniciarTarea(View v) {
        Tarea task = new Tarea(this);
        task.execute();
    }

    public void saluda(View v) {
        Toast.makeText(this, "Hola Mundo!", Toast.LENGTH_SHORT).show();
    }

}