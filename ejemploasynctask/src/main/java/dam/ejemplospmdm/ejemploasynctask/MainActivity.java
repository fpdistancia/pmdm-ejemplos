package dam.ejemplospmdm.ejemploasynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pb = findViewById(R.id.progressBar);
    }

    public void tarea(View v) {
        new AsyncTask<Void, Integer, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                while (pb.getProgress() < 100) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {}
                    publishProgress(1);
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                pb.setProgress(0);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                pb.incrementProgressBy(1);
            }

            @Override
            protected void onPostExecute(Void unused) {
            }

        }.execute();
    }

    public void saluda(View v) {
        Toast.makeText(this, "Hola Mundo!", Toast.LENGTH_LONG).show();
    }

}