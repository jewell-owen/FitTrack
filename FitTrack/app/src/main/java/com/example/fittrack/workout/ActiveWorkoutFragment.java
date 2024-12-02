package com.example.fittrack.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.fittrack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActiveWorkoutFragment extends Fragment implements View.OnClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    // Workout home screen UI items
    private TextView titleTextView;

    // Current workout UI items
    private ImageButton finishWorkoutButton;
    private ImageButton cancelWorkoutButton;
    private HorizontalScrollView exerciseScrollView;
    private LinearLayout cardLayout;
    private ConstraintLayout restTimerLayout;
    // Timer layout UI items
    private Button resetButton;
    private TextView timerTextView;
    private Button startStopButton;

    // Place holder for selectedWorkout
    private String plannedWorkout = null;



    public ActiveWorkoutFragment() {
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
        return new WorkoutFragment();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        //Workout home screen UI items initialization
        titleTextView = view.findViewById(R.id.active_workout_name_et);

        //Current workout UI items initialization
        finishWorkoutButton = view.findViewById(R.id.active_workout_finish_btn);
        cancelWorkoutButton = view.findViewById(R.id.active_workout_cancel_btn);
        exerciseScrollView = view.findViewById(R.id.workout_scroll_view);
        cardLayout = view.findViewById(R.id.workout_card_ll);
        restTimerLayout = view.findViewById(R.id.active_workout_rest_timer_cl);
        resetButton = view.findViewById(R.id.btnReset);
        timerTextView = view.findViewById(R.id.active_workout_timer_tv);
        startStopButton = view.findViewById(R.id.btnStartStop);



        return view;
    }


}
