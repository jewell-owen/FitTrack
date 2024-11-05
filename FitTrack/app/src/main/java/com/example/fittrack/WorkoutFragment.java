package com.example.fittrack;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import android.widget.Toast;

import com.example.fittrack.adapter.SavedWorkoutAdapter;
import com.example.fittrack.viewmodel.WorkoutViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkoutFragment extends Fragment implements View.OnClickListener, SavedWorkoutAdapter.OnWorkoutSelectedListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private WorkoutViewModel mViewModel;
    private Query mQuery;
    private SavedWorkoutAdapter mAdapter;

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
    private RecyclerView myWorkoutsRecyclerView;

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
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
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
        myWorkoutsRecyclerView = view.findViewById(R.id.workout_my_workouts_recycler);


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

        myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myWorkoutsRecyclerView.setAdapter(mAdapter);

        mViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        initRecyclerView();




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
                goMyWorkouts();
            }
        });

        return view;
    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapter = new SavedWorkoutAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    myWorkoutsRecyclerView.setVisibility(View.GONE);
                   // mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    myWorkoutsRecyclerView.setVisibility(View.VISIBLE);
                    //mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
               // Snackbar.make(findViewById(android.R.id.content), "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        //myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myWorkoutsRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onWorkoutSelected(DocumentSnapshot restaurant) {
        //@ToDo
        // Go to the saved workout fragment page for the selected restaurant


    }

    private void addNewSavedWorkout(){
        Map<String, Object> workout = new HashMap<>();
        workout.put("name", workoutNameEditText.getText().toString());

        db.collection("users").document(user.getUid()).collection("workouts")
                .add(workout)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        workoutNameEditText.setText(""); // Clear input field
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
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

    private void goMyWorkouts(){
        plannedWorkoutButton.setVisibility(View.GONE);
        myWorkoutsButton.setVisibility(View.GONE);
        startWorkoutButton.setVisibility(View.GONE);
        addNewMyWorkoutButton.setVisibility(View.VISIBLE);
        myWorkoutsBackButton.setVisibility(View.VISIBLE);
        myWorkoutsRecyclerView.setVisibility(View.VISIBLE);
        titleTextView.setText("Workouts");

        addNewMyWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goNewSavedWorkout();
            }
        });

        myWorkoutsBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                backMyWorkouts();
            }
        });
    }

    private void backMyWorkouts(){
        plannedWorkoutButton.setVisibility(View.VISIBLE);
        myWorkoutsButton.setVisibility(View.VISIBLE);
        startWorkoutButton.setVisibility(View.VISIBLE);
        addNewMyWorkoutButton.setVisibility(View.GONE);
        myWorkoutsBackButton.setVisibility(View.GONE);
        myWorkoutsRecyclerView.setVisibility(View.GONE);
        titleTextView.setText("Workout");
    }

    private void goNewSavedWorkout(){
        addNewMyWorkoutButton.setVisibility(View.GONE);
        myWorkoutsBackButton.setVisibility(View.GONE);
        myWorkoutsRecyclerView.setVisibility(View.GONE);
        myWorkoutExercisesScrollView.setVisibility(View.VISIBLE);
        cancelNewWorkoutButton.setVisibility(View.VISIBLE);
        workoutNameEditText.setVisibility(View.VISIBLE);
        addExerciseButton.setVisibility(View.VISIBLE);
        saveNewWorkoutButton.setVisibility(View.VISIBLE);
        titleTextView.setText("");

        cancelNewWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               backNewSavedWorkout();
            }
        });

        saveNewWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewSavedWorkout();
            }
        });

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExerciseSelectFragment exerciseSelect = new ExerciseSelectFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, exerciseSelect)
                        .commit();
            }
        });
    }

    private void backNewSavedWorkout(){
        myWorkoutExercisesScrollView.setVisibility(View.GONE);
        cancelNewWorkoutButton.setVisibility(View.GONE);
        workoutNameEditText.setVisibility(View.GONE);
        addExerciseButton.setVisibility(View.GONE);
        saveNewWorkoutButton.setVisibility(View.GONE);
        addNewMyWorkoutButton.setVisibility(View.VISIBLE);
        myWorkoutsBackButton.setVisibility(View.VISIBLE);
        myWorkoutsRecyclerView.setVisibility(View.VISIBLE);
        titleTextView.setText("Workouts");
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