package com.example.fittrack.viewmodel;

import androidx.lifecycle.ViewModel;


/**
 * ViewModel class for managing the main activity.
 */
public class MainActivityViewModel extends ViewModel {

    private boolean mIsSigningIn;


    public MainActivityViewModel() {
        mIsSigningIn = false;
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}

