package com.example.fittrack.viewmodel;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel class for managing the timer functionality.
 * This ViewModel is responsible for managing a countdown timer, allowing it to start, stop,
 * reset, and update the remaining time, while ensuring that the UI can observe the changes in the remaining time.
 */
public class TimerModel extends ViewModel {

    /**
     * LiveData object to hold and observe the remaining time in milliseconds.
     * The default value is set to 2 minutes (120,000 milliseconds).
     */
    private MutableLiveData<Long> remainingTimeLiveData = new MutableLiveData<>(120000L);

    /**
     * The end time of the timer, in milliseconds since epoch.
     */
    private long endTime = 0L;

    /**
     * Flag to track whether the timer is currently running.
     */
    private boolean clockRunning = false;

    /**
     * Gets the LiveData object representing the remaining time.
     *
     * @return A LiveData object containing the remaining time in milliseconds.
     */
    public LiveData<Long> getRemainingTime() {
        return remainingTimeLiveData;
    }

    /**
     * Starts the timer if it is not already running.
     * The end time is calculated as the current system time plus the remaining time in milliseconds.
     * The timer is updated every second.
     */
    public void start() {
        if (!clockRunning) {
            clockRunning = true;
            endTime = System.currentTimeMillis() + getRemainingTimeInMillis();
            updateTime();
        }
    }

    /**
     * Stops the timer if it is running.
     * The remaining time is updated and saved in the LiveData object.
     */
    public void stop() {
        if (clockRunning) {
            clockRunning = false;
            remainingTimeLiveData.setValue(endTime - System.currentTimeMillis());
        }
    }

    /**
     * Resets the timer to its initial value of 2 minutes (120,000 milliseconds) and stops the timer.
     */
    public void reset() {
        clockRunning = false;
        remainingTimeLiveData.setValue(120000L); // Reset to 2 minutes
    }

    /**
     * Updates the remaining time and posts the new value to the LiveData object.
     * This method is called every second while the timer is running.
     */
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

    /**
     * Gets the remaining time in milliseconds from the LiveData object.
     * If the value is null, it returns the default value of 2 minutes (120,000 milliseconds).
     *
     * @return The remaining time in milliseconds.
     */
    private long getRemainingTimeInMillis() {
        return remainingTimeLiveData.getValue() != null ? remainingTimeLiveData.getValue() : 120000L;
    }
}
