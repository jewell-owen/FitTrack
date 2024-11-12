package com.example.fittrack;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fittrack.viewmodel.MainActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

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

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        String apiKey = BuildConfig.API_KEY;
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