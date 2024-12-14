package com.example.fittrack.workout;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittrack.R;
import com.example.fittrack.adapter.LogWorkoutAdapter;
import com.example.fittrack.viewmodel.TimerModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import android.os.Handler;

/**
 * A fragment used to handle all of the functionality for logging workouts
 */
public class ActiveWorkoutFragment extends Fragment implements View.OnClickListener, LogWorkoutAdapter.OnExerciseSelectedListener {

    /**
     * FireStore data base reference
     */
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Firebase authentication reference
     */
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * Firebase user reference
     */
    FirebaseUser user = mAuth.getCurrentUser();

    private Query mQueryExercises;
    private LogWorkoutAdapter mAdapterExercises;

    // Workout home screen UI items
    private EditText titleEditText;

    // Current workout UI items
    private ImageButton finishWorkoutButton;
    private ImageButton cancelWorkoutButton;
    private Button addCustomExerciseButton;
    private Button addLibraryExerciseButton;
    private RecyclerView myWorkoutExercisesRecyclerView;

    private ConstraintLayout restTimerLayout;
    // Timer layout UI items
    private Button resetButton;
    private TextView timerTextView;
    private Button startStopButton;
    private TextView restTimerTextView;


    private boolean newWorkoutCreated = false;

    //id of this logged workout in loggedWorkouts collection
    private String id = "";

    //id of workout from workout collection to populate exercises with
    private String plannedWorkoutID = "";
    private String plannedWorkoutName = null;

    // These are used for the timer
    private Handler handler = new Handler();
    private int seconds = 0;
    private boolean running = false;
    private boolean wasRunning = false;
    private boolean countdown = false;
    String subRest_time = "";
    int position, totalSec, timerMin, timerHour;
    private int tMin, tSec;

    private String date = "";

    private TimerModel viewModel;
    private boolean clockRunning = false;

    private Button new_restTime;
    private Button first_restTime;
    private Button second_restTime;
    private Button third_restTime;
    // Done
    private Button done_restTime;

    private TextView popupBorder;
    private NumberPicker numpickMin;
    private NumberPicker numpickSec;

    /**
     * Required empty public constructor
     */
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

