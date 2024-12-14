package com.example.fittrack.workout;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.fittrack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A fragment used as the home screen for all other workout functionality.
 */
public class WorkoutFragment extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    // Workout home screen UI items
    private TextView titleTextView;
    private ImageButton startWorkoutButton;
    private Button myWorkoutsButton;
    private Button plannedWorkoutButton;
    private Button workoutHistoryButton;
    private TextView plannedWorkoutTextView;

    // Place holder for selectedWorkout
    private String plannedWorkoutName = "None";
    private String plannedWorkoutID = "None";

    private String mParam1;
    private String mParam2;

    private String date = "";


    /**
     * Required empty public constructor
     */
    public WorkoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkoutFragment.
     */
    public static WorkoutFragment newInstance(String param1, String param2) {
        WorkoutFragment fragment = new WorkoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Required onClick
     * @param view The view that was clicked
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    /**
     * Called when the view is created
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view and handles all other initialization
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the view for the fragment's UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        //Workout home screen UI items initialization
        titleTextView = view.findViewById(R.id.workout_name_tv);
        startWorkoutButton = view.findViewById(R.id.workout_start_workout_btn);
        plannedWorkoutButton = view.findViewById(R.id.workout_add_plan_btn);
        myWorkoutsButton = view.findViewById(R.id.workout_my_workouts_btn);
        workoutHistoryButton = view.findViewById(R.id.workout_logged_workouts_btn);
        plannedWorkoutTextView = view.findViewById(R.id.workout_today_plan_workout_tv);

        // Inside onCreateView or other initialization code
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Match the format used in Firestore
        date = sdf.format(calendar.getTime());

        // Fetch the planned workout
        db.collection("users")
                .document(user.getUid())
                .collection("plans")
                .document(date) // Using the formatted date as the document ID
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // If the document exists, get the workout name and assign it to the TextView
                        String workoutName = documentSnapshot.getString("workoutName");
                        if (workoutName != null) {
                            plannedWorkoutTextView.setText(workoutName);
                            plannedWorkoutName = workoutName;
                            plannedWorkoutID = documentSnapshot.getString("workoutId");
                        } else {
                            plannedWorkoutTextView.setText("None");
                            plannedWorkoutName = "None";
                            plannedWorkoutID  = "None";
                        }
                    } else {
                        // If the document does not exist, set the TextView to "None"
                        plannedWorkoutTextView.setText("None");
                        plannedWorkoutName = "None";
                        plannedWorkoutID  = "None";
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching planned workout", e);
                    // Handle failure by setting a fallback value
                    plannedWorkoutTextView.setText("None");
                    plannedWorkoutName = "None";
                    plannedWorkoutID  = "None";
                });


        // Where the timer implementation goes
        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActiveWorkoutFragment activeWorkout = new ActiveWorkoutFragment();
                Bundle args = new Bundle();
                args.putString("workoutId", plannedWorkoutID);
                args.putString("workoutName", plannedWorkoutName);
                activeWorkout.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, activeWorkout)
                        .addToBackStack(null)
                        .commit();
            }
        });

        //Go to plan workouts screen
        plannedWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlanWorkoutsFragment planWorkouts = new PlanWorkoutsFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, planWorkouts)
                        .addToBackStack(null)
                        .commit();
            }

        });

        //Go to saved workout planss screen
        myWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListSavedWorkoutsFragment listSavedWorkouts = new ListSavedWorkoutsFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, listSavedWorkouts)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Go to logged workouts history screen
        workoutHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListLoggedWorkoutsFragment listLoggedWorkouts = new ListLoggedWorkoutsFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, listLoggedWorkouts)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }




}