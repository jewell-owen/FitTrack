package com.example.fittrack.nutrition;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.fittrack.R;
import com.example.fittrack.workout.ListSavedWorkoutsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class NutritionFragment extends Fragment {


    private Button dateBtn;
    private ImageButton previousDayBtn, nextDayBtn, breakfastAddBtn, lunchAddBtn, dinnerAddBtn, otherAddBtn;

    private Calendar selectedDate;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    String nutrition = "";
    private Spinner nutritionSpinner;


    public NutritionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nutrition, container, false);

        // Initialize the selected date with today's date
        selectedDate = Calendar.getInstance();

        // Get the buttons
        dateBtn = rootView.findViewById(R.id.date_btn);
        previousDayBtn = rootView.findViewById(R.id.previous_day_btn);
        nextDayBtn = rootView.findViewById(R.id.next_day_btn);
        breakfastAddBtn = rootView.findViewById(R.id.breakfast_card_add_btn);
        lunchAddBtn = rootView.findViewById(R.id.lunch_card_add_btn);
        dinnerAddBtn = rootView.findViewById(R.id.dinner_card_add_btn);
        otherAddBtn = rootView.findViewById(R.id.other_card_add_btn);



        // Set the click listener for the date button
        dateBtn.setOnClickListener(v -> openDatePicker());

        // Set the click listener for the previous day button
        previousDayBtn.setOnClickListener(v -> changeDate(-1));

        // Set the click listener for the next day button
        nextDayBtn.setOnClickListener(v -> changeDate(1));

        // Update the date button with the current date
        updateDateButton();

        SelectLibraryNutritionFragment libraryNutrition = new SelectLibraryNutritionFragment();

        breakfastAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLibraryNutritionFragment libraryNutrition = new SelectLibraryNutritionFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, libraryNutrition)
                        .addToBackStack(null)
                        .commit();
            }
        });

        lunchAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLibraryNutritionFragment libraryNutrition = new SelectLibraryNutritionFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, libraryNutrition)
                        .addToBackStack(null)
                        .commit();
            }
        });

        dinnerAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLibraryNutritionFragment libraryNutrition = new SelectLibraryNutritionFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, libraryNutrition)
                        .addToBackStack(null)
                        .commit();
            }
        });

        otherAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLibraryNutritionFragment libraryNutrition = new SelectLibraryNutritionFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, libraryNutrition)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return rootView;
    }

    // Method to open the DatePickerDialog
    private void openDatePicker() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate.set(year1, month1, dayOfMonth);
                    updateDateButton();
                },
                year, month, day);

        datePickerDialog.show();
    }

    // Method to change the date by a given number of days (positive for next day, negative for previous day)
    private void changeDate(int dayOffset) {
        selectedDate.add(Calendar.DAY_OF_MONTH, dayOffset);
        updateDateButton();
        fetchNutritionForSelectedDate();
    }

    // Method to update the date displayed on the button
    private void updateDateButton() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = sdf.format(selectedDate.getTime());

        // If the selected date is today, show "Today" instead of the date
        Calendar today = Calendar.getInstance();
        if (selectedDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                selectedDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                selectedDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            dateBtn.setText("Today");
        } else {
            dateBtn.setText(formattedDate);
        }
        fetchNutritionForSelectedDate();
    }

    private void fetchNutritionForSelectedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = sdf.format(selectedDate.getTime());

        DocumentReference planRef = db.collection("users")
                .document(user.getUid())
                .collection("plans")
                .document(formattedDate);

        planRef.get().addOnSuccessListener(documentSnapshot -> {

        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error fetching plan for date: " + formattedDate, e);

        });
    }
}