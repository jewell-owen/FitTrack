package com.example.fittrack;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    protected BottomNavigationView bottomNavigationView;
    ProfileFragment profileFragment = new ProfileFragment();
    FriendsFragment friendsFragment = new FriendsFragment();
    WorkoutFragment workoutFragment = new WorkoutFragment();
    StatsFragment statsFragment = new StatsFragment();
    NutritionFragment nutritionFragment = new NutritionFragment();

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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if ((item.getItemId() == R.id.Profile)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, profileFragment)
                    .commit();
            return true;
        } else if ((item.getItemId() == R.id.Friends)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, friendsFragment)
                    .commit();
            return true;
        } else if ((item.getItemId() == R.id.Workout)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, workoutFragment)
                    .commit();
            return true;
        } else if ((item.getItemId() == R.id.Stats)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, statsFragment)
                    .commit();
            return true;
        } else if ((item.getItemId() == R.id.Nutrition)) {
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