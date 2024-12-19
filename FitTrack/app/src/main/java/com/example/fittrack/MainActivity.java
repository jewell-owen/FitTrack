package com.example.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.fittrack.nutrition.NutritionFragment;
import com.example.fittrack.viewmodel.MainActivityViewModel;
import com.example.fittrack.workout.WorkoutFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The main activity of the application.
 * Manages the bottom navigation and handles user authentication.
 * Displays different fragments based on the user's selection in the navigation menu.
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView bottomNavigationView;
    ProfileFragment profileFragment = new ProfileFragment();
    FriendsFragment friendsFragment = new FriendsFragment();
    WorkoutFragment workoutFragment = new WorkoutFragment();
    StatsFragment statsFragment = new StatsFragment();
    NutritionFragment nutritionFragment = new NutritionFragment();

    FirebaseAuth auth;
    FirebaseUser user;

    private MainActivityViewModel mViewModel;

    /**
     * Called when the activity is first created.
     * Initializes the bottom navigation view, sets up the authentication check,
     * and prepares the view model for the activity.
     * @param savedInstanceState The saved instance state for restoring activity's state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.Profile);
        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.top_background));

        // Firebase Authentication check
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            // If user is not authenticated, navigate to the login screen
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Initialize the ViewModel
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    /**
     * Handles navigation item selection in the bottom navigation view.
     * Replaces the current fragment with the selected one.
     * @param item The selected menu item.
     * @return true if the fragment transaction was successful, false otherwise.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Profile) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, profileFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.Friends) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, friendsFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.Workout) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, workoutFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.Stats) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, statsFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.Nutrition) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, nutritionFragment)
                    .commit();
            return true;
        } else {
            return false;
        }
    }
}
