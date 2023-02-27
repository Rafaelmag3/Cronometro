package com.example.micronometro;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private Button startButton;
    private Button stopButton;
    private Button lapButton;
    private LinearLayout lapLayout;
    private ArrayList<Long> lapTimes = new ArrayList<>();
    private boolean isChronometerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);
        lapButton = findViewById(R.id.lap_button);
        lapLayout = findViewById(R.id.lap_layout);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isChronometerRunning) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    lapTimes.clear();
                    lapLayout.removeAllViews();
                }
                chronometer.start();
                isChronometerRunning = true;
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                lapButton.setEnabled(true);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChronometerRunning) {
                    chronometer.stop();
                    isChronometerRunning = false;
                    startButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    lapButton.setEnabled(false);
                }
            }
        });

        lapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChronometerRunning) {
                    long lapTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                    lapTimes.add(lapTime);

                    TextView lapTextView = new TextView(MainActivity.this);
                    lapTextView.setText(String.format(Locale.getDefault(), "Lap %d: %s", lapTimes.size(), formatTime(lapTime)));
                    lapTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); // Establece el tamaño de la letra en 24sp
                    lapTextView.setTypeface(Typeface.create("casual", Typeface.NORMAL)); // Establece la fuente "casual"
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER;
                    lapTextView.setLayoutParams(params);
                    lapLayout.addView(lapTextView, 0);
                }
            }
        });
    }
        private String formatTime(long time) {
            int hours = (int) (time / 3600000);
            int minutes = (int) ((time % 3600000) / 60000);
            int seconds = (int) ((time % 60000) / 1000);
            int milliseconds = (int) (time % 1000);
            return String.format(Locale.getDefault(), "%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
        }
}
