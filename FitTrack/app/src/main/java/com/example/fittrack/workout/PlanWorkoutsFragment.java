package com.example.fittrack.workout;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fittrack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlanWorkoutsFragment extends Fragment implements View.OnClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private ImageButton planWorkoutsBackButton;
    private ImageButton planWorkoutsPreviousDayBtn;
    private Button planWorkoutsDateBtn;
    private ImageButton planWorkoutsNextDayBtn;
    private Spinner planWorkoutsSpinner;

    private List<String> workouts = new ArrayList<>();
    private List<String> workoutIds = new ArrayList<>();
    private String workoutName = "";
    private String workoutId = "";

    private Calendar selectedDate;

    private boolean isUserSelection = false;

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_plan_workouts, container, false);

        planWorkoutsBackButton = view.findViewById(R.id.plan_workouts_back_btn);
        planWorkoutsPreviousDayBtn = view.findViewById(R.id.plan_workouts_previous_day_btn);
        planWorkoutsNextDayBtn = view.findViewById(R.id.plan_workouts_next_day_btn);
        planWorkoutsDateBtn = view.findViewById(R.id.plan_workouts_date_btn);
        planWorkoutsSpinner = view.findViewById(R.id.plan_workouts_spinner);

        // Initialize the adapter with an empty list
        ArrayAdapter<String> workoutAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, workouts);
        workoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        planWorkoutsSpinner.setAdapter(workoutAdapter);

        // Track if the spinner selection is user-initiated
        planWorkoutsSpinner.setOnTouchListener((v, event) -> {
            isUserSelection = true;
            return false;
        });

        planWorkoutsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!isUserSelection) {
                    return; // Ignore programmatic selections
                }
                isUserSelection = false; // Reset the flag

                workoutName = adapterView.getItemAtPosition(position).toString();
                workoutId = workoutIds.get(position);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = sdf.format(selectedDate.getTime());

                DocumentReference workoutRef = db.collection("users")
                        .document(user.getUid())
                        .collection("plans")
                        .document(formattedDate);

                Map<String, Object> workoutData = new HashMap<>();
                workoutData.put("date", formattedDate);
                workoutData.put("workoutId", workoutId);
                workoutData.put("workoutName", workoutName);

                workoutRef.set(workoutData)
                        .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Workout for " + formattedDate + " set to " + workoutName, Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            Log.e("Firestore", "Error saving workout", e);
                            Toast.makeText(getContext(), "Failed to save workout.", Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // No action needed
            }
        });

        fetchWorkouts(workoutAdapter);

        selectedDate = Calendar.getInstance();

        planWorkoutsDateBtn.setOnClickListener(v -> openDatePicker());
        planWorkoutsPreviousDayBtn.setOnClickListener(v -> changeDate(-1));
        planWorkoutsNextDayBtn.setOnClickListener(v -> changeDate(1));
        planWorkoutsBackButton.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        updateDateButton();

        return view;
    }

    private void fetchWorkouts(ArrayAdapter<String> workoutAdapter) {
        CollectionReference workoutsRef = db.collection("users").document(user.getUid()).collection("workouts");

        workoutsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                workouts.clear();
                workoutIds.clear();

                workouts.add("None");
                workoutIds.add("None");
                workouts.add("Rest");
                workoutIds.add("Rest");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    queryDocumentSnapshots.forEach(document -> {
                        workouts.add(document.getString("name"));
                        workoutIds.add(document.getId());
                    });
                }

                workoutAdapter.notifyDataSetChanged();
                fetchPlanForSelectedDate(); // Fetch the current plan after loading workouts
            } else {
                Toast.makeText(getContext(), "No workouts found.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error fetching workouts", e);
            Toast.makeText(getContext(), "Failed to load workouts.", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchPlanForSelectedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = sdf.format(selectedDate.getTime());

        DocumentReference planRef = db.collection("users")
                .document(user.getUid())
                .collection("plans")
                .document(formattedDate);

        planRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String plannedWorkoutName = documentSnapshot.getString("workoutName");

                if (plannedWorkoutName != null) {
                    int index = workouts.indexOf(plannedWorkoutName);
                    if (index != -1) {
                        planWorkoutsSpinner.setSelection(index);
                    } else {
                        planWorkoutsSpinner.setSelection(workouts.indexOf("None"));
                    }
                }
            } else {
                planWorkoutsSpinner.setSelection(workouts.indexOf("None"));
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error fetching plan for date: " + formattedDate, e);
            planWorkoutsSpinner.setSelection(workouts.indexOf("None"));
        });
    }

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

    private void changeDate(int dayOffset) {
        selectedDate.add(Calendar.DAY_OF_MONTH, dayOffset);
        updateDateButton();
    }

    private void updateDateButton() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = sdf.format(selectedDate.getTime());

        Calendar today = Calendar.getInstance();
        if (selectedDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                selectedDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                selectedDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            planWorkoutsDateBtn.setText("Today");
        } else {
            planWorkoutsDateBtn.setText(formattedDate);
        }

        fetchPlanForSelectedDate();
    }
}