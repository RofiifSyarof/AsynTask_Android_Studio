package com.example.asyntask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button startButton, stopButton;
    private TextView timeCounter;
    private MyAsyncTask myAsyncTask;
    private Handler handler = new Handler();
    private int seconds = 0;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi komponen UI
        progressBar = findViewById(R.id.progressBar);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        timeCounter = findViewById(R.id.timeCounter);

        // Listener untuk tombol Start
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myAsyncTask == null || myAsyncTask.getStatus() != AsyncTask.Status.RUNNING) {
                    seconds = 0;
                    isRunning = true;
                    myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute();
                    startTimer();
                }
            }
        });

        // Listener untuk tombol Stop
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myAsyncTask != null) {
                    myAsyncTask.cancel(true);
                }
                isRunning = false;
            }
        });
    }

    // Metode untuk menjalankan timer
    private void startTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    seconds++;
                    timeCounter.setText("Time: " + seconds + "s");
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    // Kelas AsyncTask untuk menjalankan proses di background
    private class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setProgress(0); // Set progress awal ke 0
            timeCounter.setText("Time: 0s"); // Reset waktu
            seconds = 0;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i <= 100; i++) {
                if (isCancelled()) break;

                try {
                    Thread.sleep(100); // Simulasi delay 100 ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            isRunning = false; // Stop timer ketika AsyncTask selesai
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressBar.setProgress(0);
            timeCounter.setText("Time: 0s");
            isRunning = false;
        }
    }
}
