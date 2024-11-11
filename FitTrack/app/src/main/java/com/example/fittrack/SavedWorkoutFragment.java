package com.example.fittrack;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fittrack.adapter.ExerciseAdapter;
import com.example.fittrack.adapter.SavedWorkoutAdapter;
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

    private Query mQueryExercises;
    private ExerciseAdapter mAdapterExercises;

    private RecyclerView myWorkoutExercisesRecyclerView;

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


        myWorkoutExercisesRecyclerView = view.findViewById(R.id.workout_saved_workout_exercises_recycler);

        myWorkoutExercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myWorkoutExercisesRecyclerView.setAdapter(mAdapterExercises);

        Query query = db.collection("users").document(user.getUid()).collection("workouts").document().collection("exercises");
        mQueryExercises = query;
        initSavedWorkoutExercisesRecyclerView();




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
                // Show a snackbar on errors
                // Snackbar.make(findViewById(android.R.id.content), "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
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
//     addExerciseButton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            ExerciseSelectFragment exerciseSelect = new ExerciseSelectFragment();
//            getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.flFragment, exerciseSelect)
//                    .commit();
//        }


}
