package com.example.fittrack.workout;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
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


    //Select planned workout UI items
    private ImageButton plannedWorkoutsBackButton;
    private ScrollView plannedWorkoutsScrollView;
    private LinearLayout plannedWorkoutsLinearLayout;

    // Place holder for selectedWorkout
    private String plannedWorkout = null;

    private String mParam1;
    private String mParam2;


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

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        //Workout home screen UI items initialization
        titleTextView = view.findViewById(R.id.active_workout_name_et);
        startWorkoutButton = view.findViewById(R.id.workout_start_workout_btn);
        plannedWorkoutButton = view.findViewById(R.id.workout_add_plan_btn);
        myWorkoutsButton = view.findViewById(R.id.workout_my_workouts_btn);

        //Current workout UI items initialization
        finishWorkoutButton = view.findViewById(R.id.active_workout_finish_btn);
        cancelWorkoutButton = view.findViewById(R.id.active_workout_cancel_btn);
        exerciseScrollView = view.findViewById(R.id.workout_scroll_view);
        cardLayout = view.findViewById(R.id.workout_card_ll);
        restTimerLayout = view.findViewById(R.id.active_workout_rest_timer_cl);
        resetButton = view.findViewById(R.id.btnReset);
        timerTextView = view.findViewById(R.id.active_workout_timer_tv);
        startStopButton = view.findViewById(R.id.btnStartStop);

        //Select planned workout UI items initialization
        plannedWorkoutsBackButton = view.findViewById(R.id.workout_planned_workouts_back_btn);
        plannedWorkoutsScrollView = view.findViewById(R.id.workout_planned_workouts_scroll_view);
        plannedWorkoutsLinearLayout = view.findViewById(R.id.workout_planned_workouts_ll);









        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNewWorkout();
            }
        });

        plannedWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goPlannedWorkouts();
            }

        });

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

        return view;
    }




    private void goNewWorkout() {
        if (plannedWorkout != null){
//                    titleTextView.setText(plannedWorkout.getName());
        }
        else {
            titleTextView.setText("Freestyle");
        }
        plannedWorkoutButton.setVisibility(View.GONE);
        myWorkoutsButton.setVisibility(View.GONE);
        startWorkoutButton.setVisibility(View.GONE);
        finishWorkoutButton.setVisibility(View.VISIBLE);
        cancelWorkoutButton.setVisibility(View.VISIBLE);
        exerciseScrollView.setVisibility(View.VISIBLE);
        restTimerLayout.setVisibility(View.VISIBLE);
        timerTextView.setVisibility(View.VISIBLE);



        cancelWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backNewWorkout();

            }
        });
    }

    private void backNewWorkout(){
        plannedWorkoutButton.setVisibility(View.VISIBLE);
        myWorkoutsButton.setVisibility(View.VISIBLE);
        startWorkoutButton.setVisibility(View.VISIBLE);
        finishWorkoutButton.setVisibility(View.GONE);
        cancelWorkoutButton.setVisibility(View.GONE);
        exerciseScrollView.setVisibility(View.GONE);
        restTimerLayout.setVisibility(View.GONE);
        timerTextView.setVisibility(View.GONE);
        titleTextView.setText("Workout");
    }


    private void goPlannedWorkouts(){
        plannedWorkoutButton.setVisibility(View.GONE);
        myWorkoutsButton.setVisibility(View.GONE);
        startWorkoutButton.setVisibility(View.GONE);
        plannedWorkoutsBackButton.setVisibility(View.VISIBLE);
        plannedWorkoutsScrollView.setVisibility(View.VISIBLE);
        plannedWorkoutsLinearLayout.setVisibility(View.VISIBLE);
        titleTextView.setText("Set Plan");

        plannedWorkoutsBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPlannedWorkouts();
            }
        });
    }

    private void backPlannedWorkouts(){
        plannedWorkoutButton.setVisibility(View.VISIBLE);
        myWorkoutsButton.setVisibility(View.VISIBLE);
        startWorkoutButton.setVisibility(View.VISIBLE);
        plannedWorkoutsBackButton.setVisibility(View.GONE);
        plannedWorkoutsScrollView.setVisibility(View.GONE);
        titleTextView.setText("Workout");
    }


}