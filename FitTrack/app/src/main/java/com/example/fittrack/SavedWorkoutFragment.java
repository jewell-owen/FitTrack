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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittrack.adapter.ExerciseAdapter;
import com.example.fittrack.adapter.SavedWorkoutAdapter;
import com.example.fittrack.viewmodel.SavedWorkoutViewModel;
import com.example.fittrack.viewmodel.WorkoutViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class SavedWorkoutFragment extends Fragment implements View.OnClickListener,  ExerciseAdapter.OnExerciseSelectedListener{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private SavedWorkoutViewModel mViewModel;

    private Query mQueryExercises;
    private ExerciseAdapter mAdapterExercises;


    private EditText savedWorkoutNameEditText;
    private ImageButton savedWorkoutBackButton;
    private ImageButton savedWorkoutAddExistingExerciseButton;
    private ImageButton savedWorkoutAddCustomExerciseButton;
    private Button savedWorkoutSaveButton;
    private RecyclerView myWorkoutExercisesRecyclerView;

    private String workout = "";
    private String id = "";

    public SavedWorkoutFragment() {
        // Required empty public constructor
    }

    public static SavedWorkoutFragment newInstance(String param1, String param2) {
        SavedWorkoutFragment fragment = new SavedWorkoutFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        View view = inflater.inflate(R.layout.fragment_saved_workout, container, false);

        savedWorkoutNameEditText = view.findViewById(R.id.saved_workout_name_et);
        savedWorkoutBackButton = view.findViewById(R.id.saved_workout_back_btn);
        savedWorkoutAddExistingExerciseButton = view.findViewById(R.id.saved_workout_add_existing_exercise_btn);
        savedWorkoutAddCustomExerciseButton = view.findViewById(R.id.saved_workout_add_custom_exercise_btn);
        savedWorkoutSaveButton = view.findViewById(R.id.workout_my_workout_save_btn);
        myWorkoutExercisesRecyclerView = view.findViewById(R.id.workout_saved_workout_exercises_recycler);

        myWorkoutExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myWorkoutExercisesRecyclerView.setAdapter(mAdapterExercises);



        Query query = null;
        mViewModel = new ViewModelProvider(this).get(SavedWorkoutViewModel.class);

        if (getArguments() != null){
            workout = getArguments().getString("workout");
            id = getArguments().getString("id");

            savedWorkoutNameEditText.setText(workout);
            query = db.collection("users").document(user.getUid()).collection("workouts").document(id).collection("exercises");
        }


        mQueryExercises = query;
        initSavedWorkoutExercisesRecyclerView();

        savedWorkoutBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });

        savedWorkoutAddExistingExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExerciseSelectFragment exerciseSelect = new ExerciseSelectFragment();
                Bundle args = new Bundle();
                args.putString("id", id);
                exerciseSelect.setArguments(args);
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, exerciseSelect)
                        .addToBackStack(null)
                    .commit();
            }
        });

        savedWorkoutAddCustomExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomExerciseFragment customExercise = new CustomExerciseFragment();
                Bundle args = new Bundle();
                args.putString("id", id);
                customExercise.setArguments(args);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, customExercise)
                        .addToBackStack(null)
                        .commit();
            }
        });

        savedWorkoutSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //addNewSavedWorkout();
            }
        });


        return view;
    }

    private void initSavedWorkoutExercisesRecyclerView() {
        if (mQueryExercises == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        mAdapterExercises = new ExerciseAdapter(mQueryExercises, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (myWorkoutExercisesRecyclerView.getVisibility() == View.VISIBLE){
                    //Handle update
                }

            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
            }
        };

        //myWorkoutsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myWorkoutExercisesRecyclerView.setAdapter(mAdapterExercises);
    }

    @Override
    public void onExerciseSelected(DocumentSnapshot restaurant) {

    }

//    private void addNewSavedWorkout(){
//        Map<String, Object> workout = new HashMap<>();
//        workout.put("name", workoutNameEditText.getText().toString());
//
//        db.collection("users").document(user.getUid()).collection("workouts")
//                .add(workout)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        workoutNameEditText.setText(""); // Clear input field
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                    }
//                });
//    }
//


}
