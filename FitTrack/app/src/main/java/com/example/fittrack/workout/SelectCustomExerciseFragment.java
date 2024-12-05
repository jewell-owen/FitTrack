package com.example.fittrack.workout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fittrack.LoginActivity;
import com.example.fittrack.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SelectCustomExerciseFragment extends Fragment implements View.OnClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private ImageButton customExerciseBackButton;
    private EditText customExerciseNameEt;
    private EditText customExerciseInstructionsEt;
    private Spinner customExerciseMuscleSpinner;
    private Spinner customExerciseEquipmentSpinner;
    private Spinner customExerciseTypeSpinner;
    private Spinner customExerciseDifficultySpinner;
    private Button customExerciseSaveBtn;

    private String workoutId = "";

    private String muscle = "";
    private String equipment = "";
    private String type = "";
    private String difficulty = "";

    //Possible values: "savedWorkout", "loggedWorkout"
    //Variable needed to select which collection to add exercise to
    private String workoutType = "";

    private static final String[] muscles = {"select muscle","abdominals", "abductors","adductors", "biceps", "calves", "chest",
            "forearms", "glutes", "hamstrings", "lats", "lower back", "middle back", "neck", "quadriceps","shoulders", "traps", "triceps"};

    private static final String[] equipments = {"select equipment","dumbbell", "barbell", "kettlebell", "medicine ball", "cable", "smith machine", "exercise ball", "exercise band", "other"};

    private static final String[] types = {"select type","cardio", "olympic weightlifting", "plyometrics", "powerlifting", "strength",
            "stretching", "strongman"};

    private static final String[] difficulties = {"select difficulty","beginner", "intermediate","expert"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_select_custom_exercise, container, false);

        if (getArguments() != null) {
            workoutId = getArguments().getString("id");
            workoutType = getArguments().getString("workoutType");
        }

        customExerciseBackButton = view.findViewById(R.id.exercise_select_back_btn);
        customExerciseNameEt = view.findViewById(R.id.custom_exercise_name_et);
        customExerciseMuscleSpinner = view.findViewById(R.id.custom_exercise_spinner_muscle);
        customExerciseEquipmentSpinner = view.findViewById(R.id.custom_exercise_spinner_equipment);
        customExerciseTypeSpinner = view.findViewById(R.id.custom_exercise_spinner_type);
        customExerciseDifficultySpinner = view.findViewById(R.id.custom_exercise_spinner_difficulty);
        customExerciseInstructionsEt = view.findViewById(R.id.custom_exercise_instructions_et);
        customExerciseSaveBtn = view.findViewById(R.id.workout_my_workout_save_btn);

        ArrayAdapter<String> muscleAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, muscles);
        ArrayAdapter<String> equipmentAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, equipments);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, types);
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, difficulties);

        muscleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customExerciseMuscleSpinner.setAdapter(muscleAdapter);
        customExerciseMuscleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                muscle = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }
        );

        equipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customExerciseEquipmentSpinner.setAdapter(equipmentAdapter);
        customExerciseEquipmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                equipment = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }
        );

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customExerciseTypeSpinner.setAdapter(typeAdapter);
        customExerciseTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                type = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }
        );

        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customExerciseDifficultySpinner.setAdapter(difficultyAdapter);
        customExerciseDifficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                difficulty = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }
        );

        customExerciseBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        customExerciseSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = customExerciseNameEt.getText().toString();
                String instructions = customExerciseInstructionsEt.getText().toString();

                if (name.isEmpty() || instructions.isEmpty() || muscle.contains("select") || equipment.contains("select") || type.contains("select") || difficulty.contains("select")){
                    Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> exercise = new HashMap<>();
                exercise.put("name", name);
                exercise.put("muscle", muscle);
                exercise.put("equipment", equipment);
                exercise.put("type", type);
                exercise.put("difficulty", difficulty);
                exercise.put("instructions", instructions);
                Log.d("TAG", "workoutId: " + workoutId);
                CollectionReference workoutRef = null;
                if (workoutType.equals("savedWorkout")) {
                    workoutRef = db.collection("users").document(user.getUid()).collection("workouts").document(workoutId).collection("exercises");
                    if (workoutRef != null) {
                        workoutRef.add(exercise)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });

                    }
                }
                else if (workoutType.equals("loggedWorkout")) {
                    exercise.put("sets", 1);
                    workoutRef = db.collection("users").document(user.getUid()).collection("loggedWorkouts").document(workoutId).collection("exercises");
                    if (workoutRef != null) {
                        workoutRef.add(exercise)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        getActivity().getSupportFragmentManager().popBackStack();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });

                    }
                }
                else if (workoutType.equals("favoriteExercise")) {
                    DocumentReference favoriteRef = null;
                    Log.d("TAG", "workoutId: " + workoutId);
                    favoriteRef = db.collection("users").document(user.getUid()).collection("favorite").document(workoutId);
                    favoriteRef.update(exercise)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
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
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

}
