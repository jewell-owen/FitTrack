package com.example.fittrack.workout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittrack.R;
import com.example.fittrack.adapter.LogWorkoutAdapter;
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

public class ActiveWorkoutFragment extends Fragment implements View.OnClickListener, LogWorkoutAdapter.OnExerciseSelectedListener {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
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

    private String date = "";

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
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapterExercises != null) {
            mAdapterExercises.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapterExercises != null) {
            mAdapterExercises.stopListening();
        }
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

        myWorkoutExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        myWorkoutExercisesRecyclerView.setAdapter(mAdapterExercises);

        plannedWorkoutID = getArguments().getString("workoutId");
        plannedWorkoutName = getArguments().getString("workoutName");

        Log.d("TAG", "Workout ID: " + plannedWorkoutID);
        Log.d("TAG", "Name: " + plannedWorkoutName);

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

        // Starts the timer when pressing the "Start" button
        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Makes sure the timer resets if the user presses start while the timer is counting down
                if (countdown){
                    seconds = 0;
                }
                // If timer isn't already running, this is to start it
                if (!running){
                    startStopButton.setText(R.string.stop);
                    running = true;
                    wasRunning = false;
                    countdown = false;
                }
                else{
                    startStopButton.setText(R.string.start);
                    running = false;
                    countdown = false;
                }
                timerRunning();
            }
        });

        // Resets the timer when pressing the "Reset" button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = false;
                wasRunning = true;
                countdown = false;
                seconds = 0;
                startStopButton.setText(R.string.start);
                timerRunning();
            }
        });

        // Starts a countdown based on the time in the rest timer
        restTimerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = false;
                wasRunning = false;

                // get the time as a string
                String rest_time = restTimerTextView.getText().toString();
                rest_time = rest_time.substring(0, 4);
                subRest_time = rest_time.replaceAll(":", "");
                //Log.d("TAGoh", "TIME: " + subRest_time);

                // Convert that to an int
                int myNum = Integer.parseInt(subRest_time);
//                    // Add another second so the timer looks neater
//                    myNum += 1;

                seconds = myNum % 100;

                // Converting to seconds
                if (String.valueOf(myNum).length() >= 3) {
                    position = 3;
                    totalSec = (myNum / (int) Math.pow(10, position - 1)) % 10;
                    timerMin += totalSec;
                    //Log.d("tagMineTwo", "" + timerMin);
                    if (String.valueOf(myNum).length() >= 4) {
                        position = 4;
                        totalSec = (myNum / (int) Math.pow(10, position - 1)) % 10;
                        timerMin += totalSec * 10;
                        //Log.d("tagMinOne", "" + timerMin);
                        if (String.valueOf(myNum).length() >= 5) {
                            position = 5;
                            totalSec = (myNum / (int) Math.pow(10, position - 1)) % 10;
                            timerHour += totalSec;
                            if (String.valueOf(myNum).length() >= 6) {
                                position = 6;
                                totalSec = (myNum / (int) Math.pow(10, position - 1)) % 10;
                                timerHour += totalSec * 10;
                            }
                        }
                    }
                }
                timerMin *= 60;
                timerHour *= 3600;
                seconds += timerMin;
                seconds += timerHour;
                Log.d("tagSeconds", "sec: " + seconds);
                seconds *= 1000;
                // This boolean will make the timer count backwards
                countdown = true;
                timerRunning();
            }
        });


        cancelWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //@To-Do implement logic to delete logic from firebase
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

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


// Function that controls the timer
    public void timerRunning() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds /3600000 ;
                int minutes = (seconds % 3600000) / 60000;
                int secs = (seconds % 60000) / 1000;
                int millis = seconds % 1000;
                String time = String.format("%02d:%02d:%02d", hours, minutes, secs);

                timerTextView.setText(time);

                if (running) {
                    handler.postDelayed(this, 100);
                    seconds += 100;
                }

                if (wasRunning){
                    seconds = 0;
                }

                // For counting down from the rest time
                if (countdown){
                    handler.postDelayed(this, 100);
                    seconds -= 100;
                    if (timerTextView.getText().equals("00:00:00")){
                        countdown = false;
                    }
                }
            }

        });
    }


}
