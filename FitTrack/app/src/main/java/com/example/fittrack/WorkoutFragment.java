package com.example.fittrack;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private ImageButton startWorkoutButton;
    private Button myWorkoutsButton;
    private Button plannedWorkoutButton;
    private ImageButton finishWorkoutButton;
    private ImageButton cancelWorkoutButton;
    private HorizontalScrollView exerciseScrollView;
    private Button resetButton;
    private TextView timerTextView;
    private Button startStopButton;
    private ConstraintLayout restTimerLayout;
    private LinearLayout cardLayout;
    private TextView titleTextView;


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
        startWorkoutButton = view.findViewById(R.id.workout_start_workout_btn);
        plannedWorkoutButton = view.findViewById(R.id.workout_add_plan_btn);
        myWorkoutsButton = view.findViewById(R.id.workout_my_workouts_btn);
        finishWorkoutButton = view.findViewById(R.id.workout_finish_workout_btn);
        cancelWorkoutButton = view.findViewById(R.id.workout_cancel_workout_btn);
        exerciseScrollView = view.findViewById(R.id.workout_scroll_view);
        resetButton = view.findViewById(R.id.btnReset);
        timerTextView = view.findViewById(R.id.workout_timer_tv);
        startStopButton = view.findViewById(R.id.btnStartStop);
        restTimerLayout = view.findViewById(R.id.workout_rest_timer_cl);
        titleTextView = view.findViewById(R.id.workout_title_tv);
        cardLayout = view.findViewById(R.id.workout_card_ll);


        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (plannedWorkout != null){
//                    titleTextView.setText(plannedWorkout.getName());
//                }
//                else {
//                    titleTextView.setText("Freestyle");
//                }
                plannedWorkoutButton.setVisibility(View.GONE);
                myWorkoutsButton.setVisibility(View.GONE);
                startWorkoutButton.setVisibility(View.GONE);
                finishWorkoutButton.setVisibility(View.VISIBLE);
                cancelWorkoutButton.setVisibility(View.VISIBLE);
                exerciseScrollView.setVisibility(View.VISIBLE);
                restTimerLayout.setVisibility(View.VISIBLE);
                timerTextView.setVisibility(View.VISIBLE);

                createCards();


            }
        });

        plannedWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });

        myWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private void createCards() {
        // Create a new CardView for each card
        CardView cardView = createCardView();
        cardLayout.addView(cardView);
    }

    private CardView createCardView()  {
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