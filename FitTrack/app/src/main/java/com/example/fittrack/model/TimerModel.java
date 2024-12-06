package com.example.fittrack.model;

import android.os.Handler;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class TimerModel extends ViewModel {
    private long remainingTime = 120000L; // Start with 2 minutes in milliseconds
    private long endTime = 0L;
    private boolean clockRunning = false;

    public void start(TextView tv) {
        if (!clockRunning) {
            clockRunning = true;
            endTime = System.currentTimeMillis() + remainingTime; // Set the end time
            runTimer(tv);
        }
    }

    public void stop() {
        if (clockRunning) {
            clockRunning = false;
            remainingTime = endTime - System.currentTimeMillis(); // Save remaining time
        }
    }

    public void reset(TextView tv) {
        clockRunning = false;
        remainingTime = 120000L; // Reset to 2 minutes
        tv.setText("02:00");
    }

    private void runTimer(TextView tvStopwatchTime) {
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (clockRunning) {
                    long currentTime = System.currentTimeMillis();
                    remainingTime = endTime - currentTime;

                    if (remainingTime <= 0) {
                        clockRunning = false;
                        remainingTime = 0; // Ensure timer doesn't go negative
                    }

                    long minutes = (remainingTime / 60000);
                    long seconds = (remainingTime % 60000) / 1000;

                    String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

                    tvStopwatchTime.setText(time);

                    if (clockRunning) {
                        handler.postDelayed(this, 100); // Update every 100 milliseconds
                    }
                }
            }
        });
    }

    public long getRemainingTime() {
        return remainingTime;
    }
}