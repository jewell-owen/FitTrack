package com.example.fittrack;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomExerciseFragment extends Fragment implements View.OnClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private ImageButton customExerciseBackButton;
    private EditText customExerciseNameEt;
    private EditText customExerciseMuscleEt;
    private EditText customExerciseEquipmentEt;
    private EditText customExerciseTypeEt;
    private EditText customExerciseDifficultyEt;
    private Button customExerciseSaveBtn;

    private String workoutId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_custom_exercise, container, false);

        if (getArguments() != null) {
            workoutId = getArguments().getString("id");
        }

        customExerciseBackButton = view.findViewById(R.id.exercise_select_back_btn);
        customExerciseNameEt = view.findViewById(R.id.custom_exercise_name_et);
        customExerciseMuscleEt = view.findViewById(R.id.custom_exercise_muscle_et);
        customExerciseEquipmentEt = view.findViewById(R.id.custom_exercise_equipment_et);
        customExerciseTypeEt = view.findViewById(R.id.custom_exercise_type_et);
        customExerciseDifficultyEt = view.findViewById(R.id.custom_exercise_difficulty_et);
        customExerciseSaveBtn = view.findViewById(R.id.workout_my_workout_save_btn);

        Fragment temp = this;

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
                String muscle = customExerciseMuscleEt.getText().toString();
                String equipment = customExerciseEquipmentEt.getText().toString();
                String type = customExerciseTypeEt.getText().toString();
                String difficulty = customExerciseDifficultyEt.getText().toString();

                Map<String, Object> exercise = new HashMap<>();
                exercise.put("name", name);
                exercise.put("muscle", muscle);
                exercise.put("equipment", equipment);
                exercise.put("type", type);
                exercise.put("difficulty", difficulty);
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
