package com.example.fittrack.nutrition;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fittrack.R;
import com.example.fittrack.adapter.LoggedWorkoutAdapter;
import com.example.fittrack.adapter.NutritionAdapter;
import com.example.fittrack.workout.ListSavedWorkoutsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class NutritionFragment extends Fragment implements  NutritionAdapter.onFoodSelectedListener {


    private Button dateBtn;
    private ImageButton previousDayBtn, nextDayBtn, breakfastAddBtn, lunchAddBtn, dinnerAddBtn, otherAddBtn;

    private Calendar selectedDate;

    private String dateId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private Query mQueryExercisesBreakfast;
    private NutritionAdapter mAdapterExercisesBreakfast;

    private Query mQueryExercisesLunch;
    private NutritionAdapter mAdapterExercisesLunch;

    private Query mQueryExercisesDinner;
    private NutritionAdapter mAdapterExercisesDinner;

    private Query mQueryExercisesOther;
    private NutritionAdapter mAdapterExercisesOther;

    private RecyclerView breakfastRecycler, lunchRecycler, dinnerRecycler, otherRecycler;

    private TextView totalCaloriesTv, breakfastCaloriesTv, lunchCaloriesTv, dinnerCaloriesTv, otherCaloriesTv;

    private ImageButton editGoalBtn, saveGoalBtn;

    private TextView goalTv;
    private EditText goalEt;

    String nutrition = "";

    private String goal;

    private double breakfastCaloriesText = 0;
    private double lunchCaloriesText = 0;
    private double dinnerCaloriesText = 0;
    private double otherCaloriesText = 0;
    private double totalCalories = 0;
    private int numFood;

    private boolean isEditing = false;



    public NutritionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapterExercisesBreakfast != null) {
            mAdapterExercisesBreakfast.startListening();
        }

        if (mAdapterExercisesLunch != null) {
            mAdapterExercisesLunch.startListening();
        }

        if (mAdapterExercisesDinner != null) {
            mAdapterExercisesDinner.startListening();
        }

        if (mAdapterExercisesOther != null) {
            mAdapterExercisesOther.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapterExercisesBreakfast != null) {
            mAdapterExercisesBreakfast.stopListening();
        }

        if (mAdapterExercisesLunch != null) {
            mAdapterExercisesLunch.stopListening();
        }

        if (mAdapterExercisesDinner != null) {
            mAdapterExercisesDinner.stopListening();
        }

        if (mAdapterExercisesOther != null) {
            mAdapterExercisesOther.stopListening();
        }
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
        editGoalBtn = rootView.findViewById(R.id.nutrition_edit_btn);
        saveGoalBtn = rootView.findViewById(R.id.nutrition_save_btn);
        goalTv = rootView.findViewById(R.id.daily_goal_value_tv);
        goalEt = rootView.findViewById(R.id.daily_goal_value_et);


        breakfastCaloriesTv = rootView.findViewById(R.id.breakfast_total_calories_tv);
        lunchCaloriesTv = rootView.findViewById(R.id.lunch_total_calories_tv);
        dinnerCaloriesTv = rootView.findViewById(R.id.dinner_total_calories_tv);
        otherCaloriesTv = rootView.findViewById(R.id.other_total_calories_tv);
        totalCaloriesTv = rootView.findViewById(R.id.total_calories_value_tv);

        breakfastRecycler = rootView.findViewById(R.id.breakfast_recycler);
        lunchRecycler = rootView.findViewById(R.id.lunch_recycler);
        dinnerRecycler = rootView.findViewById(R.id.dinner_recycler);
        otherRecycler = rootView.findViewById(R.id.other_recycler);

        breakfastRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        breakfastRecycler.setAdapter(mAdapterExercisesBreakfast);

        lunchRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lunchRecycler.setAdapter(mAdapterExercisesLunch);

        dinnerRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        dinnerRecycler.setAdapter(mAdapterExercisesDinner);

        otherRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        otherRecycler.setAdapter(mAdapterExercisesOther);

        updateDateButton();

        mQueryExercisesBreakfast = db.collection("users")
                .document(user.getUid())
                .collection(dateId)
                .document(dateId)
                .collection("breakfast");
        initBreakfastRecyclerView();

        mQueryExercisesLunch = db.collection("users")
                .document(user.getUid())
                .collection(dateId)
                .document(dateId)
                .collection("lunch");
        initLunchRecyclerView();

        mQueryExercisesDinner = db.collection("users")
                .document(user.getUid())
                .collection(dateId)
                .document(dateId)
                .collection("dinner");
        initDinnerRecyclerView();

        mQueryExercisesOther = db.collection("users")
                .document(user.getUid())
                .collection(dateId)
                .document(dateId)
                .collection("other");
        initOtherRecyclerView();



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
                Bundle args = new Bundle();
                args.putString("meal", "breakfast");
                args.putString("date", dateId);
                libraryNutrition.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, libraryNutrition)
                        .addToBackStack(null)
                        .commit();
                // Add a listener for when food is selected in the library
            }
        });

        lunchAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLibraryNutritionFragment libraryNutrition = new SelectLibraryNutritionFragment();
                Bundle args = new Bundle();
                args.putString("meal", "lunch");
                args.putString("date", dateId);
                libraryNutrition.setArguments(args);
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
                Bundle args = new Bundle();
                args.putString("meal", "dinner");
                args.putString("date", dateId);
                libraryNutrition.setArguments(args);
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
                Bundle args = new Bundle();
                args.putString("meal", "other");
                args.putString("date", dateId);
                libraryNutrition.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, libraryNutrition)
                        .addToBackStack(null)
                        .commit();
            }
        });

        editGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEditing){
                    goalEt.setText("");
                    goalTv.setVisibility(View.INVISIBLE);
                    goalEt.setVisibility(View.VISIBLE);
                    editGoalBtn.setVisibility(View.INVISIBLE);
                    saveGoalBtn.setVisibility(View.VISIBLE);
                    isEditing = true;
                }
            }
        });

        saveGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditing){
                    goal = goalEt.getText().toString();
                    goalTv.setText(goal);
                    goalTv.setVisibility(View.VISIBLE);
                    goalEt.setVisibility(View.INVISIBLE);
                    editGoalBtn.setVisibility(View.VISIBLE);
                    saveGoalBtn.setVisibility(View.INVISIBLE);
                    isEditing = false;
                }
            }
        });



        return rootView;
    }

    private void initBreakfastRecyclerView() {
        if (mQueryExercisesBreakfast == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapterExercisesBreakfast = new NutritionAdapter(mQueryExercisesBreakfast, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                    updateBreakfastCalories();

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
            }
        };

        //myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        breakfastRecycler.setAdapter(mAdapterExercisesBreakfast);
    }

    private void initLunchRecyclerView() {
        if (mQueryExercisesLunch == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapterExercisesLunch = new NutritionAdapter(mQueryExercisesLunch, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                    updateLunchCalories();

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
            }
        };

        //myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lunchRecycler.setAdapter(mAdapterExercisesLunch);
    }

    private void initDinnerRecyclerView() {
        if (mQueryExercisesDinner == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapterExercisesDinner = new NutritionAdapter(mQueryExercisesDinner, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                    updateDinnerCalories();
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
            }
        };

        //myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dinnerRecycler.setAdapter(mAdapterExercisesDinner);
    }

    private void initOtherRecyclerView() {
        if (mQueryExercisesOther == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapterExercisesOther = new NutritionAdapter(mQueryExercisesOther, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                    updateOtherCalories();

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
            }
        };

        //myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        otherRecycler.setAdapter(mAdapterExercisesOther);
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

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateId = sdf2.format(selectedDate.getTime());

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

    public void updateBreakfastCalories () {
        db.collection("users")
                .document(user.getUid())
                .collection(dateId)
                .document(dateId)
                .collection("breakfast")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            int documentCount = querySnapshot.size();
                            numFood = documentCount;
                            double tempOther = breakfastCaloriesText;
                            breakfastCaloriesText = 0;
                            for (DocumentSnapshot document : querySnapshot) {
                                if (document.get("calories") != null) {
                                    double tempSets = Double.parseDouble(document.get("calories").toString());
                                    breakfastCaloriesText += tempSets;
                                    breakfastCaloriesTv.setText(String.valueOf(breakfastCaloriesText));
                                }
                            }
                            totalCalories += breakfastCaloriesText - tempOther;
                            totalCaloriesTv.setText(String.valueOf(totalCalories));
                        }
                    }
                });
    }

    public void updateLunchCalories () {
        db.collection("users")
                .document(user.getUid())
                .collection(dateId)
                .document(dateId)
                .collection("lunch")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            int documentCount = querySnapshot.size();
                            numFood = documentCount;
                            double tempOther = lunchCaloriesText;
                            lunchCaloriesText = 0;
                            for (DocumentSnapshot document : querySnapshot) {
                                if (document.get("calories") != null) {
                                    double tempSets = Double.parseDouble(document.get("calories").toString());
                                    lunchCaloriesText += tempSets;
                                    lunchCaloriesTv.setText(String.valueOf(lunchCaloriesText));
                                }
                            }
                            totalCalories += lunchCaloriesText - tempOther;
                            totalCaloriesTv.setText(String.valueOf(totalCalories));
                        }
                    }
                });
    }

    public void updateDinnerCalories () {
        db.collection("users")
                .document(user.getUid())
                .collection(dateId)
                .document(dateId)
                .collection("dinner")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            int documentCount = querySnapshot.size();
                            numFood = documentCount;
                            double tempOther = dinnerCaloriesText;
                            dinnerCaloriesText = 0;
                            for (DocumentSnapshot document : querySnapshot) {
                                if (document.get("calories") != null) {
                                    double tempSets = Double.parseDouble(document.get("calories").toString());
                                    dinnerCaloriesText += tempSets;
                                    dinnerCaloriesTv.setText(String.valueOf(dinnerCaloriesText));
                                }
                            }
                            totalCalories += dinnerCaloriesText - tempOther;
                            totalCaloriesTv.setText(String.valueOf(totalCalories));
                        }
                    }
                });
    }

    public void updateOtherCalories () {
        db.collection("users")
                .document(user.getUid())
                .collection(dateId)
                .document(dateId)
                .collection("other")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            int documentCount = querySnapshot.size();
                            numFood = documentCount;
                            double tempOther = otherCaloriesText;
                            otherCaloriesText = 0;
                            for (DocumentSnapshot document : querySnapshot) {
                                if (document.get("calories") != null) {
                                    double tempSets = Double.parseDouble(document.get("calories").toString());
                                    otherCaloriesText += tempSets;
                                    otherCaloriesTv.setText(String.valueOf(otherCaloriesText));
                                }
                            }
                            totalCalories += otherCaloriesText - tempOther;
                            totalCaloriesTv.setText(String.valueOf(totalCalories));

                        }
                    }
                });
    }


    @Override
    public void onGoToFood(DocumentSnapshot snapshot) {

    }
}