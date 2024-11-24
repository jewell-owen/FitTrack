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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fittrack.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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


    private static final String[] muscles = {"none", "abdominals", "abductors","adductors", "biceps", "calves", "chest",
            "forearms", "glutes", "hamstrings", "lats", "lower_back", "middle_back", "neck", "quadriceps", "traps", "triceps"};

    private static final String[] equipment = {"dumbbell", "barbell", "kettlebell", "medicine_ball", "cable", "smith_machine",};

    private static final String[] types = {"cardio", "olympic_weightlifting", "plyometrics", "powerlifting", "strength",
            "stretching", "strongman"};

    private static final String[] difficulties = {"beginner", "intermediate","expert"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_select_custom_exercise, container, false);

        if (getArguments() != null) {
            workoutId = getArguments().getString("id");
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
        ArrayAdapter<String> equipmentAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, equipment);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, types);
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, difficulties);

        muscleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customExerciseMuscleSpinner.setAdapter(muscleAdapter);
        customExerciseMuscleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

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
                String muscle = "";
                String equipment = "";
                String type = "";
                String difficulty = "";
                String instructions = customExerciseInstructionsEt.getText().toString();

                Map<String, Object> exercise = new HashMap<>();
                exercise.put("name", name);
                exercise.put("muscle", muscle);
                exercise.put("equipment", equipment);
                exercise.put("type", type);
                exercise.put("difficulty", difficulty);
                exercise.put("instructions", instructions);
                Log.d("TAG", "workoutId: " + workoutId);
                db.collection("users").document(user.getUid()).collection("workouts").document(workoutId).collection("exercises").add(exercise)
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
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

}
