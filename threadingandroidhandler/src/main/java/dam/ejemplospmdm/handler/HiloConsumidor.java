package dam.ejemplospmdm.handler;

import android.os.Handler;
import android.os.Looper;

        public class HiloConsumidor extends Thread {
            @Override
            public void run() {
                Looper.prepare();
                // aquí se puede crear un Handler
                Looper.loop();
            }
        }


