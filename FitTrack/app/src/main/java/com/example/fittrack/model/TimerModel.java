package com.example.fittrack.model;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerModel extends ViewModel {
    private MutableLiveData<Long> remainingTimeLiveData = new MutableLiveData<>(120000L); // Default 2 minutes
    private long endTime = 0L;
    private boolean clockRunning = false;

    public LiveData<Long> getRemainingTime() {
        return remainingTimeLiveData;
    }

    public void start() {
        if (!clockRunning) {
            clockRunning = true;
            endTime = System.currentTimeMillis() + getRemainingTimeInMillis();
            updateTime();
        }
    }

    public void stop() {
        if (clockRunning) {
            clockRunning = false;
            remainingTimeLiveData.setValue(endTime - System.currentTimeMillis());
        }
    }

    public void reset() {
        clockRunning = false;
        remainingTimeLiveData.setValue(120000L); // Reset to 2 minutes
    }

    private void updateTime() {
        if (clockRunning) {
            long currentTime = System.currentTimeMillis();
            long remainingTime = endTime - currentTime;

            if (remainingTime <= 0) {
                clockRunning = false;
                remainingTime = 0; // Ensure no negative values
            }

            remainingTimeLiveData.postValue(remainingTime);

            if (clockRunning) {
                new Handler().postDelayed(this::updateTime, 1000); // Update every second
            }
        }
    }

    private long getRemainingTimeInMillis() {
        return remainingTimeLiveData.getValue() != null ? remainingTimeLiveData.getValue() : 120000L;
    }
}