    /**
     * Required onClick
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    /**
     * onStart method to start listening for Firestore updates
     */
    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapterExercises != null) {
            mAdapterExercises.startListening();
        }

    }

    /**
     * onStop method to stop listening for Firestore updates
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAdapterExercises != null) {
            mAdapterExercises.stopListening();
        }
    }

    /**
     * Called when the fragment is first created, checks rest timer time
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TimerModel.class);

        // Observe remaining time
        viewModel.getRemainingTime().observe(this, time -> {
            if (time != null) {
                restTimerTextView.setText(formatTime(time));
            }
        });
    }

    /**
     * Called when the fragment is visible to the user and actively running and updates timer
     */
    @Override
    public void onResume() {
        super.onResume();
        if (clockRunning) {
            viewModel.start(); // Resume timer if it was running
            updateStartStopButton(true);
        }
    }

    /**
     * Called to create view for the fragment, handles almost all initialization
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return view for the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_active_workout, container, false);

        //Workout home screen UI items initialization
        titleEditText = view.findViewById(R.id.active_workout_name_et);

        //Current workout UI items initialization
        finishWorkoutButton = view.findViewById(R.id.active_workout_finish_btn);
        cancelWorkoutButton = view.findViewById(R.id.active_workout_cancel_btn);
        addCustomExerciseButton = view.findViewById(R.id.active_workout_add_custom_exercise_btn);
        addLibraryExerciseButton = view.findViewById(R.id.active_workout_add_existing_exercise_btn);
        myWorkoutExercisesRecyclerView = view.findViewById(R.id.active_workout_exercises_recycler);

        restTimerLayout = view.findViewById(R.id.active_workout_rest_timer_cl);
        resetButton = view.findViewById(R.id.btnReset);
        timerTextView = view.findViewById(R.id.active_workout_timer_tv);
        startStopButton = view.findViewById(R.id.btnStartStop);
        restTimerTextView = view.findViewById(R.id.workout_rest_timer_tv);

        viewModel = new ViewModelProvider(this).get(TimerModel.class);


        myWorkoutExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        myWorkoutExercisesRecyclerView.setAdapter(mAdapterExercises);

        plannedWorkoutID = getArguments().getString("workoutId");
        plannedWorkoutName = getArguments().getString("workoutName");

        Log.d("TAG", "Workout ID: " + plannedWorkoutID);
        Log.d("TAG", "Name: " + plannedWorkoutName);

        //Handle logic for workout from a plan or freestyle workout depending on user selection
        if (!Objects.equals(plannedWorkoutName, "None")) {
            if (!newWorkoutCreated){
                Map<String, Object> newWorkout = new HashMap<>();
                newWorkout.put("name", plannedWorkoutName);
                titleEditText.setText(plannedWorkoutName);
                final String[] newWorkoutID = {""};
                db.collection("users").document(user.getUid()).collection("loggedWorkouts")
                        .add(newWorkout)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                newWorkoutCreated = true;
                                newWorkoutID[0] = documentReference.getId();
                                Query queryNewWorkout = null;
                                queryNewWorkout = db.collection("users").document(user.getUid()).collection("loggedWorkouts").document(newWorkoutID[0]).collection("exercises");
                                mQueryExercises = queryNewWorkout;
                                id = newWorkoutID[0];
                                initSavedWorkoutExercisesRecyclerView();

                                //Add exercise card for each exercise in "users", user id, "workout", plannedWorkoutID
                                db.collection("users")
                                        .document(user.getUid())
                                        .collection("workouts")
                                        .document(plannedWorkoutID)
                                        .collection("exercises")
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (DocumentSnapshot document : queryDocumentSnapshots) {
                                                    Map<String, Object> exerciseData = document.getData();
                                                    exerciseData.put("sets", 1);

                                                    db.collection("users")
                                                            .document(user.getUid())
                                                            .collection("loggedWorkouts")
                                                            .document(newWorkoutID[0])
                                                            .collection("exercises")
                                                            .add(exerciseData)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Log.d("TAG", "Exercise added: " + documentReference.getId());

                                                                    // Force RecyclerView update here
                                                                    if (mAdapterExercises != null) {
                                                                        mAdapterExercises.notifyDataSetChanged();
                                                                    }
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("TAG", "Error adding exercise", e);
                                                                }
                                                            });
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error fetching exercises", e);
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }
        }
        else {
            plannedWorkoutName = "Freestyle";
            if (!newWorkoutCreated){
                Map<String, Object> newWorkout = new HashMap<>();
                newWorkout.put("name", plannedWorkoutName);
                titleEditText.setText(plannedWorkoutName);
                final String[] newWorkoutID = {""};
                db.collection("users").document(user.getUid()).collection("loggedWorkouts")
                        .add(newWorkout)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                newWorkoutCreated = true;
                                newWorkoutID[0] = documentReference.getId();
                                Query queryNewWorkout = null;
                                queryNewWorkout = db.collection("users").document(user.getUid()).collection("loggedWorkouts").document(newWorkoutID[0]).collection("exercises");
                                mQueryExercises = queryNewWorkout;
                                id = newWorkoutID[0];
                                initSavedWorkoutExercisesRecyclerView();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }

        }

        //Starts and stops timer
        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countdown){
                    seconds = 0;
                }
                countdown = false;

                if (running){
                    startStopButton.setText(R.string.start);
                    running = false;
                }
                else{
                    startStopButton.setText(R.string.stop);
                    running = true;
                }
                timerRunning();
            }
        });

        // Resets the timer when pressing the "Reset" button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countdown = false;
                running = false;
                seconds = 0;
                timerRunning();
            }
        });


        // Starts a countdown based on the time in the rest timer
        restTimerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    countdown = true;
                    running = true;

                    // Fix the start/stop button
                    startStopButton.setText(R.string.stop);

                    // Gets the time from the rest time
                    String subString = restTimerTextView.getText().toString().replace(":", "");
                    int rest_time = Integer.parseInt(subString);

                    String timerText = restTimerTextView.getText().toString();
                    int minutes = 0, secondsPart = 0;

                    // Parse the string (assume MM:SS format)
                    if (timerText.contains(":")) {
                        String[] parts = timerText.split(":");
                        if (parts.length == 2) { // MM:SS
                            minutes = Integer.parseInt(parts[0]);
                            secondsPart = Integer.parseInt(parts[1]);
                        }
                    }

                    // Convert the parsed time into total seconds
                    seconds = (minutes * 60) + secondsPart;
                    seconds *= 1000; // Convert to milliseconds

                    timerRunning();
            }
        });

        restTimerTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // For pop up window
                // Inflate the layout for the popup menu
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popUpView = inflater.inflate(R.layout.fragment_active_workout_popup, null);


                // Creates pop up window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                // Lets tap outside popup menu to dismiss it
                Boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popUpView, width, height, focusable);


                // Show the popup window
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // elements
                new_restTime = popUpView.findViewById(R.id.new_resttime);
                first_restTime = popUpView.findViewById(R.id.first_resttime);
                second_restTime = popUpView.findViewById(R.id.second_resttime);
                third_restTime = popUpView.findViewById(R.id.third_resttime);
                // Done
                done_restTime = popUpView.findViewById(R.id.new_resttime);

                popupBorder = popUpView.findViewById(R.id.popupBorder);
                numpickMin = popUpView.findViewById(R.id.numPickMin);
                numpickSec = popUpView.findViewById(R.id.numPickSec);



                NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
                    @Override
                    public String format(int value) {
                        int temp = value * 5;
                        return "" + temp;
                    }
                };
                numpickSec.setFormatter(formatter);

                numpickMin.setMinValue(0);
                numpickMin.setMaxValue(5);
                numpickSec.setMinValue(0);
                numpickSec.setMaxValue(12);

                //new rest time
                new_restTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupBorder.setVisibility(View.VISIBLE);
                        numpickMin.setVisibility(View.VISIBLE);
                        numpickSec.setVisibility(View.VISIBLE);
                        // User presses this when they are done with the time
                        new_restTime.setText(R.string.rest_time_done);
                        // Changing the values of the rest time
                        new_restTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tMin = numpickMin.getValue();
                                tSec = numpickSec.getValue();
                                tSec *= 5; // Due to formatting the picker
                                showTime(tMin, tSec);
                                popupWindow.dismiss();
                            }
                        });
                    }
                });

                // First Rest time
                first_restTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newRestTime = first_restTime.getText().toString();
                        restTimerTextView.setText(newRestTime);
                        popupWindow.dismiss();
                    }
                });

                // First Rest time
                second_restTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newRestTime = second_restTime.getText().toString();
                        restTimerTextView.setText(newRestTime);
                        popupWindow.dismiss();
                    }
                });

                // First Rest time
                third_restTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newRestTime = third_restTime.getText().toString();
                        restTimerTextView.setText(newRestTime);
                        popupWindow.dismiss();
                    }
                });

                return true;
            }
        });

        //Delete workout in FireStore
        cancelWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference workoutRef = db.collection("users")
                        .document(user.getUid())
                        .collection("loggedWorkouts")
                        .document(id);

                // Delete the document from Firestore
                workoutRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Close the fragment/activity upon successful deletion
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Log or handle the failure
                                Log.e("FirestoreDelete", "Error deleting document", e);
                            }
                        });
            }
        });

        // Update workout data in FireStore
        finishWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the workout name from the EditText
                String newWorkoutName = titleEditText.getText().toString().trim();

                // Reference to the workout document in Firestore
                DocumentReference workoutRef = db.collection("users")
                        .document(user.getUid())
                        .collection("loggedWorkouts")
                        .document(id);

                // Create a map with the updated name
                Map<String, Object> updates = new HashMap<>();
                updates.put("name", newWorkoutName);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Match the format used in Firestore
                date = sdf.format(calendar.getTime());
                updates.put("date", date);

                // Update the "name" field in Firestore
                workoutRef.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Close the fragment/activity upon successful update
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Log or handle the failure
                            }
                        });
            }
        });

        // Go to custom exercise creation fragment
        addCustomExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectCustomExerciseFragment customExercise = new SelectCustomExerciseFragment();
                Bundle args = new Bundle();
                args.putString("id", id);
                args.putString("workoutType", "loggedWorkout");
                customExercise.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, customExercise)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Go to API library exercise selection fragment
        addLibraryExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectLibraryExerciseFragment exerciseSelect = new SelectLibraryExerciseFragment();
                Bundle args = new Bundle();
                args.putString("id", id);
                args.putString("workoutType", "loggedWorkout");
                exerciseSelect.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, exerciseSelect)
                        .addToBackStack(null)
                        .commit();

            }
        });

        return view;
    }

    /**
     * Updates the start/stop button text based on the running state
     * @param isRunning True if the timer is running, false otherwise
     */
    private void updateStartStopButton(boolean isRunning) {
        if (isRunning) {
            startStopButton.setBackgroundColor(getResources().getColor(R.color.redButton));
            startStopButton.setText(getString(R.string.stop));
        } else {
            startStopButton.setBackgroundColor(getResources().getColor(R.color.greenButton));
            startStopButton.setText(getString(R.string.start));
        }
    }

    /**
     * Formats the time in milliseconds to a string in the format "MM:SS"
     * @param millis time in milliseconds
     * @return formatted time as a string
     */
    private String formatTime(long millis) {
        long minutes = (millis / 60000);
        long seconds = (millis % 60000) / 1000;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    /**
     * Initializes the RecyclerView for displaying exercises in the workout
     */
    private void initSavedWorkoutExercisesRecyclerView() {
        if (mQueryExercises == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
            return;
        }

        mAdapterExercises = new LogWorkoutAdapter(mQueryExercises, this) {
            @Override
            protected void onDataChanged() {
                if (mAdapterExercises.getItemCount() == 0) {
                    Log.d("TAG", "No exercises found.");
                } else {
                    Log.d("TAG", "Exercises loaded.");
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("TAG", "Error loading exercises", e);
            }
        };

        myWorkoutExercisesRecyclerView.setAdapter(mAdapterExercises);
        mAdapterExercises.startListening(); // Ensure the adapter starts listening for changes.
    }

    /**
     * Handles the click event for a specific exercise to go view full information in new fragment
     * @param exercise DocumentSnapshot representing the exercise
     */
    @Override
    public void onExerciseMoreInfo(DocumentSnapshot exercise) {
        Bundle bundle = new Bundle();
        bundle.putString("name", exercise.getString("name"));
        bundle.putString("type", exercise.getString("type"));
        bundle.putString("muscle", exercise.getString("muscle"));
        bundle.putString("equipment", exercise.getString("equipment"));
        bundle.putString("difficulty", exercise.getString("difficulty"));
        bundle.putString("instructions", exercise.getString("instructions"));
        ViewExerciseFragment viewExerciseFragment = new ViewExerciseFragment();
        viewExerciseFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, viewExerciseFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Handles the click event for a specific exercise to be deleted
     * @param exercise DocumentSnapshot representing the exercise to be deleted
     */
    @Override
    public void onExerciseDeleted(DocumentSnapshot exercise) {
        DocumentReference workoutRef = db.collection("users")
                .document(user.getUid())
                .collection("loggedWorkouts")
                .document(id)
                .collection("exercises")
                .document(exercise.getId());

        // Delete the workout document
        workoutRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    /**
     * Handles the click event for a specific exercise to have its sets increased
     * @param snapshot DocumentSnapshot representing the exercise
     * @param sets Current number of sets
     */
    @Override
    public void onSetAdded(DocumentSnapshot snapshot, int sets) {
        DocumentReference exerciseRef = db.collection("users")
                .document(user.getUid())
                .collection("loggedWorkouts")
                .document(id)
                .collection("exercises")
                .document(snapshot.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("sets",  sets);

        // Update the "name" field in Firestore
        exerciseRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log or handle the failure
                    }
                });
    }

    /**
     * Handles the click event for a specific exercise to have its sets decreased
     * @param snapshot DocumentSnapshot representing the exercise
     * @param sets Current number of sets
     */
    @Override
    public void onSetDeleted(DocumentSnapshot snapshot, int sets) {
        DocumentReference exerciseRef = db.collection("users")
                .document(user.getUid())
                .collection("loggedWorkouts")
                .document(id)
                .collection("exercises")
                .document(snapshot.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("sets",  sets);

        String oldSet = Integer.toString(sets+1);
        String oldSetString = "";
        if (oldSet.equals("6")){
            oldSetString = "six";
        }
        else if (oldSet.equals("5")){
            oldSetString = "five";
        }
        else if (oldSet.equals("4")){
            oldSetString = "four";
        }
        else if (oldSet.equals("3")){
            oldSetString = "three";
        }
        else if (oldSet.equals("2")) {
            oldSetString = "two";
        }
        String fieldWeight = oldSetString + "Weight";
        String fieldReps = oldSetString + "Reps";
        updates.put(fieldWeight, "");
        updates.put(fieldReps, "");

        // Update the "name" field in Firestore
        exerciseRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log or handle the failure
                    }
                });
    }

    /**
     * Handles the click event for a specific exercise to have its weight changed
     * @param snapshot DocumentSnapshot representing the exercise
     * @param set Current set
     * @param weight New weight
     */
    @Override
    public void onWeightChanged(DocumentSnapshot snapshot, String set, String weight) {
        // Reference to the workout document in Firestore
        DocumentReference exerciseRef = db.collection("users")
                .document(user.getUid())
                .collection("loggedWorkouts")
                .document(id)
                .collection("exercises")
                .document(snapshot.getId());

        // Create a map with the updated name
        Map<String, Object> updates = new HashMap<>();
        String field = set + "Weight";
        updates.put(field, weight);

        // Update the "name" field in Firestore
        exerciseRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log or handle the failure
                    }
                });
    }

    /**
     * Handles the click event for a specific exercise to have its reps changed
     * @param snapshot DocumentSnapshot representing the exercise
     * @param set Current set
     * @param reps New reps
     */
    @Override
    public void onRepsChanged(DocumentSnapshot snapshot, String set, String reps) {
        // Reference to the workout document in Firestore
        DocumentReference exerciseRef = db.collection("users")
                .document(user.getUid())
                .collection("loggedWorkouts")
                .document(id)
                .collection("exercises")
                .document(snapshot.getId());

        // Create a map with the updated name
        Map<String, Object> updates = new HashMap<>();
        String field = set + "Reps";
        updates.put(field, reps);

        // Update the "name" field in Firestore
        exerciseRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Close the fragment/activity upon successful update
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log or handle the failure
                    }
                });
    }


    /**
     * Starts the timer
     */
    public void timerRunning() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600000;
                int minutes = (seconds % 3600000) / 60000;
                int secs = (seconds % 60000) / 1000;
                String time = String.format("%02d:%02d:%02d", hours, minutes, secs);
                timerTextView.setText(time);


                if (countdown && seconds > 0){
                    handler.postDelayed(this, 100);
                    seconds -= 100;
                }
                else if (running) {
                    handler.postDelayed(this, 100);
                    seconds += 100;
                }
            }

        });
    }

    /**
     * Updates the rest timer text
     * @param tMin Time in minutes
     * @param tSec Time in seconds
     */
    public void showTime(int tMin, int tSec){
        String formattedTime = String.format("%02d:%02d", tMin, tSec);
        restTimerTextView.setText(formattedTime);
    }


}
