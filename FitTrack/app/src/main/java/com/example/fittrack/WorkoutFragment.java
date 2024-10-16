package com.example.fittrack;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutFragment extends Fragment implements View.OnClickListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    //My workouts list UI items
    private ImageButton myWorkoutsBackButton;
    private ImageButton addNewMyWorkoutButton;
    private ScrollView myWorkoutsScrollView;
    private LinearLayout myWorkoutsLinearLayout;

    //Create new workout UI items
    private EditText workoutNameEditText;
    private ImageButton cancelNewWorkoutButton;
    private ImageButton addExerciseButton;
    private ScrollView myWorkoutExercisesScrollView;
    private LinearLayout myWorkoutExercisesLinearLayout;
    private Button saveNewWorkoutButton;

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
        titleTextView = view.findViewById(R.id.exercises_title_tv);
        startWorkoutButton = view.findViewById(R.id.workout_start_workout_btn);
        plannedWorkoutButton = view.findViewById(R.id.workout_add_plan_btn);
        myWorkoutsButton = view.findViewById(R.id.workout_my_workouts_btn);

        //Current workout UI items initialization
        finishWorkoutButton = view.findViewById(R.id.workout_finish_workout_btn);
        cancelWorkoutButton = view.findViewById(R.id.workout_cancel_workout_btn);
        exerciseScrollView = view.findViewById(R.id.workout_scroll_view);
        cardLayout = view.findViewById(R.id.workout_card_ll);
        restTimerLayout = view.findViewById(R.id.workout_rest_timer_cl);
        resetButton = view.findViewById(R.id.btnReset);
        timerTextView = view.findViewById(R.id.workout_timer_tv);
        startStopButton = view.findViewById(R.id.btnStartStop);

        //My workouts list UI items initialization
        addNewMyWorkoutButton = view.findViewById(R.id.workout_add_my_workout_btn);
        myWorkoutsBackButton = view.findViewById(R.id.workout_my_workouts_back_btn);
        myWorkoutsScrollView = view.findViewById(R.id.workout_my_workouts_scroll_view);
        myWorkoutsLinearLayout = view.findViewById(R.id.workout_my_workouts_ll);

        //Create new workout UI items initialization
        workoutNameEditText = view.findViewById(R.id.workout_name_et);
        cancelNewWorkoutButton = view.findViewById(R.id.workout_new_workout_back_btn);
        addExerciseButton = view.findViewById(R.id.workout_add_exercise_btn);
        myWorkoutExercisesScrollView = view.findViewById(R.id.workout_my_exercises_scroll_view);
        myWorkoutExercisesLinearLayout = view.findViewById(R.id.workout_my_exercises_ll);
        saveNewWorkoutButton = view.findViewById(R.id.workout_my_workout_save_btn);

        //Select planned workout UI items initialization
        plannedWorkoutsBackButton = view.findViewById(R.id.workout_planned_workouts_back_btn);
        plannedWorkoutsScrollView = view.findViewById(R.id.workout_planned_workouts_scroll_view);
        plannedWorkoutsLinearLayout = view.findViewById(R.id.workout_planned_workouts_ll);


        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                createEmptyExerciseCard();

                cancelWorkoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                });
            }
        });

        plannedWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        plannedWorkoutButton.setVisibility(View.VISIBLE);
                        myWorkoutsButton.setVisibility(View.VISIBLE);
                        startWorkoutButton.setVisibility(View.VISIBLE);
                        plannedWorkoutsBackButton.setVisibility(View.GONE);
                        plannedWorkoutsScrollView.setVisibility(View.GONE);
                        titleTextView.setText("Workout");
                    }
                });
            }

        });

        myWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plannedWorkoutButton.setVisibility(View.GONE);
                myWorkoutsButton.setVisibility(View.GONE);
                startWorkoutButton.setVisibility(View.GONE);
                addNewMyWorkoutButton.setVisibility(View.VISIBLE);
                myWorkoutsBackButton.setVisibility(View.VISIBLE);
                myWorkoutsScrollView.setVisibility(View.VISIBLE);
                titleTextView.setText("Workouts");

                addNewMyWorkoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addNewMyWorkoutButton.setVisibility(View.GONE);
                        myWorkoutsBackButton.setVisibility(View.GONE);
                        myWorkoutsScrollView.setVisibility(View.GONE);
                        myWorkoutExercisesScrollView.setVisibility(View.VISIBLE);
                        cancelNewWorkoutButton.setVisibility(View.VISIBLE);
                        workoutNameEditText.setVisibility(View.VISIBLE);
                        addExerciseButton.setVisibility(View.VISIBLE);
                        saveNewWorkoutButton.setVisibility(View.VISIBLE);
                        titleTextView.setText("");

                        cancelNewWorkoutButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myWorkoutExercisesScrollView.setVisibility(View.GONE);
                                cancelNewWorkoutButton.setVisibility(View.GONE);
                                workoutNameEditText.setVisibility(View.GONE);
                                addExerciseButton.setVisibility(View.GONE);
                                saveNewWorkoutButton.setVisibility(View.GONE);
                                addNewMyWorkoutButton.setVisibility(View.VISIBLE);
                                myWorkoutsBackButton.setVisibility(View.VISIBLE);
                                myWorkoutsScrollView.setVisibility(View.VISIBLE);
                                titleTextView.setText("Workouts");
                            }
                        });

                        saveNewWorkoutButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                        addExerciseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                });

                myWorkoutsBackButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        plannedWorkoutButton.setVisibility(View.VISIBLE);
                        myWorkoutsButton.setVisibility(View.VISIBLE);
                        startWorkoutButton.setVisibility(View.VISIBLE);
                        addNewMyWorkoutButton.setVisibility(View.GONE);
                        myWorkoutsBackButton.setVisibility(View.GONE);
                        myWorkoutsScrollView.setVisibility(View.GONE);
                        titleTextView.setText("Workout");
                    }
                });
            }
        });

        return view;
    }

    private void createEmptyExerciseCard() {
        // Create a new CardView for each card
        CardView cardView = createEmptyExerciseCardView();
        cardLayout.addView(cardView);
    }

    private CardView createEmptyExerciseCardView()  {
        // Inflate the CardView layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        CardView cardView = (CardView) inflater.inflate(R.layout.card_empty_exercise, cardLayout, false);

        // Set values to the views in the CardView
        ImageButton btnCard = cardView.findViewById(R.id.card_select_exercise_btn);

        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return cardView;
    }
}