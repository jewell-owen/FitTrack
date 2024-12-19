package com.example.fittrack.viewmodel;

import androidx.lifecycle.ViewModel;

/**
 * ViewModel class for managing the state of the main activity.
 * This ViewModel is responsible for holding and managing UI-related data
 * for the MainActivity, specifically the state of the sign-in process.
 */
public class MainActivityViewModel extends ViewModel {

    /**
     * Boolean flag indicating whether the user is in the sign-in process.
     */
    private boolean mIsSigningIn;

    /**
     * Default constructor for MainActivityViewModel.
     * Initializes mIsSigningIn to false, indicating that the user is not signing in by default.
     */
    public MainActivityViewModel() {
        mIsSigningIn = false;
    }

    /**
     * Gets the current state of the sign-in process.
     *
     * @return true if the user is in the sign-in process, false otherwise.
     */
    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    /**
     * Sets the state of the sign-in process.
     *
     * @param mIsSigningIn true if the user is signing in, false otherwise.
     */
    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}
